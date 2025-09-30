package me.lofro.pelicano2.client;

import me.lofro.pelicano2.client.entities.renderer.EntityRenderRegistries;
import me.lofro.pelicano2.client.screens.missions.MissionAcceptScreen;
import me.lofro.pelicano2.client.screens.updates.UpdatesInfo;
import me.lofro.pelicano2.client.screens.updates.UpdatesLogScreen;
import me.lofro.pelicano2.global.network.PacketIDS;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

import java.util.List;

public class ModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRenderRegistries.register();

        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.OPEN_BITACORA, (client, handler, buf, responseSender) -> {
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

        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.OPEN_MISSION, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                client.setScreen(new MissionAcceptScreen(Text.of("MISION TEST"), Text.of("DESCRIPCION")));
            });
        });
    }

}
