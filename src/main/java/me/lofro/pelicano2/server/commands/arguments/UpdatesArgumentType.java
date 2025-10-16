package me.lofro.pelicano2.server.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import me.lofro.pelicano2.server.updates.Updates;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UpdatesArgumentType implements ArgumentType<Updates> {
    private static final Collection<String> EXAMPLES = Stream.of(Updates.values()).map(Updates::name).collect(Collectors.toList());

    public static UpdatesArgumentType updates() {
        return new UpdatesArgumentType();
    }

    @Override
    public Updates parse(StringReader reader) throws CommandSyntaxException {
        int argBeginning = reader.getCursor();
        if (!reader.canRead()) {
            reader.skip();
        }

        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }

        String substring = reader.getString().substring(argBeginning, reader.getCursor());
        try {
            return Updates.valueOf(substring);
        } catch (Exception ex) {
            throw new SimpleCommandExceptionType(ex::getMessage).createWithContext(reader);
        }
    }

    public static Updates getUpdate(CommandContext<ServerCommandSource> context, String name) {
        return context.getArgument(name, Updates.class);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static final SuggestionProvider<ServerCommandSource> UPDATES_SUGGESTIONS =
            (context, builder) -> {
                for (Updates update : Updates.values()) {
                    builder.suggest(update.name());
                }
                return builder.buildFuture();
            };

}
