package me.lofro.pelicano2.client;

import me.lofro.pelicano2.client.entities.renderer.EntityRenderRegistries;
import net.fabricmc.api.ClientModInitializer;

public class ModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRenderRegistries.register();
    }

}
