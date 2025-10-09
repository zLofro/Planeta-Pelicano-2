package me.lofro.pelicano2.global.mixins;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.server.skills.data.Skills;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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

}
