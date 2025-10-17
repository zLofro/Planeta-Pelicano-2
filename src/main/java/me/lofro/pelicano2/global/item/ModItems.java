package me.lofro.pelicano2.global.item;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.item.items.ToggleGamemodeItem;
import me.lofro.pelicano2.global.utils.ModConstants;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item UPGRADE_BALL = registerItem("upgrade_ball", new Item(new Item.Settings().maxCount(1)));
    public static final Item HUMAN_UPGRADE_POTION = registerItem("human_upgrade_potion", new Item(new Item.Settings().maxCount(1)));
    public static final Item CAT_TOGGLE_GAMEMODE = registerItem("cat_toggle_gamemode", new ToggleGamemodeItem(new Item.Settings().maxCount(1)));
    public static final Item CAPYBARA_TOGGLE_GAMEMODE = registerItem("capybara_toggle_gamemode", new ToggleGamemodeItem(new Item.Settings().maxCount(1)));

    // Helper method to register it
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(ModConstants.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Main.getLogger().info("Registering Mod Items for {}", ModConstants.MOD_ID);
    }

}
