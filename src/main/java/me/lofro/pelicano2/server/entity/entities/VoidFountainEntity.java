package me.lofro.pelicano2.server.entity.entities;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.utils.ModConstants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class VoidFountainEntity extends PathAwareEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final TrackedData<Boolean> CONSUMING = DataTracker.registerData(VoidFountainEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    // Estado de animación
    private int consumeTicks = 0;

    public VoidFountainEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);

        setPersistent();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CONSUMING, false);
    }

    public boolean isConsuming() {
        return this.dataTracker.get(CONSUMING);
    }

    public void setConsuming(boolean value) {
        this.dataTracker.set(CONSUMING, value);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void initGoals() {

    }

    @Override
    public void move(MovementType movementType, Vec3d movement) {

    }

    @Override
    public void travel(Vec3d movementInput) {

    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }

    private <E extends GeoEntity> PlayState animationPredicate(AnimationState<E> state) {
        if (isConsuming()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("use"));
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void tick() {
        super.tick();

        if (isConsuming()) {
            consumeTicks++;
            if (consumeTicks > 40) {
                setConsuming(false);
                consumeTicks = 0;
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void onItemOffered(ServerPlayerEntity player) {
        if (!getWorld().isClient) {
            setConsuming(true);
            this.consumeTicks = 0;
            player.sendMessage(Text.literal("Has canjeado con éxito un punto de habilidad.").formatted(Formatting.DARK_AQUA), false);

            player.getWorld().playSound(null, player.getBlockPos(), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "claim_point")), SoundCategory.MASTER, 1.0f, 1.0f);

            Main.getInstance().serverManager().getSkillTreeManager().addUpgradePoints(player.getUuid(), 1);
        }
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        // evita el despawn por completo
        return false;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.getBoolean("PersistenceRequired")) {
            this.setPersistent();
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("PersistenceRequired", true);
    }

}
