package me.lofro.pelicano2.server;

import lombok.Getter;
import me.lofro.pelicano2.server.audio.AudioManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import me.lofro.pelicano2.Main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ModServer {

    private final Main main;

    private MinecraftServer minecraftServer;

    private @Getter ScheduledExecutorService serverExecutorService;

    private @Getter AudioManager audioManager;

    public ModServer(final Main main) {
        this.main = main;

        ServerLifecycleEvents.SERVER_STARTED.register((server -> {
            this.minecraftServer = server;

            this.serverExecutorService = Executors.newSingleThreadScheduledExecutor();

            this.audioManager = new AudioManager(this);
        }));
        ServerLifecycleEvents.SERVER_STOPPED.register((server -> {
            audioManager.stopAllAudios();

            audioManager = null;

            serverExecutorService.shutdownNow();
            serverExecutorService = null;
        }));
    }

    /**
     * @return the current Minecraft Server.
     */
    public MinecraftServer minecraftServer() {
        return minecraftServer;
    }

}
