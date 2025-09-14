package me.lofro.pelicano2.server.entity.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class GuideEntity extends MobEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public GuideEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createGuideAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movementController", 0, this::movementPredicate));
        controllers.add(new AnimationController<>(this, "attackController", 0, this::attackPredicate));
        controllers.add(new AnimationController<>(this, "customAnimationController", 0, this::customAnimationPredicate));
    }

    private <E extends GeoEntity> PlayState movementPredicate(AnimationState<E> state) {
        if (this.isInSneakingPose() && !this.handSwinging && !this.shouldExecuteSittingAnimation()) {
            if (state.isMoving()) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("crouch_move"));
            } else {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("crouch_idle"));
            }
            return PlayState.CONTINUE;
        } else if (state.isMoving() && !this.isInSneakingPose() && !this.handSwinging && !this.shouldExecuteSittingAnimation()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("running"));
            return PlayState.CONTINUE;
        } else if (!this.handSwinging && !this.shouldExecuteSittingAnimation()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends GeoEntity> PlayState attackPredicate(AnimationState<E> state) {
        if (this.handSwinging) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("attack"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends GeoEntity> PlayState customAnimationPredicate(AnimationState<E> state) {
        if (this.shouldExecuteSittingAnimation()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("sit"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private boolean shouldExecuteSittingAnimation() {
        return this.getOffHandStack().isOf(Items.PAPER);
    }
}
