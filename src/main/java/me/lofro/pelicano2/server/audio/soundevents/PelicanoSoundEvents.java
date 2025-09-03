package me.lofro.pelicano2.server.audio.soundevents;

import me.lofro.pelicano2.global.utils.ModConstants;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class PelicanoSoundEvents {

    public static SoundEvent AUDIO1;

    public static void register() {
        AUDIO1 = register("audio-1");
    }

    private static SoundEvent register(String id) {
        var identifier = new Identifier(ModConstants.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

}
