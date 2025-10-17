package me.lofro.pelicano2.server.entity.entities;

import me.lofro.pelicano2.global.item.ModItems;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
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

public class CauldronEntity extends PathAwareEntity implements GeoEntity {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final TrackedData<Boolean> COOKING = DataTracker.registerData(CauldronEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> COOK_READY = DataTracker.registerData(CauldronEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    final int COOKING_TIME_TICKS = 1200 * 20;

    int cookingTicks = 0;

    public CauldronEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);

        setPersistent();
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            if (isCooking()) {
                if (cookingTicks >= 0) {
                    this.setCustomName(Text.literal(formatTime(cookingTicks / 20)).formatted(Formatting.AQUA));
                    cookingTicks--;
                } else {
                    setCooking(false);
                    setCookReady(true);
                }
            }
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
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(COOKING, false);
        this.dataTracker.startTracking(COOK_READY, false);
    }

    public boolean isCooking() {
        return this.dataTracker.get(COOKING);
    }

    public void setCooking(boolean value) {
        this.dataTracker.set(COOKING, value);
    }

    public boolean isCookReady() {
        return this.dataTracker.get(COOK_READY);
    }

    public void setCookReady(boolean value) {
        if (value) {
            this.setCustomName(Text.literal("¡Objeto listo!").formatted(Formatting.BOLD).formatted(Formatting.YELLOW));
            this.setCustomNameVisible(true);
        } else {
            this.setCustomName(null);
            this.setCustomNameVisible(false);
        }
        this.dataTracker.set(COOK_READY, value);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.getWorld().isClient) {
            ItemStack stack = player.getStackInHand(hand);
            if (this.isCookReady()) {
                player.giveItemStack(ModItems.UPGRADE_BALL.getDefaultStack());
                this.setCookReady(false);
                this.cookingTicks = 0;

                player.sendMessage(Text.literal("Has recibido un orbe de mejora.").formatted(Formatting.AQUA));

                player.getWorld().playSound(null, player.getBlockPos(), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "taking_object_cauldron")), SoundCategory.MASTER, 1.0f, 1.0f);

                MinecraftServer server = player.getServer();

                if (server != null) {
                    ServerCommandSource source =  server.getCommandSource().withLevel(4);
                    server.getCommandManager().executeWithPrefix(source, "scoreboard players set $isobject particle_anim1");
                }
            } else {
                if (stack.isOf(ModItems.HUMAN_UPGRADE_POTION)) {
                    if (!this.isCooking()) {
                        this.setCooking(true);
                        this.cookingTicks = COOKING_TIME_TICKS;
                        this.setCustomName(Text.literal(formatTime(cookingTicks / 20)).formatted(Formatting.AQUA));
                        this.setCustomNameVisible(true);

                        if (!player.isCreative()) {
                            stack.decrement(1);
                            if (stack.isEmpty()) {
                                player.getInventory().setStack(player.getInventory().selectedSlot, ItemStack.EMPTY);
                            }
                        }

                        player.getWorld().playSound(null, player.getBlockPos(), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "cooking_cauldron")), SoundCategory.MASTER, 1.0f, 1.0f);
                    } else {
                        player.sendMessage(Text.literal("Ya se está haciendo un objeto.").formatted(Formatting.RED), false);
                    }
                } else {
                    player.sendMessage(Text.literal("No tienes una poción de mejora humana en la mano.").formatted(Formatting.RED), false);
                }
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.CONSUME;
    }

    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
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
        if (isCooking()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("work"));
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.cookingTicks = nbt.getInt("cookingTicks");
        if (cookingTicks > 0) {
            setCooking(true);
        }

        if (cookingTicks <= -1) {
            setCookReady(true);
        }
        if (nbt.getBoolean("PersistenceRequired")) {
            this.setPersistent();
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("cookingTicks", cookingTicks);
        nbt.putBoolean("PersistenceRequired", true);
    }

}
