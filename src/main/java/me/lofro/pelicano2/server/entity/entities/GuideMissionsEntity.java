package me.lofro.pelicano2.server.entity.entities;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.server.missions.MissionManager;
import me.lofro.pelicano2.server.missions.missions.Missions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.UUID;

public class GuideMissionsEntity extends PathAwareEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final TrackedData<String> MISSION = DataTracker.registerData(GuideMissionsEntity.class, TrackedDataHandlerRegistry.STRING);

    public GuideMissionsEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return PathAwareEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.getWorld().isClient) {
            MissionManager missionManager = Main.getInstance().serverManager().getMissionManager();
            UUID playerUUID = player.getUuid();

            Missions mission = getMission();

            if (!missionManager.completed(playerUUID, mission) && !missionManager.active(playerUUID, mission)) {
                ServerPlayerEntity serverPlayerEntity = Main.getInstance().serverManager().minecraftServer().getPlayerManager().getPlayer(playerUUID);
                Main.getInstance().serverManager().getMissionManager().openAssignScreen(serverPlayerEntity, mission);
            } else {
                player.sendMessage(Text.of("¡Ya hablaste conmigo!"));
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MISSION, Missions.values()[0].name());
    }

    public void setMission(Missions mission) {
        this.dataTracker.set(MISSION, mission.name());
    }

    public Missions getMission() {
        return Missions.valueOf(this.dataTracker.get(MISSION));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movementController", 0, this::movementPredicate));
    }

    private <E extends GeoEntity> PlayState movementPredicate(AnimationState<E> state) {
        if (this.isInSneakingPose() && !this.handSwinging) {
            if (state.isMoving()) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("crouch_move"));
            } else {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("crouch_idle"));
            }
            return PlayState.CONTINUE;
        } else if (state.isMoving() && !this.isInSneakingPose() && !this.handSwinging) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("walk"));
            return PlayState.CONTINUE;
        } else if (!this.handSwinging) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Mission", this.getMission().name());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Mission")) {
            this.setMission(Missions.valueOf(nbt.getString("Mission")));
        }
    }

}
