package me.lofro.pelicano2.global.mixins;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.server.updates.Updates;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

    @Shadow private float exhaustion;

    @Inject(method = "update", at = @At("HEAD"))
    private void onUpdate(PlayerEntity player, CallbackInfo ci) {
        if (Main.getInstance() != null && Main.getInstance().serverManager() != null && Main.getInstance().serverManager().getUpdatesManager() != null) {
            if (Main.getInstance().serverManager().getUpdatesManager().getUpdatesData().getCurrentUpdates().contains(Updates.HAMBRE_BAJO_AGUA)) {
                // Solo aplica a jugadores vivos y en mundos donde el hambre esté habilitado
                if (player instanceof ServerPlayerEntity serverPlayer &&
                        player.isAlive() &&
                        player.getWorld().getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)) {

                    // Si el jugador está bajo el agua completamente
                    if (player.isSubmergedInWater()) {
                        // Aumenta la fatiga (exhaustion) un 40%
                        this.exhaustion *= 1.4F;
                    }
                }
            }
        }
    }

}
