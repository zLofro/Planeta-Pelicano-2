package me.lofro.pelicano2.client.network.receivers;

import me.lofro.pelicano2.client.ModClient;
import me.lofro.pelicano2.client.screens.missions.MissionAcceptScreen;
import me.lofro.pelicano2.client.screens.missions.MissionViewScreen;
import me.lofro.pelicano2.client.screens.updates.UpdatesInfo;
import me.lofro.pelicano2.client.screens.updates.UpdatesLogScreen;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.server.entity.entities.GuideMissionsEntity;
import me.lofro.pelicano2.server.entity.entities.NicteMissionsEntity;
import me.lofro.pelicano2.server.missions.missions.Missions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;

import java.util.*;

public class ClientReceivers {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.OPEN_BITACORA_SCREEN_S2C, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                client.setScreen(new UpdatesLogScreen(List.of(
                        new UpdatesInfo("Recolecta 10 manzanas", false),
                        new UpdatesInfo("Habla con el aldeano", true),
                        new UpdatesInfo("Explora el bosque", false),
                        new UpdatesInfo("Encuentra el tesoro", true),
                        new UpdatesInfo("Testeo jaja", false),
                        new UpdatesInfo("me estoy volviendo loco", false),
                        new UpdatesInfo("Comete un pimiento", false),
                        new UpdatesInfo("?????", true),
                        new UpdatesInfo("ultima mision", true)
                )));
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
                if (!playerMissions.isEmpty()) {
                    client.setScreen(new MissionViewScreen(playerMissions));
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.UPDATE_CLIENT_MISSION_DATA_S2C, (client, handler, buf, responseSender) -> {
            List<Missions> assignedMissions = buf.readCollection(ArrayList::new, buf2 -> buf2.readEnumConstant(Missions.class));

            ModClient.getClientMissionData().setAssignedMissions(assignedMissions);

            ClientWorld clientWorld = client.world;

            /*if (clientWorld != null) {
                for (Entity entity : clientWorld.getEntities()) {
                    if (entity instanceof GuideMissionsEntity || entity instanceof NicteMissionsEntity) {
                        clientWorld.removeEntity(entity.getId(), Entity.RemovalReason.DISCARDED);
                        clientWorld.addEntity(entity.getId(), entity);
                    }
                }
            }*/
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.ADD_TO_CLIENT_MISSION_DATA, (client, handler, buf, responseSender) -> {
            Missions mission = buf.readEnumConstant(Missions.class);

            ModClient.getClientMissionData().getAssignedMissions().add(mission);

            ClientWorld clientWorld = client.world;

            /*if (clientWorld != null) {
                for (Entity entity : clientWorld.getEntities()) {
                    if (entity instanceof GuideMissionsEntity || entity instanceof NicteMissionsEntity) {
                        clientWorld.removeEntity(entity.getId(), Entity.RemovalReason.DISCARDED);
                        clientWorld.addEntity(entity.getId(), entity);
                    }
                }
            }*/
        });

    }

}
