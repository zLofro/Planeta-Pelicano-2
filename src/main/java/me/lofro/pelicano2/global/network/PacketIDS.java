package me.lofro.pelicano2.global.network;

import me.lofro.pelicano2.global.utils.ModConstants;
import net.minecraft.util.Identifier;

public class PacketIDS {
    public static final Identifier OPEN_BITACORA_SCREEN_S2C = new Identifier(ModConstants.MOD_ID, "open_bitacora");
    public static final Identifier OPEN_MISSION_SCREEN_S2C = new Identifier(ModConstants.MOD_ID, "open_mission");
    public static final Identifier OPEN_MISSION_VIEW_SCREEN_S2C = new Identifier(ModConstants.MOD_ID, "open_mission_view");
    public static final Identifier OPEN_SKILLS_SCREEN_S2C = new Identifier(ModConstants.MOD_ID, "open_skills");
    public static final Identifier OPEN_WORKBENCH_SCREEN_S2C = new Identifier(ModConstants.MOD_ID, "open_workbench");

    public static final Identifier CLICK_SELECTION_ICON_C2S = new Identifier(ModConstants.MOD_ID, "click_selection_icon");

    public static final Identifier CLICK_UPDATE_SKILL_C2S = new Identifier(ModConstants.MOD_ID, "click_update_skill");

    public static final Identifier ACCEPT_MISSION_C2S = new Identifier(ModConstants.MOD_ID, "accept_mission_c2s");

    public static final Identifier WORKBENCH_BUTTON_CLICK_C2S = new Identifier(ModConstants.MOD_ID, "workbench_button_click");

    public static final Identifier UPDATE_CLIENT_MISSION_DATA_S2C = new Identifier(ModConstants.MOD_ID, "update_client_mission_data");
    public static final Identifier ADD_TO_CLIENT_MISSION_DATA_S2C = new Identifier(ModConstants.MOD_ID, "add_to_client_mission_data");
}
