package me.lofro.pelicano2.global.mixins;

import me.lofro.pelicano2.global.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerInteractionManagerMixin {

    @Inject(method = "interactItem", at = @At("HEAD"), cancellable = true)
    private void interactItem(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stackInHand = player.getStackInHand(hand);

        if ((stackInHand.isOf(ModItems.CAT_TOGGLE_GAMEMODE) || stackInHand.isOf(ModItems.CAPYBARA_TOGGLE_GAMEMODE)) && player.interactionManager.getGameMode() == GameMode.SPECTATOR) {
            // Llamamos al use del item en el server
            TypedActionResult<ItemStack> typedActionResult = stackInHand.use(player.getWorld(), player, hand);
            ActionResult result = typedActionResult.getResult();

            // Si el resultado indica que el item inicia uso / fue aceptado, arrancamos el uso
            if (result == ActionResult.SUCCESS || result == ActionResult.CONSUME || result == ActionResult.CONSUME_PARTIAL) {
                // Inicia el uso en el servidor (necesario para items que cargan/continúan)
                player.setCurrentHand(hand);
            }

            // Devolvemos lo que el item devolvió (o PASS si no fue aceptado)
            cir.setReturnValue(result);
        }
    }

}
