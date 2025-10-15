package me.lofro.pelicano2.global.mixins;

import me.lofro.pelicano2.global.item.ModItems;
import me.lofro.pelicano2.server.entity.entities.VoidFountainEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("TAIL"))
    private void onDropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
        World world = player.getWorld();
        ItemEntity dropped = cir.getReturnValue();

        if (world.isClient() || dropped == null) return;

        if (stack.isOf(ModItems.UPGRADE_BALL)) {
            double radius = 2.0D;
            List<VoidFountainEntity> fountains = world.getEntitiesByClass(
                    VoidFountainEntity.class,
                    new Box(dropped.getX() - radius, dropped.getY() - radius, dropped.getZ() - radius,
                            dropped.getX() + radius, dropped.getY() + radius, dropped.getZ() + radius),
                    entity -> true
            );

            if (!fountains.isEmpty()) {
                VoidFountainEntity fountain = fountains.getFirst();

                dropped.discard();
                fountain.onItemOffered(player);
            }
        }
    }

}
