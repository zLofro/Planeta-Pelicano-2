package me.lofro.pelicano2.client;

import lombok.Getter;
import me.lofro.pelicano2.client.data.ClientMissionData;
import me.lofro.pelicano2.client.entities.renderer.EntityRenderRegistries;
import me.lofro.pelicano2.client.network.receivers.ClientReceivers;
import net.fabricmc.api.ClientModInitializer;

public class ModClient implements ClientModInitializer {

    private static @Getter ClientMissionData clientMissionData;

    @Override
    public void onInitializeClient() {
        EntityRenderRegistries.register();

        clientMissionData = new ClientMissionData();

        ClientReceivers.register();
    }

}
