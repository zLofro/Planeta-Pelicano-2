package me.lofro.pelicano2.server.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import me.lofro.pelicano2.server.missions.missions.Missions;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MissionArgumentType implements ArgumentType<Missions> {
    private static final Collection<String> EXAMPLES = Stream.of(Missions.values()).map(Missions::name).collect(Collectors.toList());

    public static MissionArgumentType mission() {
        return new MissionArgumentType();
    }

    @Override
    public Missions parse(StringReader reader) throws CommandSyntaxException {
        int argBeginning = reader.getCursor();
        if (!reader.canRead()) {
            reader.skip();
        }

        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }

        String substring = reader.getString().substring(argBeginning, reader.getCursor());
        try {
            return Missions.valueOf(substring);
        } catch (Exception ex) {
            throw new SimpleCommandExceptionType(ex::getMessage).createWithContext(reader);
        }
    }

    public static Missions getMission(CommandContext<ServerCommandSource> context, String name) {
        return context.getArgument(name, Missions.class);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
