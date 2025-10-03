package me.lofro.pelicano2.client.network.receivers;

import me.lofro.pelicano2.client.screens.missions.MissionAcceptScreen;
import me.lofro.pelicano2.client.screens.missions.MissionViewScreen;
import me.lofro.pelicano2.client.screens.updates.UpdatesInfo;
import me.lofro.pelicano2.client.screens.updates.UpdatesLogScreen;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.server.missions.missions.Missions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    }

}
