package me.lofro.pelicano2.server.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.server.audio.Audios;
import me.lofro.pelicano2.server.commands.arguments.AudioArgumentType;
import me.lofro.pelicano2.server.commands.arguments.MissionArgumentType;
import me.lofro.pelicano2.server.commands.arguments.SkillsArgumentType;
import me.lofro.pelicano2.server.global.utils.identity.IdentityUtils;
import me.lofro.pelicano2.server.missions.MissionManager;
import me.lofro.pelicano2.server.missions.missions.Missions;
import me.lofro.pelicano2.server.skills.data.Skills;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
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
                .then(CommandManager.literal("bitacora")
                        .executes(commandContext -> {
                            ServerPlayerEntity player = commandContext.getSource().getPlayer();

                            if (player != null) {
                                PacketByteBuf buf = PacketByteBufs.create();
                                ServerPlayNetworking.send(player, PacketIDS.OPEN_BITACORA_SCREEN_S2C, buf);
                            }

                            return Command.SINGLE_SUCCESS;
                        }));

        literalArgumentBuilder
                .then(CommandManager.literal("mission")
                        .then(CommandManager.literal("spawnNPC")
                                .then(CommandManager.argument("mission", MissionArgumentType.mission())
                                        .then(CommandManager.argument("location", Vec3ArgumentType.vec3())
                                                .executes(commandContext -> spawnNPC(MissionArgumentType.getMission(commandContext, "mission"),
                                                        commandContext.getSource().getWorld(),
                                                        Vec3ArgumentType.getVec3(commandContext, "location"))))))
                        .then(CommandManager.literal("missions")
                                .executes(commandContext -> {
                                    ServerPlayerEntity player = commandContext.getSource().getPlayer();

                                    if (player != null) {
                                        MissionManager missionManager = Main.getInstance().serverManager().getMissionManager();

                                        missionManager.openViewScreen(player);
                                    }

                                    return Command.SINGLE_SUCCESS;
                                })));

        literalArgumentBuilder.then(CommandManager.literal("skill")
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("players", EntityArgumentType.players())
                                .then(CommandManager.argument("skill", SkillsArgumentType.skill())
                                        .then(CommandManager.argument("level", IntegerArgumentType.integer())
                                                .executes(commandContext -> executeSetSkillLevel(EntityArgumentType.getPlayers(commandContext, "players"),
                                                        SkillsArgumentType.getSkill(commandContext, "skill"), IntegerArgumentType.getInteger(commandContext, "level"))))))));

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

    public static int executeSetSkillLevel(Collection<ServerPlayerEntity> players, Skills skill, int level) {
        players.forEach(player -> Main.getInstance().serverManager().getSkillTreeManager().updateSkillLevel(player.getUuid(), skill, level));

        return Command.SINGLE_SUCCESS;
    }

}
