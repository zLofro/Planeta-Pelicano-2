package me.lofro.pelicano2.global.mixins;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.server.updates.Updates;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(MerchantEntity.class)
public class MerchantEntityMixin {

    @Unique
    private static final Random RANDOM = new Random();

    @Inject(method = "trade", at = @At("HEAD"), cancellable = true)
    private void onTrade(TradeOffer offer, CallbackInfo ci) {
        if (offer == null || Main.getInstance() == null) return;

        if (Main.getInstance().serverManager().getUpdatesManager().getUpdatesData().getCurrentUpdates().contains(Updates.ALDEANOS_COLOMBIANOS)) {
            // 30% de probabilidad de "estafa"
            if (RANDOM.nextDouble() < 0.3) {
                // Marca la oferta como usada (consume los ítems que el jugador entregó)
                offer.use();

                // Intentamos obtener el jugador que comerciaba con el merchant
                PlayerEntity player = ((MerchantEntity) (Object) this).getCustomer();
                if (player != null) {
                    player.sendMessage(Text.literal("¡El aldeano es colombiano y te estafo!").formatted(Formatting.RED));
                }

                // Cancelamos la ejecución normal del trade para que no se entregue el resultado.
                ci.cancel();
            }
        }
    }

}
