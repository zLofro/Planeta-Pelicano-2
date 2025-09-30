package me.lofro.pelicano2.server.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.server.audio.Audios;
import me.lofro.pelicano2.server.commands.arguments.AudioArgumentType;
import me.lofro.pelicano2.server.global.utils.identity.IdentityUtils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;

public class PelicanoCommand {
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
                                ServerPlayNetworking.send(player, PacketIDS.OPEN_BITACORA, buf);
                            }

                            return Command.SINGLE_SUCCESS;
                        }));

        literalArgumentBuilder
                .then(CommandManager.literal("mission")
                        .executes(commandContext -> {
                            ServerPlayerEntity player = commandContext.getSource().getPlayer();

                            if (player != null) {
                                PacketByteBuf buf = PacketByteBufs.create();
                                ServerPlayNetworking.send(player, PacketIDS.OPEN_MISSION, buf);
                            }

                            return Command.SINGLE_SUCCESS;
                        }));

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
}
