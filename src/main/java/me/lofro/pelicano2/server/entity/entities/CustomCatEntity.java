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

public class CustomCatEntity extends MobEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public CustomCatEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createCustomCatAttributes() {
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
        if (shouldNotRender()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("nada"));
            return PlayState.CONTINUE;
        }

        double velocitySq = this.getVelocity().horizontalLengthSquared();

        boolean isMoving = velocitySq > 0.001; // ajusta el umbral si la animación "tiembla"

        if (this.isInSneakingPose() && !this.handSwinging &&
                !this.shouldExecuteSittingAnimation() &&
                !this.shouldExecuteAppearAnimation() &&
                !this.shouldExecuteDisappearAnimation()) {

            if (isMoving) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("crouch_move"));
            } else {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("crouch_idle"));
            }
            return PlayState.CONTINUE;
        } else if (isMoving && !this.isInSneakingPose() &&
                !this.handSwinging &&
                !this.shouldExecuteSittingAnimation() &&
                !this.shouldExecuteAppearAnimation() &&
                !this.shouldExecuteDisappearAnimation()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("running"));
            return PlayState.CONTINUE;
        } else if (!this.handSwinging &&
                !this.shouldExecuteSittingAnimation() &&
                !this.shouldExecuteAppearAnimation() &&
                !this.shouldExecuteDisappearAnimation()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends GeoEntity> PlayState attackPredicate(AnimationState<E> state) {
        if (this.handSwinging && !shouldNotRender()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("attack"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends GeoEntity> PlayState customAnimationPredicate(AnimationState<E> state) {
        if (this.shouldExecuteSittingAnimation() && !shouldNotRender()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("sit2"));
            return PlayState.CONTINUE;
        } else if (this.shouldExecuteAppearAnimation() && !shouldNotRender()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("appear"));
            return PlayState.CONTINUE;
        } else if (this.shouldExecuteDisappearAnimation() && !shouldNotRender()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("desaparecer"));
            return PlayState.CONTINUE;
        } else if (this.shouldPlayDance1() && !shouldNotRender()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("baile_1"));
            return PlayState.CONTINUE;
        } else if (this.shouldPlayDance2() && !shouldNotRender()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("idle2"));
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

    private boolean shouldExecuteDisappearAnimation() {
        return this.getOffHandStack().isOf(Items.STICK);
    }

    private boolean shouldExecuteAppearAnimation() {
        return this.getOffHandStack().isOf(Items.NAUTILUS_SHELL);
    }

    private boolean shouldNotRender() {
        return this.getOffHandStack().isOf(Items.FLINT);
    }

    private boolean shouldPlayDance1() {
        return this.getOffHandStack().isOf(Items.BOOK);
    }

    private boolean shouldPlayDance2() {
        return this.getOffHandStack().isOf(Items.COMPASS);
    }
}
