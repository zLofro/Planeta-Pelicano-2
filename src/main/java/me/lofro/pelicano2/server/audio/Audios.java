package me.lofro.pelicano2.server.audio;

public enum Audios {
    Audio1("audio-1", 23),
    Audio2("audio-2", 24),
    Audio3("audio-3", 8),
    Audio4("audio-4", 13),
    Audio5("audio-5", 5),
    Audio6("audio-6", 16),
    Audio7("audio-7", 14),
    Audio8("audio-8", 23);

    public final String audioName;
    public final int duration;

    Audios(String audioName, int duration) {
        this.audioName = audioName;
        this.duration = duration;
    }
}
