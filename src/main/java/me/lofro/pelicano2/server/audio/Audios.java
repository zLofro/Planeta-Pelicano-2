package me.lofro.pelicano2.server.audio;

public enum Audios {
    Audio1("audio-1", 23);

    public final String audioName;
    public final int duration;

    Audios(String audioName, int duration) {
        this.audioName = audioName;
        this.duration = duration;
    }
}
