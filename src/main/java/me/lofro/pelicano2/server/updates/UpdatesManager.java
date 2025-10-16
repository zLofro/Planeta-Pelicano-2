package me.lofro.pelicano2.server.updates;

import lombok.Getter;
import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.data.api.Restorable;
import me.lofro.pelicano2.global.data.util.JsonConfig;
import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.ModServer;
import me.lofro.pelicano2.server.updates.data.UpdatesData;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

import java.util.Timer;
import java.util.TimerTask;

public class UpdatesManager implements Restorable {

    private final ModServer modServer;

    private @Getter UpdatesData updatesData;

    private boolean animationIsActive = false;

    public UpdatesManager(ModServer modServer) {
        this.modServer = modServer;

        this.restore(modServer.getDataManager().getUpdatesData());
    }

    public void unlock(Updates update) {
        if (!unlocked(update)) {
            updatesData.getCurrentUpdates().add(update);

            MinecraftServer minecraftServer = modServer.minecraftServer();

            startAnimation(update, minecraftServer, 4);

            minecraftServer.getPlayerManager().getPlayerList().forEach(player -> {
                player.getWorld().playSound(null, player.getBlockPos(), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "update_unlocked")), SoundCategory.MASTER, 1.0f, 1.0f);
            });
        }
    }

    public void lock(Updates update) {
        if (unlocked(update)) {
            updatesData.getCurrentUpdates().remove(update);
        }
    }

    public boolean unlocked(Updates update) {
        return updatesData.getCurrentUpdates().contains(update);
    }

    private void startAnimation(Updates update, MinecraftServer minecraftServer, int seconds) {
        if (animationIsActive) return;
        animationIsActive = true;

        var world = minecraftServer.getOverworld();

        final double realTime = 1000.0 / 20;
        final long gameTime = world.getTimeOfDay();
        final int totalFrames = seconds * 20;
        final float gameTimeStep = ((gameTime <= 18_000 ? 18_000f : 42_000f) - gameTime) / totalFrames;

        final float[] nightIncrement = {0};
        final int[] frame = {0};
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                frame[0]++;
                if (frame[0] >= totalFrames) {
                    this.cancel();
                    animationIsActive = false;
                    world.setTimeOfDay(18_000);

                    minecraftServer.getPlayerManager().getPlayerList().forEach(player -> {
                        player.sendMessage(update.getSummary().copy().formatted(Formatting.DARK_PURPLE).styled(style -> style.withBold(true)));
                        player.sendMessage(update.description.copy().formatted(Formatting.LIGHT_PURPLE));
                        player.getWorld().playSound(null, player.getBlockPos(), SoundEvent.of(new Identifier(ModConstants.MOD_ID, "update_message")), SoundCategory.MASTER, 1.0f, 1.0f);
                        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.MASTER, 1.0f, 1.0f);
                    });

                    return;
                }
                nightIncrement[0] += gameTimeStep;
                world.setTimeOfDay((long) (gameTime + nightIncrement[0]));

                world.getPlayers().forEach(p -> p.networkHandler.sendPacket(new WorldTimeUpdateS2CPacket(world.getTime(), world.getTimeOfDay(), world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE))));
            }
        }, 0, (long) realTime);
    }

    @Override
    public void restore(JsonConfig jsonConfig) {
        if (jsonConfig.getJsonObject().entrySet().isEmpty()) {
            this.updatesData = new UpdatesData();
        } else {
            this.updatesData = Main.gson().fromJson(jsonConfig.getJsonObject(), UpdatesData.class);
        }
    }

    @Override
    public void save(JsonConfig jsonConfig) {
        jsonConfig.setJsonObject(Main.gson().toJsonTree(updatesData).getAsJsonObject());
        try {
            jsonConfig.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
