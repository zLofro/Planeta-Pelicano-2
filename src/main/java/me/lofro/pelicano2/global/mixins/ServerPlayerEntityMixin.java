package me.lofro.pelicano2.global.mixins;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.accessors.ServerPlayerEntityAccessor;
import me.lofro.pelicano2.global.item.ModItems;
import me.lofro.pelicano2.server.entity.entities.VoidFountainEntity;
import me.lofro.pelicano2.server.updates.Updates;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityAccessor {

    @Unique
    int zombiesKilled = 0;
    @Unique
    int skeletonsKilled = 0;
    @Unique
    int spidersKilled = 0;
    @Unique
    int creepersKilled = 0;
    @Unique
    int vultresKilled = 0;

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("zombiesKilled", this.zombiesKilled);
        nbt.putInt("skeletonsKilled", this.skeletonsKilled);
        nbt.putInt("spidersKilled", this.spidersKilled);
        nbt.putInt("creepersKilled", this.creepersKilled);
        nbt.putInt("vultresKilled", this.vultresKilled);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("zombiesKilled")) this.zombiesKilled = nbt.getInt("zombiesKilled");
        if (nbt.contains("skeletonsKilled")) this.skeletonsKilled = nbt.getInt("skeletonsKilled");
        if (nbt.contains("spidersKilled")) this.spidersKilled = nbt.getInt("spidersKilled");
        if (nbt.contains("creepersKilled")) this.creepersKilled = nbt.getInt("creepersKilled");
        if (nbt.contains("vultresKilled")) this.vultresKilled = nbt.getInt("vultresKilled");
    }

    @Override public void planetaPelicano2$incrementZombieKills() { this.zombiesKilled++; }
    @Override public void planetaPelicano2$incrementSkeletonKills() { this.skeletonsKilled++; }
    @Override public void planetaPelicano2$incrementSpiderKills() { this.spidersKilled++; }
    @Override public void planetaPelicano2$incrementCreeperKills() { this.creepersKilled++; }
    @Override public void planetaPelicano2$incrementVultureKills() { this.vultresKilled++; }

    @Override public int planetaPelicano2$getZombiesKilled() { return this.zombiesKilled; }
    @Override public int planetaPelicano2$getSkeletonsKilled() { return this.skeletonsKilled; }
    @Override public int planetaPelicano2$getSpidersKilled() { return this.spidersKilled; }
    @Override public int planetaPelicano2$getCreepersKilled() { return this.creepersKilled; }
    @Override public int planetaPelicano2$getVultresKilled() { return this.vultresKilled; }

    @Unique
    private static final Random random = new Random();

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("TAIL"))
    private void onDropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
        World world = player.getWorld();
        ItemEntity dropped = cir.getReturnValue();

        if (world.isClient() || dropped == null) return;

        if (stack.isOf(ModItems.UPGRADE_BALL)) {
            double radius = 2.0D;
            List<VoidFountainEntity> fountains = world.getEntitiesByClass(
                    VoidFountainEntity.class,
                    new Box(dropped.getX() - radius, dropped.getY() - radius, dropped.getZ() - radius,
                            dropped.getX() + radius, dropped.getY() + radius, dropped.getZ() + radius),
                    entity -> true
            );

            if (!fountains.isEmpty()) {
                VoidFountainEntity fountain = fountains.getFirst();

                dropped.discard();
                fountain.onItemOffered(player);
            }
        }
    }

    @Unique
    private static final double DROP_CHANCE = 0.01; // 1% de probabilidad

    @Inject(method = "damage", at = @At("TAIL"))
    private void onPlayerDamaged(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (Main.getInstance() != null && Main.getInstance().serverManager() != null && Main.getInstance().serverManager().getUpdatesManager() != null) {
            if (Main.getInstance().serverManager().getUpdatesManager().getUpdatesData().getCurrentUpdates().contains(Updates.CAIDA_ITEMS)) {
                ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

                // Solo 1% de probabilidad
                if (random.nextDouble() <= DROP_CHANCE) {

                    // Buscar un stack aleatorio no vacío
                    int invSize = player.getInventory().size();
                    int attempts = 0;

                    while (attempts < 10) { // hasta 10 intentos para encontrar un stack válido
                        int randomSlot = random.nextInt(invSize);
                        ItemStack stack = player.getInventory().getStack(randomSlot);

                        if (!stack.isEmpty()) {
                            player.dropItem(stack, true); // true = se lanza con fuerza natural
                            player.getInventory().markDirty();
                            break;
                        }
                        attempts++;
                    }
                }
            }
        }
    }

    @Inject(method = "wakeUp(ZZ)V", at = @At("TAIL"))
    private void onWakeUp(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci) {
        if (random.nextDouble() < 0.3 && Main.getInstance() != null && Main.getInstance().serverManager() != null && Main.getInstance().serverManager().getUpdatesManager() != null) {
            if (Main.getInstance().serverManager().getUpdatesManager().getUpdatesData().getCurrentUpdates().contains(Updates.SUENIO_DESORDENADO)) {
                ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
                DefaultedList<ItemStack> items = player.getInventory().main;

                Collections.shuffle(items, random);

                player.sendMessage(Text.literal("Te levantaste con el inventario... revuelto.").formatted(Formatting.RED));
            }
        }
    }

}
