package me.lofro.pelicano2.server.updates.data;

import lombok.Getter;
import me.lofro.pelicano2.server.updates.Updates;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UpdatesData {
    private final List<Updates> currentUpdates;

    public UpdatesData(List<Updates> currentUpdates) {
        this.currentUpdates = currentUpdates;
    }

    public UpdatesData() {
        this.currentUpdates = new ArrayList<>();
    }
}
