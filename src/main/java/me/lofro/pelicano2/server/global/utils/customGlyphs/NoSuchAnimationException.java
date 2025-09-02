package me.lofro.pelicano2.server.global.utils.customGlyphs;

public class NoSuchAnimationException extends Exception {
    public NoSuchAnimationException(String animation) {
        super("The animation \"" + animation + "\" was never loaded");
    }
}
