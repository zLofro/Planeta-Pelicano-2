package me.lofro.pelicano2.server.global.utils.customGlyphs;

public class NoAliasException extends Exception {
    NoAliasException(String alias) {
        super("\"" + alias + "\" is not an alias of any declared glyph");
    }
}
