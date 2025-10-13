package me.lofro.pelicano2.global.item.items;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.utils.ModConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class UpgradeOrb extends Item {

    public UpgradeOrb(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack oldStack = user.getStackInHand(hand);

        if (!world.isClient) {
            Main.getInstance().serverManager().getSkillTreeManager().addUpgradePoints(user.getUuid(), 1);
            user.sendMessage(Text.of("Has canjeado un punto de mejora."), false);

            // TODO SONIDO

            // Crea un nuevo stack con la cantidad decrecida y así garantizas la sincronización
            ItemStack newStack = oldStack.copy();
            newStack.decrement(1);

            // Si la nueva pila queda vacía, asigna ItemStack.EMPTY
            if (newStack.isEmpty()) {
                user.setStackInHand(hand, ItemStack.EMPTY);
            } else {
                user.setStackInHand(hand, newStack);
            }

            if (user instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.currentScreenHandler.sendContentUpdates();
            }
        }

        // Devuelve el stack actual de la mano (cliente/servidor)
        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }

}
