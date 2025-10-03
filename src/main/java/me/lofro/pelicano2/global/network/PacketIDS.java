package me.lofro.pelicano2.global.network;

import me.lofro.pelicano2.global.utils.ModConstants;
import net.minecraft.util.Identifier;

public class PacketIDS {
    public static final Identifier OPEN_BITACORA_SCREEN_S2C = new Identifier(ModConstants.MOD_ID, "open_bitacora");
    public static final Identifier OPEN_MISSION_SCREEN_S2C = new Identifier(ModConstants.MOD_ID, "open_mission");
    public static final Identifier OPEN_MISSION_VIEW_SCREEN_S2C = new Identifier(ModConstants.MOD_ID, "open_mission_view");

    public static final Identifier ACCEPT_MISSION_C2S = new Identifier(ModConstants.MOD_ID, "accept_mission_c2s");
}
