package me.lofro.pelicano2.global.mixins;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.server.updates.Updates;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(RaiderEntity.class)
public class IllagerEntityMixin extends PatrolEntity {

    @Unique
    private static final Random RANDOM = new Random();
    @Unique
    private int chaosCooldown = 0;

    protected IllagerEntityMixin(EntityType<? extends PatrolEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        if (this.getWorld().isClient) return;

        if (Main.getInstance() != null && Main.getInstance().serverManager() != null && Main.getInstance().serverManager().getUpdatesManager() != null) {
            if (Main.getInstance().serverManager().getUpdatesManager().getUpdatesData().getCurrentUpdates().contains(Updates.ILLAGERS_FUEGO)) {
                ServerWorld world = (ServerWorld) this.getWorld();

                if (chaosCooldown > 0) {
                    chaosCooldown--;
                    return;
                }

                if (this.getTarget() != null && this.isAlive()) {
                    double chance = RANDOM.nextDouble();

                    // 50% chance de lanzar una bola de fuego, 50% de invocar monstruo
                    if (chance < 0.5) {
                        lanzarBolaDeFuego(world);
                    } else {
                        invocarMonstruo(world);
                    }

                    // Reinicia el cooldown (entre 8 y 12 segundos)
                    chaosCooldown = 160 + RANDOM.nextInt(80);
                }
            }
        }
    }

    @Unique
    private void lanzarBolaDeFuego(ServerWorld world) {
        if (this.getTarget() == null) return;

        Vec3d targetPos = this.getTarget().getPos();
        Vec3d look = targetPos.subtract(this.getPos()).normalize();

        FireballEntity fireball = new FireballEntity(world, this,
                look.x, look.y, look.z, 3);
        fireball.refreshPositionAndAngles(
                this.getX(),
                this.getEyeY() - 0.1,
                this.getZ(),
                this.getYaw(),
                this.getPitch());

        world.spawnEntity(fireball);
        world.syncWorldEvent(null, 1018, this.getBlockPos(), 0); // sonido de blaze
    }

    @Unique
    private void invocarMonstruo(ServerWorld world) {
        EntityType<?>[] posibles = {
                EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER
        };
        EntityType<?> tipo = posibles[RANDOM.nextInt(posibles.length)];
        MobEntity mob = (MobEntity) tipo.create(world);
        if (mob == null) return;

        Vec3d pos = this.getPos().add(
                (RANDOM.nextDouble() - 0.5) * 3,
                0,
                (RANDOM.nextDouble() - 0.5) * 3);
        mob.refreshPositionAndAngles(pos.x, pos.y, pos.z, RANDOM.nextFloat() * 360F, 0.0F);

        world.spawnEntity(mob);
        world.syncWorldEvent(null, 2001, this.getBlockPos(), 0); // partículas al invocar
    }

}
