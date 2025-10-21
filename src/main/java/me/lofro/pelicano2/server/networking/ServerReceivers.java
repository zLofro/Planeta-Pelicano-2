package me.lofro.pelicano2.server.networking;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.client.screens.selection.SelectionScreen;
import me.lofro.pelicano2.global.accessors.ServerPlayerEntityAccessor;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.server.missions.MissionManager;
import me.lofro.pelicano2.server.missions.missions.Missions;
import me.lofro.pelicano2.server.skills.SkillTreeManager;
import me.lofro.pelicano2.server.skills.data.Skills;
import me.lofro.pelicano2.server.updates.Updates;
import me.lofro.pelicano2.server.workbench.WorkBenchManager;
import me.lofro.pelicano2.server.workbench.WorkBenchRecipe;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ServerReceivers {

    public static void registerServerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(PacketIDS.ACCEPT_MISSION_C2S, (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            MissionManager missionManager = Main.getInstance().serverManager().getMissionManager();

            Missions mission = packetByteBuf.readEnumConstant(Missions.class);

            if (mission != null) {
                missionManager.assign(mission, serverPlayerEntity.getUuid());

                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeEnumConstant(mission);
                ServerPlayNetworking.send(serverPlayerEntity, PacketIDS.ADD_TO_CLIENT_MISSION_DATA_S2C, buf);

                serverPlayerEntity.sendMessage(Text.literal("¡Has aceptado con éxito la mision " + mission.getTitle().getString() + "!").formatted(Formatting.GREEN));
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(PacketIDS.CLICK_UPDATE_SKILL_C2S, (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            Skills skill = packetByteBuf.readEnumConstant(Skills.class);

            SkillTreeManager skillTreeManager = Main.getInstance().serverManager().getSkillTreeManager();

            skillTreeManager.addSkillLevel(serverPlayerEntity.getUuid(), skill, 1);
            skillTreeManager.removeUpgradePoints(serverPlayerEntity.getUuid(), 1);

            serverPlayerEntity.sendMessage(Text.literal("¡Felicidades! Has mejorado tu habilidad de " + skill.name() + ".").formatted(Formatting.AQUA));
            ServerCommandSource source = minecraftServer.getCommandSource().withLevel(4);
            minecraftServer.getCommandManager().executeWithPrefix(source, "/scoreboard players set $islevelup particle_anim 1");
        });

        ServerPlayNetworking.registerGlobalReceiver(PacketIDS.CLICK_SELECTION_ICON_C2S, (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            SelectionScreen.Buttons button = packetByteBuf.readEnumConstant(SelectionScreen.Buttons.class);

            switch (button) {
                case UPDATES -> {
                    PacketByteBuf buf = PacketByteBufs.create();

                    List<Updates> updates = Main.getInstance().serverManager().getUpdatesManager().getUpdatesData().getCurrentUpdates();
                    buf.writeCollection(updates, PacketByteBuf::writeEnumConstant);

                    ServerPlayNetworking.send(serverPlayerEntity, PacketIDS.OPEN_BITACORA_SCREEN_S2C, buf);
                }
                case MISSIONS -> {
                    MissionManager missionManager = Main.getInstance().serverManager().getMissionManager();

                    missionManager.openViewScreen(serverPlayerEntity);
                }
                case SKILLS -> {
                    SkillTreeManager skillTreeManager = Main.getInstance().serverManager().getSkillTreeManager();

                    PacketByteBuf buf = PacketByteBufs.create();

                    var skills = skillTreeManager.getSkills(serverPlayerEntity.getUuid());

                    buf.writeMap(Objects.requireNonNullElseGet(skills, HashMap::new), PacketByteBuf::writeEnumConstant, PacketByteBuf::writeInt);

                    buf.writeInt(skillTreeManager.getUpgradePoints(serverPlayerEntity.getUuid()));

                    ServerPlayNetworking.send(serverPlayerEntity, PacketIDS.OPEN_SKILLS_SCREEN_S2C, buf);
                }
                case BOSSES -> {
                    ServerPlayNetworking.send(serverPlayerEntity, PacketIDS.OPEN_BOSSES_SCREEN_S2C, PacketByteBufs.create());
                }
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;

            if (player != null) {
                MissionManager missionManager = Main.getInstance().serverManager().getMissionManager();

                List<Missions> assignedMissions = new ArrayList<>();

                var completedMissions = missionManager.getMissionData().getCompletedMissions().get(player.getUuid());
                var activeMissions = missionManager.getMissionData().getActiveMissions().get(player.getUuid());

                if (completedMissions != null) {
                    assignedMissions.addAll(completedMissions);
                }

                if (activeMissions != null) {
                    assignedMissions.addAll(activeMissions);
                }

                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeCollection(assignedMissions, PacketByteBuf::writeEnumConstant);

                ServerPlayNetworking.send(player, PacketIDS.UPDATE_CLIENT_MISSION_DATA_S2C, buf);

                server.execute(() -> {
                    SkillTreeManager skillTreeManager = Main.getInstance().serverManager().getSkillTreeManager();

                    skillTreeManager.onUpdateSkillLevel(player, Skills.SPEED,
                            skillTreeManager.getSkillLevel(player.getUuid(), Skills.SPEED)
                    );
                });
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(PacketIDS.WORKBENCH_BUTTON_CLICK_C2S, (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            boolean noTime = packetByteBuf.readBoolean();

            boolean existsSelectedRecipe = packetByteBuf.readBoolean();

            if (existsSelectedRecipe) {
                WorkBenchRecipe recipe = packetByteBuf.readEnumConstant(WorkBenchRecipe.class);

                WorkBenchManager workBenchManager = Main.getInstance().serverManager().getWorkBenchManager();

                if (noTime) {
                    if (workBenchManager.isReady(serverPlayerEntity.getUuid())) {
                        var item = workBenchManager.getWorkBenchData().getCrafts().get(serverPlayerEntity.getUuid()).getLeft();
                        serverPlayerEntity.sendMessage(Text.literal("Has fabricado exitosamente un ").append(item.getName()).formatted(Formatting.LIGHT_PURPLE));
                        workBenchManager.claimResult(serverPlayerEntity);
                    } else {
                        if (workBenchManager.startCraft(serverPlayerEntity, recipe)) {
                            serverPlayerEntity.sendMessage(Text.literal("Ha comenzado la fabricación " + workBenchManager.getActiveCrafts().get(serverPlayerEntity.getUuid()).getItem().getName().getString()).formatted(Formatting.YELLOW));
                        } else {
                            serverPlayerEntity.sendMessage(Text.literal("No tienes los items necesarios para fabricar ese objeto.").formatted(Formatting.RED));
                        }
                    }
                } else {
                    serverPlayerEntity.sendMessage(Text.literal("¡Ya hay un crafteo en curso!").formatted(Formatting.RED));
                }
            } else {
                serverPlayerEntity.sendMessage(Text.literal("No has seleccionado ningún crafteo.").formatted(Formatting.RED));
            }
        });

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (entity instanceof ServerPlayerEntity player && Main.getInstance() != null) {
                ServerPlayerEntityAccessor accessor = (ServerPlayerEntityAccessor) player;

                String id = killedEntity.getType().toString();

                MissionManager missionManager = Main.getInstance().serverManager().getMissionManager();

                if (id.contains("zombie") && missionManager.active(player.getUuid(), Missions.DEFENDER_SYLMORIA)) {
                    accessor.planetaPelicano2$incrementZombieKills();
                    player.sendMessage(Text.literal("Has matado a un " + killedEntity.getName().getString() + ". " + accessor.planetaPelicano2$getZombiesKilled() + "/20.").formatted(Formatting.YELLOW));
                }
                else if (id.contains("skeleton") && missionManager.active(player.getUuid(), Missions.DEFENDER_SYLMORIA)) {
                    accessor.planetaPelicano2$incrementSkeletonKills();
                    player.sendMessage(Text.literal("Has matado a un " + killedEntity.getName().getString() + ". " + accessor.planetaPelicano2$getSkeletonsKilled() + "/20.").formatted(Formatting.YELLOW));
                }
                else if (id.contains("spider") && missionManager.active(player.getUuid(), Missions.DEFENDER_SYLMORIA)) {
                    accessor.planetaPelicano2$incrementSpiderKills();
                    player.sendMessage(Text.literal("Has matado a un " + killedEntity.getName().getString() + ". " + accessor.planetaPelicano2$getSpidersKilled() + "/10.").formatted(Formatting.YELLOW));
                }
                else if (id.contains("creeper") && missionManager.active(player.getUuid(), Missions.DEFENDER_SYLMORIA)) {
                    accessor.planetaPelicano2$incrementCreeperKills();
                    player.sendMessage(Text.literal("Has matado a un " + killedEntity.getName().getString() + ". " + accessor.planetaPelicano2$getCreepersKilled() + "/10.").formatted(Formatting.YELLOW));
                }
                else if (id.contains("vulture") && missionManager.active(player.getUuid(), Missions.ALAS_DE_SOMBRA)) {
                    accessor.planetaPelicano2$incrementVultureKills();
                    player.sendMessage(Text.literal("Has matado a un " + killedEntity.getName().getString() + ". " + accessor.planetaPelicano2$getVultresKilled() + "/20.").formatted(Formatting.YELLOW));
                }
            }
        });
    }

}
