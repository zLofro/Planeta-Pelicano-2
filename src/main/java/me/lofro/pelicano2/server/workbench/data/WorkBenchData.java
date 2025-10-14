package me.lofro.pelicano2.server.workbench.data;

import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class WorkBenchData {

    private final Map<UUID, Pair<Item, Integer>> crafts;

    public WorkBenchData(Map<UUID, Pair<Item, Integer>> crafts) {
        this.crafts = crafts;
    }

    public WorkBenchData() {
        this.crafts = new HashMap<>();
    }

}
