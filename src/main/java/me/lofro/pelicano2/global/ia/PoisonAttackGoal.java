package me.lofro.pelicano2.global.ia;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;

public class PoisonAttackGoal extends MeleeAttackGoal {

    private final SquidEntity squid;

    public PoisonAttackGoal(SquidEntity squid, double speed, boolean pauseWhenIdle) {
        super(squid, speed, pauseWhenIdle);
        this.squid = squid;
    }

    @Override
    protected void attack(LivingEntity target, double squaredDistance) {
        super.attack(target, squaredDistance);

        if (this.squid.getWorld().isClient) return;

        if (target instanceof PlayerEntity player && squaredDistance < getSquaredMaxAttackDistance(player)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0)); // 5s de veneno
        }
    }
}