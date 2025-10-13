package me.lofro.pelicano2.server.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.server.audio.Audios;
import me.lofro.pelicano2.server.commands.arguments.AudioArgumentType;
import me.lofro.pelicano2.server.commands.arguments.MissionArgumentType;
import me.lofro.pelicano2.server.commands.arguments.SkillsArgumentType;
import me.lofro.pelicano2.server.global.utils.identity.IdentityUtils;
import me.lofro.pelicano2.server.missions.MissionManager;
import me.lofro.pelicano2.server.missions.missions.Missions;
import me.lofro.pelicano2.server.skills.data.Skills;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collection;

public class PelicanoCommand {

    // TODO HACER COMANDO DE GET SKILL LEVEL, FORZAR INICIO DE MISIÓN, ETC.

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("pelicano").requires((source) -> source.hasPermissionLevel(4));

        literalArgumentBuilder
                .then(CommandManager.literal("audio")
                        .then(CommandManager.argument("audio", AudioArgumentType.audio())
                                .then(CommandManager.argument("showSubtitles", BoolArgumentType.bool())
                                        .then(CommandManager.argument("playAudio", BoolArgumentType.bool())
                                                .executes(commandContext -> playAudio(
                                                        AudioArgumentType.getAudio(commandContext, "audio"),
                                                        BoolArgumentType.getBool(commandContext, "showSubtitles"),
                                                        BoolArgumentType.getBool(commandContext, "playAudio")
                                                ))))));
        literalArgumentBuilder
                .then(CommandManager.literal("morph")
                        .then(CommandManager.argument("entity",
                                        RegistryEntryArgumentType.registryEntry(commandRegistryAccess, RegistryKeys.ENTITY_TYPE))
                                .suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                                .then(CommandManager.argument("players", EntityArgumentType.players())
                                        .executes(commandContext -> {
                                            EntityType<?> entity =
                                                    RegistryEntryArgumentType.getEntityType(commandContext, "entity").value();
                                            Collection<ServerPlayerEntity> players =
                                                    EntityArgumentType.getPlayers(commandContext, "players");
                                            return morph(entity, players);
                                        }))));

        literalArgumentBuilder
                .then(CommandManager.literal("mission")
                        .then(CommandManager.literal("spawnNPC")
                                .then(CommandManager.argument("mission", MissionArgumentType.mission())
                                        .then(CommandManager.argument("location", Vec3ArgumentType.vec3())
                                                .executes(commandContext -> spawnNPC(MissionArgumentType.getMission(commandContext, "mission"),
                                                        commandContext.getSource().getWorld(),
                                                        Vec3ArgumentType.getVec3(commandContext, "location"))))))
                        .then(CommandManager.literal("forceAssign")
                                .then(CommandManager.argument("mission", MissionArgumentType.mission())
                                        .then(CommandManager.argument("players", EntityArgumentType.players())
                                                .executes(commandContext -> executeForceAssign(commandContext,
                                                        MissionArgumentType.getMission(commandContext, "mission"),
                                                        EntityArgumentType.getPlayers(commandContext, "players"))))))
                        .then(CommandManager.literal("complete")
                                .then(CommandManager.argument("mission", MissionArgumentType.mission())
                                        .then(CommandManager.argument("players", EntityArgumentType.players())
                                                .executes(commandContext -> executeCompleteMission(commandContext,
                                                        MissionArgumentType.getMission(commandContext, "mission"),
                                                        EntityArgumentType.getPlayers(commandContext, "players"))))))
                        .then(CommandManager.literal("unComplete")
                                .then(CommandManager.argument("mission", MissionArgumentType.mission())
                                        .then(CommandManager.argument("players", EntityArgumentType.players())
                                                .executes(commandContext -> executeUnCompleteMission(commandContext,
                                                        MissionArgumentType.getMission(commandContext, "mission"),
                                                        EntityArgumentType.getPlayers(commandContext, "players"))))))
                        .then(CommandManager.literal("unAssign")
                                .then(CommandManager.argument("mission", MissionArgumentType.mission())
                                        .then(CommandManager.argument("players", EntityArgumentType.players())
                                                .executes(commandContext -> executeUnAssignMission(commandContext,
                                                        MissionArgumentType.getMission(commandContext, "mission"),
                                                        EntityArgumentType.getPlayers(commandContext, "players")))))));

        literalArgumentBuilder.then(CommandManager.literal("skill")
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("players", EntityArgumentType.players())
                                .then(CommandManager.argument("skill", SkillsArgumentType.skill())
                                        .then(CommandManager.argument("level", IntegerArgumentType.integer())
                                                .executes(commandContext -> executeSetSkillLevel(commandContext,
                                                        EntityArgumentType.getPlayers(commandContext, "players"),
                                                        SkillsArgumentType.getSkill(commandContext, "skill"), IntegerArgumentType.getInteger(commandContext, "level")))))))
                .then(CommandManager.literal("get")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .then(CommandManager.argument("skill", SkillsArgumentType.skill())
                                        .executes(commandContext -> executeGetSkillLevel(commandContext,
                                                EntityArgumentType.getPlayer(commandContext, "player"), SkillsArgumentType.getSkill(commandContext, "skill")))))));

        dispatcher.register(literalArgumentBuilder);
    }

    public static int playAudio(Audios audio, boolean showSubtitles, boolean playSound) {
        boolean executed = Main.getInstance().serverManager().getAudioManager().playAudio(audio, showSubtitles, playSound);
        return executed ? 1 : 0;
    }

    public static int morph(EntityType<?> entity, Collection<ServerPlayerEntity> players) {
        String identityId = EntityType.getId(entity).toString();
        players.forEach(player -> IdentityUtils.equipIdentity(player, identityId, true));
        return players.size();
    }

    public static int spawnNPC(Missions mission, World world, Vec3d location) {
        Main.getInstance().serverManager().getMissionManager().spawnNPC(mission, world, location);

        return 1;
    }

    public static int executeSetSkillLevel(CommandContext<ServerCommandSource> commandContext, Collection<ServerPlayerEntity> players, Skills skill, int level) {
        players.forEach(player -> {
            Main.getInstance().serverManager().getSkillTreeManager().updateSkillLevel(player.getUuid(), skill, level);
            commandContext.getSource().sendMessage(Text.of("El nivel de " + skill.name() + " del jugador " + player.getName().getString() + " ha sido actualizado a " + level));
        });

        return Command.SINGLE_SUCCESS;
    }

    public static int executeGetSkillLevel(CommandContext<ServerCommandSource> commandContext, ServerPlayerEntity player, Skills skill) {
        int level = Main.getInstance().serverManager().getSkillTreeManager().getSkillLevel(player.getUuid(), skill);

        commandContext.getSource().sendMessage(Text.of("El nivel de " + skill.name() + " del jugador " + player.getName().getString() + " es " + level));

        return Command.SINGLE_SUCCESS;
    }

    public static int executeForceAssign(CommandContext<ServerCommandSource> commandContext, Missions mission, Collection<ServerPlayerEntity> players) {
        MissionManager missionManager = Main.getInstance().serverManager().getMissionManager();
        players.forEach(player -> {
            commandContext.getSource().sendMessage(Text.of("Has asignado la mision " + mission.getTitle().getString() + " al jugador " + player.getName().getString()));
            missionManager.assign(mission, player.getUuid());
        });

        return Command.SINGLE_SUCCESS;
    }

    public static int executeCompleteMission(CommandContext<ServerCommandSource> commandContext, Missions mission, Collection<ServerPlayerEntity> players) {
        MissionManager missionManager = Main.getInstance().serverManager().getMissionManager();
        players.forEach(player -> {
            commandContext.getSource().sendMessage(Text.of("Has completado la mision " + mission.getTitle().getString() + " al jugador " + player.getName().getString()));
            missionManager.complete(mission, player.getUuid());
        });

        return Command.SINGLE_SUCCESS;
    }

    public static int executeUnCompleteMission(CommandContext<ServerCommandSource> commandContext, Missions mission, Collection<ServerPlayerEntity> players) {
        MissionManager missionManager = Main.getInstance().serverManager().getMissionManager();

        players.forEach(player -> {
            missionManager.removeCompleted(mission, player.getUuid());
            commandContext.getSource().sendMessage(Text.of("Has eliminado de completadas la mision " + mission.getTitle().getString() + " al jugador " + player.getName().getString()));
        });

        return Command.SINGLE_SUCCESS;
    }

    public static int executeUnAssignMission(CommandContext<ServerCommandSource> commandContext, Missions mission, Collection<ServerPlayerEntity> players) {
        MissionManager missionManager = Main.getInstance().serverManager().getMissionManager();

        players.forEach(player -> {
            missionManager.removeActive(mission, player.getUuid());
            commandContext.getSource().sendMessage(Text.of("Has des asignado la mision " + mission.getTitle().getString() + " al jugador " + player.getName().getString()));
        });

        return Command.SINGLE_SUCCESS;
    }

}
