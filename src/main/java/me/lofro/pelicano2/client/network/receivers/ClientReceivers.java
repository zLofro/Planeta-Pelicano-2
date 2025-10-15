package me.lofro.pelicano2.client.network.receivers;

import me.lofro.pelicano2.client.KeyBinds;
import me.lofro.pelicano2.client.ModClient;
import me.lofro.pelicano2.client.screens.missions.MissionAcceptScreen;
import me.lofro.pelicano2.client.screens.missions.MissionViewScreen;
import me.lofro.pelicano2.client.screens.selection.SelectionScreen;
import me.lofro.pelicano2.client.screens.skills.SkillsScreen;
import me.lofro.pelicano2.client.screens.updates.UpdatesLogScreen;
import me.lofro.pelicano2.client.screens.workbench.WorkBenchScreen;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.server.missions.missions.Missions;
import me.lofro.pelicano2.server.skills.data.Skills;
import me.lofro.pelicano2.server.updates.Updates;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;

import java.util.*;

public class ClientReceivers {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.OPEN_BITACORA_SCREEN_S2C, (client, handler, buf, responseSender) -> {
            List<Updates> updates = buf.readCollection(ArrayList::new, b -> b.readEnumConstant(Updates.class));
            client.execute(() -> {
                if (client.player != null) {
                    client.setScreen(new UpdatesLogScreen(updates, client.player));
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.OPEN_MISSION_SCREEN_S2C, (client, handler, buf, responseSender) -> {
            Missions mission = buf.readEnumConstant(Missions.class);

            client.execute(() -> {
                if (mission != null) {
                    client.setScreen(new MissionAcceptScreen(mission));
                } else {
                    throw new RuntimeException("Error cargando la mision del packet.");
                }});
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.OPEN_MISSION_VIEW_SCREEN_S2C, (client, handler, buf, responseSender) -> {
            Map<Missions, Boolean> playerMissions = buf.readMap(HashMap::new, b -> b.readEnumConstant(Missions.class), PacketByteBuf::readBoolean);

            client.execute(() -> {
                if (client.player != null) {
                    client.setScreen(new MissionViewScreen(playerMissions, client.player));
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.UPDATE_CLIENT_MISSION_DATA_S2C, (client, handler, buf, responseSender) -> {
            List<Missions> assignedMissions = buf.readCollection(ArrayList::new, buf2 -> buf2.readEnumConstant(Missions.class));

            ModClient.getClientMissionData().setAssignedMissions(assignedMissions);
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.ADD_TO_CLIENT_MISSION_DATA_S2C, (client, handler, buf, responseSender) -> {
            Missions mission = buf.readEnumConstant(Missions.class);

            ModClient.getClientMissionData().getAssignedMissions().add(mission);
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.OPEN_SKILLS_SCREEN_S2C, (client, handler, buf, responseSender) -> {
            Map<Skills, Integer> skills = buf.readMap(HashMap::new, b -> b.readEnumConstant(Skills.class), PacketByteBuf::readInt);
            int upgradePoints = buf.readInt();

            client.execute(() -> {
                if (client.player != null) {
                   client.setScreen(new SkillsScreen(skills, upgradePoints, client.player));
               }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.OPEN_WORKBENCH_SCREEN_S2C, (client, handler, buf, responseSender) -> {
            int secondsRemaining = buf.readInt();
            boolean activeCraft = buf.readBoolean();
            int itemId = buf.readInt();

            client.execute(() -> {
                if (client.player != null) {
                    if (itemId != -1) {
                        client.setScreen(new WorkBenchScreen(client.player, secondsRemaining, activeCraft, Item.byRawId(itemId)));
                    } else {
                        client.setScreen(new WorkBenchScreen(client.player, secondsRemaining, activeCraft));
                    }
                }
            });
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KeyBinds.OPEN_CUSTOM_SCREEN.wasPressed()) {
                if (client.player != null) {
                    client.execute(() -> client.setScreen(new SelectionScreen(client.player)));
                }
            }
        });
    }

}
