package me.lofro.pelicano2.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.server.audio.Audios;
import me.lofro.pelicano2.server.commands.arguments.AudioArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class PelicanoCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("pelicano")
                        .then(CommandManager.literal("audio")
                                .then(CommandManager.argument("audio", AudioArgumentType.audio())
                                        .executes(commandContext -> playAudio(AudioArgumentType.getAudio(commandContext, "audio")))))
        );
    }

    public static int playAudio(Audios audio) {
        boolean executed = Main.getInstance().serverManager().getAudioManager().playAudio(audio, true, false);

        return executed ? 1 : 0;
    }
}
