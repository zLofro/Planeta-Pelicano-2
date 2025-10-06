package me.lofro.pelicano2.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {

    public static KeyBinding OPEN_CUSTOM_SCREEN;

    public static void register() {
        OPEN_CUSTOM_SCREEN = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pelicano2.open_selection_screen", // ID interna
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_GRAVE_ACCENT,
                "category.pelicano2.keys"
        ));
    }

}
