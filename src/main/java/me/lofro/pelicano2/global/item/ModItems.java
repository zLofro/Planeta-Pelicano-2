package me.lofro.pelicano2.global.item;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.item.items.UpgradeOrb;
import me.lofro.pelicano2.global.utils.ModConstants;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item UPGRADE_BALL = registerItem("upgrade_ball", new UpgradeOrb(new Item.Settings().maxCount(64)));

    // Helper method to register it
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(ModConstants.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Main.getLogger().info("Registering Mod Items for {}", ModConstants.MOD_ID);
    }

}
