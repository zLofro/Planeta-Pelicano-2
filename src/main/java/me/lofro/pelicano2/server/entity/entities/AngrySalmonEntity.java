package me.lofro.pelicano2.server.entity.entities;

import me.lofro.pelicano2.server.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class AngrySalmonEntity extends SalmonEntity {

    public AngrySalmonEntity(EntityType<? extends SalmonEntity> entityType, World world) {
        super(entityType, world);
        setCustomName(Text.literal("Salmon enfadado").formatted(Formatting.RED));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        // Nada alrededor
        this.goalSelector.add(1, new SwimAroundGoal(this, 1.0D, 20));
        // Ataca en melee
        this.goalSelector.add(0, new MeleeAttackGoal(this, 1.2D, true));
        // Persigue jugadores
        this.targetSelector.add(0,  new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return SalmonEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public boolean tryAttack(net.minecraft.entity.Entity target) {
        boolean attacked = super.tryAttack(target);
        if (attacked && target instanceof PlayerEntity player) {
            player.damage(this.getDamageSources().mobAttack(this), 3.0F);
        }
        return attacked;
    }

    public static void spawnHorde(ServerWorld world, net.minecraft.entity.LivingEntity source, int count) {
        for (int i = 0; i < count; i++) {
            AngrySalmonEntity angry = new AngrySalmonEntity(ModEntities.ANGRY_SALMON, world);
            angry.refreshPositionAndAngles(
                    source.getX() + (world.random.nextDouble() - 0.5) * 4,
                    source.getY() + (world.random.nextDouble() - 0.5) * 2,
                    source.getZ() + (world.random.nextDouble() - 0.5) * 4,
                    world.random.nextFloat() * 360F,
                    0.0F
            );
            world.spawnEntity(angry);
        }
    }

}
