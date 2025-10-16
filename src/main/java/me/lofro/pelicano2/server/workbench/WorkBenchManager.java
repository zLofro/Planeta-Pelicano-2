package me.lofro.pelicano2.server.workbench;

import lombok.Getter;
import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.data.api.Restorable;
import me.lofro.pelicano2.global.data.util.JsonConfig;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.server.ModServer;
import me.lofro.pelicano2.server.workbench.data.ActiveCraft;
import me.lofro.pelicano2.server.workbench.data.WorkBenchData;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.*;

public class WorkBenchManager implements Restorable {

    private final ModServer modServer;

    private @Getter WorkBenchData workBenchData;

    private final @Getter Map<UUID, ActiveCraft> activeCrafts;

    private int tickCounter = 0;

    public WorkBenchManager(ModServer modServer) {
        this.modServer = modServer;

        this.activeCrafts = new HashMap<>();

        this.restore(modServer.getDataManager().getWorkbenchData());

        ServerTickEvents.END_SERVER_TICK.register((MinecraftServer server) -> {
            tickCounter++;

            if (tickCounter >= 20) {
                tickCounter = 0;
                this.tick();
            }
        });
    }

    public void openGui(ServerPlayerEntity player) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        packetByteBuf.writeInt(timeLeft(player.getUuid()));
        packetByteBuf.writeBoolean(activeCraft(player.getUuid()) || isReady(player.getUuid()));

        int currentResultID;

        if (isReady(player.getUuid())) {
            currentResultID = Registries.ITEM.getRawId(getWorkBenchData().getCrafts().get(player.getUuid()).getLeft());
        } else if (activeCraft(player.getUuid())) {
            currentResultID = Registries.ITEM.getRawId(activeCrafts.get(player.getUuid()).getItem());
        } else {
            currentResultID = -1;
        }

        packetByteBuf.writeInt(currentResultID);

        ServerPlayNetworking.send(player, PacketIDS.OPEN_WORKBENCH_SCREEN_S2C, packetByteBuf);
    }

    public void claimResult(ServerPlayerEntity player) {
        if (isReady(player.getUuid())) {
            var crafts = workBenchData.getCrafts();
            player.giveItemStack(new ItemStack(crafts.get(player.getUuid()).getLeft()));
            crafts.remove(player.getUuid());
        }
    }

    public boolean startCraft(ServerPlayerEntity player, WorkBenchRecipe workBenchRecipe) {
        if (!activeCraft(player.getUuid())) {
            if (workBenchRecipe != null && hasAllItems(player, workBenchRecipe.getRecipe())) {
                consumeRecipeItems(player, workBenchRecipe);

                activeCrafts.put(player.getUuid(), new ActiveCraft(workBenchRecipe.getResult(), workBenchRecipe.seconds, System.currentTimeMillis()));

                return true;
            }
            return false;
        }

        return false;
    }

    public void tick() {
        long now = System.currentTimeMillis();

        List<UUID> toRemove = new ArrayList<>();

        activeCrafts.forEach((uuid, craft) -> {
            if (craft.getRemainingSeconds(now) <= 0) {
                workBenchData.getCrafts().put(uuid, new Pair<>(craft.getItem(), -1));
                toRemove.add(uuid);
            }
        });

        toRemove.forEach(activeCrafts::remove);
    }

    private ActiveCraft getActiveCraft(UUID playerId) {
        return activeCrafts.get(playerId);
    }

    /**
     * Elimina del inventario del jugador los ítems requeridos por la receta.
     * Asume que el jugador tiene todos los ítems (ya verificado previamente).
     *
     * @param player el jugador
     * @param recipe la receta a consumir
     */
    public void consumeRecipeItems(ServerPlayerEntity player, WorkBenchRecipe recipe) {
        if (recipe == null) return;

        for (Pair<Item, Integer> stackInfo : recipe.getRecipe()) {
            if (stackInfo.getLeft() == null) break;

            ItemStack stackNeeded = new ItemStack(stackInfo.getLeft(), stackInfo.getRight());

            int remainingToConsume = stackNeeded.getCount(); // cantidad requerida

            // Recorremos el inventario buscando ítems del mismo tipo
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack invStack = player.getInventory().getStack(i);

                // Coincidencia exacta de tipo de ítem
                if (invStack.isOf(stackNeeded.getItem()) && !invStack.isEmpty()) {
                    int available = invStack.getCount();

                    // Determinamos cuántos tomar de este stack
                    int toConsume = Math.min(available, remainingToConsume);
                    invStack.decrement(toConsume);
                    remainingToConsume -= toConsume;

                    // Si ya consumimos lo necesario, pasamos al siguiente ingrediente
                    if (remainingToConsume <= 0) {
                        break;
                    }
                }
            }

            // En teoría nunca debería pasar si antes se verificó con hasAllItems()
            if (remainingToConsume > 0) {
                System.err.println("Advertencia: faltaron ítems al consumir receta ("
                        + stackNeeded.getItem().toString() + "), faltaban " + remainingToConsume);
            }
        }

        // Actualiza el inventario del jugador para reflejar los cambios visualmente
        player.getInventory().markDirty();
    }

    /**
     * Comprueba si el jugador tiene todos los ítems requeridos (con cantidades incluidas).
     * No los consume, solo verifica.
     */
    private boolean hasAllItems(ServerPlayerEntity player, Pair<Item, Integer>[] stacksInfo) {
        // Copia mutable del inventario (solo los counts)
        int[] availableCounts = new int[player.getInventory().size()];

        // Guardamos una copia de las cantidades disponibles
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            availableCounts[i] = stack.getCount();
        }

        for (Pair<Item, Integer> stackInfo : stacksInfo) {
            if (stackInfo.getLeft() == null) return false;

            ItemStack required = new ItemStack(stackInfo.getLeft(), stackInfo.getRight());

            int requiredCount = required.getCount();
            boolean fulfilled = false;

            // Recorremos el inventario para ver si hay suficientes ítems iguales
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack invStack = player.getInventory().getStack(i);

                if (invStack.isOf(required.getItem()) && availableCounts[i] > 0) {
                    int used = Math.min(availableCounts[i], requiredCount);
                    availableCounts[i] -= used;
                    requiredCount -= used;

                    if (requiredCount <= 0) {
                        fulfilled = true;
                        break;
                    }
                }
            }

            // Si después de revisar el inventario aún faltan ítems no cumple
            if (!fulfilled && requiredCount > 0) {
                return false;
            }
        }

        return true;
    }

    public void removeCraft(UUID player) {
        this.activeCrafts.remove(player);
        workBenchData.getCrafts().remove(player);
    }

    public boolean isReady(UUID player) {
        var crafts = workBenchData.getCrafts();
        return crafts.containsKey(player) && crafts.get(player).getRight() == -1;
    }

    public boolean activeCraft(UUID player) {
        return getActiveCraft(player) != null;
    }

    public int timeLeft(UUID player) {
        if (activeCraft(player)) {
            return getActiveCraft(player).getRemainingSeconds(System.currentTimeMillis());
        }

        return -1;
    }

    @Override
    public void restore(JsonConfig jsonConfig) {
        if (jsonConfig.getJsonObject().entrySet().isEmpty()) {
            this.workBenchData = new WorkBenchData();
        } else {
            this.workBenchData = Main.gson().fromJson(jsonConfig.getJsonObject(), WorkBenchData.class);
        }

        workBenchData.getCrafts().forEach(((uuid, itemIntegerPair) -> {
            if (itemIntegerPair != null) {
                activeCrafts.put(uuid, new ActiveCraft(itemIntegerPair.getLeft(), itemIntegerPair.getRight(), System.currentTimeMillis()));
            }
        }));
    }

    @Override
    public void save(JsonConfig jsonConfig) {
        this.activeCrafts.forEach((uuid, activeCraft) -> {
            if (activeCraft != null) {
                workBenchData.getCrafts().put(uuid, new Pair<>(activeCraft.getItem(), activeCraft.getRemainingSeconds(System.currentTimeMillis())));
            }
        });

        jsonConfig.setJsonObject(Main.gson().toJsonTree(workBenchData).getAsJsonObject());
        try {
            jsonConfig.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
