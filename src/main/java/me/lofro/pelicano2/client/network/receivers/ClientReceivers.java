package me.lofro.pelicano2.client.network.receivers;

import me.lofro.pelicano2.client.KeyBinds;
import me.lofro.pelicano2.client.ModClient;
import me.lofro.pelicano2.client.screens.missions.MissionAcceptScreen;
import me.lofro.pelicano2.client.screens.missions.MissionViewScreen;
import me.lofro.pelicano2.client.screens.selection.SelectionScreen;
import me.lofro.pelicano2.client.screens.updates.UpdatesInfo;
import me.lofro.pelicano2.client.screens.updates.UpdatesLogScreen;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.server.missions.missions.Missions;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;

import java.util.*;

public class ClientReceivers {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.OPEN_BITACORA_SCREEN_S2C, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                if (client.player != null) {
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
                    ), client.player));
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

        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.ADD_TO_CLIENT_MISSION_DATA, (client, handler, buf, responseSender) -> {
            Missions mission = buf.readEnumConstant(Missions.class);

            ModClient.getClientMissionData().getAssignedMissions().add(mission);
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
