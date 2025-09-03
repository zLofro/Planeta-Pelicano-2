package me.lofro.pelicano2.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
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
                                        .then(CommandManager.argument("showSubtitles", BoolArgumentType.bool())
                                                .then(CommandManager.argument("playAudio", BoolArgumentType.bool())
                                                        .executes(commandContext -> playAudio(AudioArgumentType.getAudio(commandContext, "audio"),
                                                                BoolArgumentType.getBool(commandContext, "showSubtitles"),
                                                                BoolArgumentType.getBool(commandContext, "playAudio")))))))
        );
    }

    public static int playAudio(Audios audio, boolean showSubtitles, boolean playSound) {
        boolean executed = Main.getInstance().serverManager().getAudioManager().playAudio(audio, true, true);

        return executed ? 1 : 0;
    }
}
