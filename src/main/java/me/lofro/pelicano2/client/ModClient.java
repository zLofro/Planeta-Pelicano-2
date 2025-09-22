package me.lofro.pelicano2.client;

import me.lofro.pelicano2.client.entities.renderer.EntityRenderRegistries;
import me.lofro.pelicano2.client.screens.Mission;
import me.lofro.pelicano2.client.screens.MissionLogScreen;
import me.lofro.pelicano2.global.network.PacketIDS;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.List;

public class ModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRenderRegistries.register();

        ClientPlayNetworking.registerGlobalReceiver(PacketIDS.OPEN_BITACORA, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                client.setScreen(new MissionLogScreen(List.of(
                        new Mission("Recolecta 10 manzanas", false),
                        new Mission("Habla con el aldeano", true),
                        new Mission("Explora el bosque", false),
                        new Mission("Encuentra el tesoro", true),
                        new Mission("Testeo jaja", false),
                        new Mission("me estoy volviendo loco", false),
                        new Mission("Comete un pimiento", false),
                        new Mission("?????", true),
                        new Mission("ultima mision", true)
                )));
            });
        });
    }

}
