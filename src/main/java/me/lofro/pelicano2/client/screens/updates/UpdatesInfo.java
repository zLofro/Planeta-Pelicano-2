package me.lofro.pelicano2.client.screens.updates;

//TODO TEST

import net.minecraft.text.Text;

public class UpdatesInfo {
    public final Text title;
    public final boolean seen;

    public UpdatesInfo(String title, boolean seen) {
        this.title = Text.of(title);
        this.seen = seen;
    }

}
