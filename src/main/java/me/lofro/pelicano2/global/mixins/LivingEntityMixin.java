package me.lofro.pelicano2.global.mixins;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.server.entity.ModEntities;
import me.lofro.pelicano2.server.entity.entities.AngrySalmonEntity;
import me.lofro.pelicano2.server.skills.data.Skills;
import me.lofro.pelicano2.server.updates.Updates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(
            method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float modifyDamageDealt(float amount, DamageSource source) {
        if (source.getAttacker() instanceof PlayerEntity player) {
            if (!this.getWorld().isClient) {

                int level = Main.getInstance().serverManager().getSkillTreeManager().getSkillLevel(player.getUuid(), Skills.STRENGTH);

                // Escalado lineal: nivel 10 = x2 daño
                float multiplier = 1.0F + (level / 10.0F);
                return amount * multiplier;
            }
        }
        return amount;
    }

    // Modifica el daño recibido (aplicado al jugador que recibe daño)
    @ModifyVariable(
            method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float modifyDamageTaken(float amount, DamageSource source) {
        Object self = this;
        if (self instanceof PlayerEntity player) {
            if (!this.getWorld().isClient) {

                int level = Main.getInstance().serverManager().getSkillTreeManager().getSkillLevel(player.getUuid(), Skills.RESISTANCE);

                // Escalado lineal: nivel 10 = x2 daño
                float multiplier = 1.0F - (level / 20.0F); // 10 → 0.5
                return amount * multiplier;
            }
        }
        return amount;
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onDeath(DamageSource source, CallbackInfo ci) {
        if (Main.getInstance() != null) {
            LivingEntity entity = (LivingEntity) (Object) this;
            World world = entity.getWorld();
            if (!world.isClient) {
                ServerWorld serverWorld = (ServerWorld) world;
                if (Main.getInstance().serverManager().getUpdatesManager().getUpdatesData().getCurrentUpdates().contains(Updates.ARANIAS_MULTIPLICAN)) {
                    if (entity instanceof SpiderEntity spider) {

                        float currentScale = ScaleTypes.BASE.getScaleData(spider).getScale();

                        if (currentScale == 1.0F) {
                            for (int i = 0; i < 3; i++) {
                                SpiderEntity baby = EntityType.SPIDER.create(serverWorld);
                                if (baby == null) continue;

                                Vec3d pos = entity.getPos().add(
                                        (serverWorld.random.nextDouble() - 0.5) * 1.5,
                                        0,
                                        (serverWorld.random.nextDouble() - 0.5) * 1.5
                                );
                                baby.refreshPositionAndAngles(pos.x, pos.y, pos.z, serverWorld.random.nextFloat() * 360.0F, 0.0F);

                                ScaleData scale = ScaleTypes.BASE.getScaleData(baby);
                                scale.setScale(0.5f); // la mitad de tamaño

                                baby.setHealth(8.0F); // vida reducida
                                serverWorld.spawnEntity(baby);
                            }
                        }
                    }
                } else if (Main.getInstance().serverManager().getUpdatesManager().getUpdatesData().getCurrentUpdates().contains(Updates.HOSTILE_SALMONS)) {
                    if (entity instanceof SalmonEntity && !ModEntities.ANGRY_SALMON.equals(entity.getType())) {
                        AngrySalmonEntity.spawnHorde(serverWorld, entity, 6);
                    }
                }
            }
        }
    }

}
