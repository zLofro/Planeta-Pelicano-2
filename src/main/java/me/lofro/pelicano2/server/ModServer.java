package me.lofro.pelicano2.server;

import lombok.Getter;
import me.lofro.pelicano2.server.audio.AudioManager;
import me.lofro.pelicano2.server.data.DataManager;
import me.lofro.pelicano2.server.missions.MissionManager;
import me.lofro.pelicano2.server.skills.SkillTreeManager;
import me.lofro.pelicano2.server.updates.UpdatesManager;
import me.lofro.pelicano2.server.workbench.WorkBenchManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import me.lofro.pelicano2.Main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ModServer {

    private final Main main;

    private MinecraftServer minecraftServer;

    private @Getter DataManager dataManager;
    private @Getter AudioManager audioManager;
    private @Getter MissionManager missionManager;
    private @Getter SkillTreeManager skillTreeManager;
    private @Getter WorkBenchManager workBenchManager;
    private @Getter UpdatesManager updatesManager;

    private @Getter ScheduledExecutorService serverExecutorService;

    public ModServer(final Main main) {
        this.main = main;

        ServerLifecycleEvents.SERVER_STARTED.register((server -> {
            this.minecraftServer = server;

            try {
                this.dataManager = new DataManager(this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            this.serverExecutorService = Executors.newSingleThreadScheduledExecutor();

            this.audioManager = new AudioManager(this);

            this.missionManager = new MissionManager(this);

            this.skillTreeManager = new SkillTreeManager(this);

            this.workBenchManager = new WorkBenchManager(this);

            this.updatesManager = new UpdatesManager(this);
        }));
        ServerLifecycleEvents.SERVER_STOPPED.register((server -> {
            try {
                dataManager.save();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

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
