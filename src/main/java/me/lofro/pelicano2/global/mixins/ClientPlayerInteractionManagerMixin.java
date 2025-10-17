package me.lofro.pelicano2.global.mixins;

import me.lofro.pelicano2.global.item.ModItems;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow private GameMode gameMode;

    @Redirect(
            method = "interactItem",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/GameMode;SPECTATOR:Lnet/minecraft/world/GameMode;"
            )
    )
    private GameMode redirectSpectatorCheck(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        // Si es nuestro item, devolvemos un modo que no sea SPECTATOR para que el cliente
        // permita la interacción (p. ej. SURVIVAL). No devuelvas null.
        if (itemStack.isOf(ModItems.CAT_TOGGLE_GAMEMODE) || itemStack.isOf(ModItems.CAPYBARA_TOGGLE_GAMEMODE)) {
            return GameMode.SURVIVAL;
        }

        // comportamiento por defecto: sigue siendo SPECTATOR
        return GameMode.SPECTATOR;
    }
}
