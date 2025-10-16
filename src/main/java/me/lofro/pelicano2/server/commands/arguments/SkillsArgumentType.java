package me.lofro.pelicano2.server.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import me.lofro.pelicano2.server.skills.data.Skills;
import me.lofro.pelicano2.server.updates.Updates;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SkillsArgumentType implements ArgumentType<Skills> {
    private static final Collection<String> EXAMPLES = Stream.of(Skills.values()).map(Skills::name).collect(Collectors.toList());

    public static SkillsArgumentType skill() {
        return new SkillsArgumentType();
    }

    @Override
    public Skills parse(StringReader reader) throws CommandSyntaxException {
        int argBeginning = reader.getCursor();
        if (!reader.canRead()) {
            reader.skip();
        }

        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }

        String substring = reader.getString().substring(argBeginning, reader.getCursor());
        try {
            return Skills.valueOf(substring);
        } catch (Exception ex) {
            throw new SimpleCommandExceptionType(ex::getMessage).createWithContext(reader);
        }
    }

    public static Skills getSkill(CommandContext<ServerCommandSource> context, String name) {
        return context.getArgument(name, Skills.class);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static final SuggestionProvider<ServerCommandSource> UPDATES_SUGGESTIONS =
            (context, builder) -> {
                for (Skills skill : Skills.values()) {
                    builder.suggest(skill.name());
                }
                return builder.buildFuture();
            };

}
