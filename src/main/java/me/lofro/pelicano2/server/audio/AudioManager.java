package me.lofro.pelicano2.server.audio;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.ModServer;
import me.lofro.pelicano2.server.global.utils.timedTitle.InvalidTitleTimings;
import me.lofro.pelicano2.server.global.utils.timedTitle.TimedActionBar;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AudioManager {

    private final MinecraftServer server;

    private final ModServer serverManager;

    public boolean playingAudio;

    public AudioManager(ModServer serverManager) {
        this.serverManager = serverManager;
        this.server = serverManager.minecraftServer();
        playingAudio = false;
    }

    public boolean playAudio(Audios audio, boolean showSubtitles, boolean playAudio) {
        if (playingAudio) {return false;}

        String audioName = audio.audioName;

        List<Pair<String, Integer>> lines = new ArrayList<>();
        if (showSubtitles) {
            try (InputStream input = Main.class.getResourceAsStream("/assets/pelicano2/subtitles/" + audioName + ".txt")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split("\\|");
                    if (parts.length == 2) {
                        String text = parts[0].trim();
                        String timeString = parts[1].replace(";", "").trim();
                        int time = Integer.parseInt(timeString);

                        lines.add(new Pair<>(text, time));
                    }
                }
            } catch (IOException e) {
                Main.getLogger().error("No se ha encontrado el archivo de subtitulos {}.txt o ha habido un problema en su lectura", audioName, e);
                return false;
            }
        }

        server.getPlayerManager().getPlayerList().forEach(player -> {
            if (playAudio) {
                player.playSound(SoundEvent.of(new Identifier(ModConstants.MOD_ID, audioName)), SoundCategory.MASTER, 1, 1);
            }

            if (showSubtitles) {
                lines.forEach(lineData -> {
                    try {
                        TimedActionBar.send(player, Text.of(lineData.getLeft()), lineData.getRight() * 1000);
                    } catch (InvalidTitleTimings e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });

        playingAudio = true;

        serverManager.getServerExecutorService().schedule(() -> {
            playingAudio = false;
        }, audio.duration, TimeUnit.SECONDS);

        return true;
    }

    public boolean stopAudio(Audios audio) {
        if (!playingAudio) {return false;}

        server.getPlayerManager().getPlayerList().forEach(player -> {
            stopSound(player, new Identifier(ModConstants.MOD_ID, audio.audioName), SoundCategory.MASTER);
        });

        TimedActionBar.actionBarQueue.clear();

        playingAudio = false;

        return true;
    }

    public void stopAllAudios() {
        if (!playingAudio) {return;}

        server.getPlayerManager().getPlayerList().forEach(AudioManager::stopAllSounds);

        TimedActionBar.actionBarQueue.clear();

        playingAudio = false;
    }

    public static void stopSound(ServerPlayerEntity player, Identifier id, SoundCategory category) {
        StopSoundS2CPacket packet = new StopSoundS2CPacket(id, category);
        player.networkHandler.sendPacket(packet);
    }

    public static void stopAllSounds(ServerPlayerEntity player) {
        StopSoundS2CPacket packet = new StopSoundS2CPacket(null, null);
        player.networkHandler.sendPacket(packet);
    }

}
