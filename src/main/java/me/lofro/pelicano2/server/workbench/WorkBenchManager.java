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

                activeCrafts.put(player.getUuid(), new ActiveCraft(workBenchRecipe.getResult(), 10, System.currentTimeMillis()));

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
     * Elimina del inventario del jugador los items requeridos por la receta.
     * No verifica si el jugador tiene los ítems, se asume que ya se comprobó con getCraftableRecipe().
     *
     * @param player el jugador
     * @param recipe la receta a consumir
     */
    public void consumeRecipeItems(ServerPlayerEntity player, WorkBenchRecipe recipe) {
        if (recipe == null) return;

        for (Item itemNeeded : recipe.getRecipe()) {
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (stack.isOf(itemNeeded) && !stack.isEmpty()) {
                    stack.decrement(1); // consume 1 unidad
                    break; // pasa al siguiente ingrediente
                }
            }
        }
        player.getInventory().markDirty(); // actualiza visualmente el inventario
    }

    /**
     * Comprueba si el jugador tiene todos los items de la receta.
     * No los consume, solo verifica.
     */
    private boolean hasAllItems(ServerPlayerEntity player, Item[] items) {
        // Copia mutable del inventario para no contar el mismo item dos veces
        int[] counts = new int[player.getInventory().size()];

        // Creamos una copia de las stacks para evitar modificaciones
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            counts[i] = stack.getCount();
        }

        // Por cada ingrediente, buscamos si el jugador lo tiene
        for (Item itemNeeded : items) {
            boolean found = false;
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (stack.isOf(itemNeeded) && counts[i] > 0) {
                    counts[i]--; // "usar" un item
                    found = true;
                    break;
                }
            }
            if (!found) return false; // no se encontró uno de los ingredientes
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
