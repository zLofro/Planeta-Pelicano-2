package me.lofro.pelicano2.global.mixins;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.server.updates.Updates;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin {

    @Inject(method = "burnsInDaylight", at = @At("HEAD"), cancellable = true)
    private void noBurnUnderSun(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!Main.getInstance().serverManager().getUpdatesManager().getUpdatesData().getCurrentUpdates().contains(Updates.ZOMBIES_INMUNES_SOL)); // Nunca se queman
    }

    @Inject(method = "createZombieAttributes", at = @At("RETURN"), cancellable = true)
    private static void boostDamage(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        if (Main.getInstance() != null) {
            if (Main.getInstance().serverManager().getUpdatesManager().getUpdatesData().getCurrentUpdates().contains(Updates.ZOMBIES_INMUNES_SOL)) {
                DefaultAttributeContainer.Builder builder = cir.getReturnValue();
                double baseDamage = builder.build().getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                builder.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, baseDamage * 1.15);
                cir.setReturnValue(builder);
            }
        }
    }

}
