package me.lofro.pelicano2.server.workbench.data;

import lombok.Getter;
import net.minecraft.item.Item;

public class ActiveCraft {
    @Getter
    private final Item item;
    private final int totalDuration; // en segundos
    private final long startTime; // en milisegundos

    public ActiveCraft(Item item, int totalDuration, long startTime) {
        this.item = item;
        this.totalDuration = totalDuration;
        this.startTime = startTime;
    }

    public int getRemainingSeconds(long now) {
        long elapsed = (now - startTime) / 1000;
        return Math.max(0, totalDuration - (int) elapsed);
    }
}
