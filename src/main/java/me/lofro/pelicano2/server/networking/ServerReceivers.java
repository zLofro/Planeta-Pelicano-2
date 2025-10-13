package me.lofro.pelicano2.server.networking;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.client.screens.selection.SelectionScreen;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.server.missions.MissionManager;
import me.lofro.pelicano2.server.missions.missions.Missions;
import me.lofro.pelicano2.server.skills.SkillTreeManager;
import me.lofro.pelicano2.server.skills.data.Skills;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

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

                serverPlayerEntity.sendMessage(Text.of("Misión " + mission.getTitle().getString() + " aceptada."));
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(PacketIDS.CLICK_UPDATE_SKILL_C2S, (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            Skills skill = packetByteBuf.readEnumConstant(Skills.class);

            SkillTreeManager skillTreeManager = Main.getInstance().serverManager().getSkillTreeManager();

            skillTreeManager.addSkillLevel(serverPlayerEntity.getUuid(), skill, 1);
            skillTreeManager.removeUpgradePoints(serverPlayerEntity.getUuid(), 1);
        });

        ServerPlayNetworking.registerGlobalReceiver(PacketIDS.CLICK_SELECTION_ICON_C2S, (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            SelectionScreen.Buttons button = packetByteBuf.readEnumConstant(SelectionScreen.Buttons.class);

            switch (button) {
                case UPDATES -> {
                    PacketByteBuf buf = PacketByteBufs.create();
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
                    //TODO
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
    }

}
