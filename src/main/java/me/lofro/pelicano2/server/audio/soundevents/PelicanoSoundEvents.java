package me.lofro.pelicano2.server.audio.soundevents;

import me.lofro.pelicano2.global.utils.ModConstants;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class PelicanoSoundEvents {

    public static SoundEvent AUDIO1;
    public static SoundEvent AUDIO2;
    public static SoundEvent AUDIO3;
    public static SoundEvent AUDIO4;
    public static SoundEvent AUDIO5;
    public static SoundEvent AUDIO6;
    public static SoundEvent AUDIO7;
    public static SoundEvent AUDIO8;

    public static void register() {
        AUDIO1 = register("audio-1");
        AUDIO2 = register("audio-2");
        AUDIO3 = register("audio-3");
        AUDIO4 = register("audio-4");
        AUDIO5 = register("audio-5");
        AUDIO6 = register("audio-6");
        AUDIO7 = register("audio-7");
        AUDIO8 = register("audio-8");
    }

    private static SoundEvent register(String id) {
        var identifier = new Identifier(ModConstants.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

}
