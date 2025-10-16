package me.lofro.pelicano2.global.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class OtherModUtils {

    public static boolean otherModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static Item getOtherModItem(String modID, String itemName) {
        if (otherModLoaded(modID)) {
            Identifier id = new Identifier(modID, itemName);
            return Registries.ITEM.get(id);
        } else {
            return null;
        }
    }

}
