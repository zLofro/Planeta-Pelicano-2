package me.lofro.pelicano2.global.mixins;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.server.updates.Updates;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolemEntity.class)
public class IronGolemEntityMixin extends GolemEntity {

    public IronGolemEntityMixin(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "initGoals")
    public void addAttackGoal(CallbackInfo ci) {
        if (Main.getInstance() != null && Main.getInstance().serverManager() != null && Main.getInstance().serverManager().getUpdatesManager() != null) {
            if (Main.getInstance().serverManager().getUpdatesManager().getUpdatesData().getCurrentUpdates()
                    .contains(Updates.IRON_GOLEM_HOSTILES)) {

                goalSelector.add(0, new MeleeAttackGoal((IronGolemEntity) (Object) this, 1.0D, false));
                targetSelector.add(0, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
            }
        }
    }

}
