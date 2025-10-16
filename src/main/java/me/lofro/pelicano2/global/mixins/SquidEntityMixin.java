package me.lofro.pelicano2.global.mixins;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.ia.PoisonAttackGoal;
import me.lofro.pelicano2.server.updates.Updates;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SquidEntity.class)
public class SquidEntityMixin extends MobEntity {

    protected SquidEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    // Añadir atributo de daño base
    @Inject(at = @At("RETURN"), method = "createSquidAttributes")
    private static void addAttackDamageAttribute(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D);
    }

    @Inject(at = @At("HEAD"), method = "initGoals")
    public void addAttackGoal(CallbackInfo ci) {
        if (Main.getInstance() != null) {
            if (Main.getInstance().serverManager().getUpdatesManager().getUpdatesData().getCurrentUpdates()
                    .contains(Updates.CALAMARES_HOSTILES)) {

                goalSelector.add(0, new PoisonAttackGoal((SquidEntity) (Object) this, 1.0D, false));
                targetSelector.add(0, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
            }
        }
    }

}
