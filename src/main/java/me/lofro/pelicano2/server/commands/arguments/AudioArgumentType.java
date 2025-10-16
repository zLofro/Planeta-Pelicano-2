package me.lofro.pelicano2.server.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import me.lofro.pelicano2.server.audio.Audios;
import me.lofro.pelicano2.server.missions.missions.Missions;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AudioArgumentType implements ArgumentType<Audios> {
    private static final Collection<String> EXAMPLES = Stream.of(Audios.values()).map(Audios::name).collect(Collectors.toList());

    public static AudioArgumentType audio() {
        return new AudioArgumentType();
    }

    @Override
    public Audios parse(StringReader reader) throws CommandSyntaxException {
        int argBeginning = reader.getCursor();
        if (!reader.canRead()) {
            reader.skip();
        }

        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }

        String substring = reader.getString().substring(argBeginning, reader.getCursor());
        try {
            return Audios.valueOf(substring);
        } catch (Exception ex) {
            throw new SimpleCommandExceptionType(ex::getMessage).createWithContext(reader);
        }
    }

    public static Audios getAudio(CommandContext<ServerCommandSource> context, String name) {
        return context.getArgument(name, Audios.class);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static final SuggestionProvider<ServerCommandSource> UPDATES_SUGGESTIONS =
            (context, builder) -> {
                for (Audios audio : Audios.values()) {
                    builder.suggest(audio.name());
                }
                return builder.buildFuture();
            };

}
