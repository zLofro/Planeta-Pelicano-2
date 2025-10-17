package me.lofro.pelicano2.global.item.items;

import me.lofro.pelicano2.global.item.ModItems;
import me.lofro.pelicano2.global.utils.ModConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.Timer;
import java.util.TimerTask;

public class ToggleGamemodeItem extends Item {

    public ToggleGamemodeItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) return super.use(world, user, hand);
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) user;
        boolean toggleToSpectator = serverPlayerEntity.interactionManager.isCreative();

        serverPlayerEntity.setStackInHand(Hand.OFF_HAND, toggleToSpectator ? new ItemStack(Items.STICK) : new ItemStack(Items.NAUTILUS_SHELL));

        ItemStack stackInHand = serverPlayerEntity.getStackInHand(hand);

        if (stackInHand.isOf(ModItems.CAPYBARA_TOGGLE_GAMEMODE)) {
            serverPlayerEntity.getWorld().playSound(null, serverPlayerEntity.getBlockPos(), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "capybara_appear")), SoundCategory.MASTER, 1.0f, 1.0f);
        } else {
            serverPlayerEntity.getWorld().playSound(null, serverPlayerEntity.getBlockPos(), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "cat_appear")), SoundCategory.MASTER, 1.0f, 1.0f);
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (serverPlayerEntity.getServer() != null) {
                    serverPlayerEntity.getServer().execute(() -> {
                        serverPlayerEntity.changeGameMode(toggleToSpectator ? GameMode.SPECTATOR : GameMode.CREATIVE);
                        serverPlayerEntity.setStackInHand(Hand.OFF_HAND, toggleToSpectator ? new ItemStack(Items.FLINT) : new ItemStack(Items.AIR));
                    });
                }
            }
        }, 2000);

        return super.use(world, user, hand);
    }

}
