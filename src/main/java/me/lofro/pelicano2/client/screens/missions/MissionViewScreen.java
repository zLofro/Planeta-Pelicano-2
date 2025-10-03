package me.lofro.pelicano2.client.screens.missions;

import me.lofro.pelicano2.Main;
import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.missions.missions.Missions;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissionViewScreen extends Screen {

    // --- Textures ---
    private static final Identifier BACKGROUND = new Identifier(ModConstants.MOD_ID, "textures/gui/missions/mission_view_background.png");
    private static final Identifier SLIDE = new Identifier(ModConstants.MOD_ID, "textures/gui/bitacora/slide.png");
    private static final Identifier SLIDE_BACKGROUND = new Identifier(ModConstants.MOD_ID, "textures/gui/bitacora/slide_background.png");
    private static final Identifier MISSION_COMPLETED = new Identifier(ModConstants.MOD_ID, "textures/gui/bitacora/update_seen.png");
    private static final Identifier MISSION_PENDING = new Identifier(ModConstants.MOD_ID, "textures/gui/bitacora/update_not_seen.png");

    private final Map<Missions, Boolean> playerMissions;
    private int scrollOffset = 0;

    // Layout
    private final int guiWidth = 288;
    private final int guiHeight = 287;
    private final int rowHeight = 40;
    private final int scrollHeight = 194;

    public MissionViewScreen(Map<Missions, Boolean> playerMissions) {
        super(Text.literal("Missions"));
        this.playerMissions = new HashMap<>(playerMissions);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        int x = (this.width - guiWidth) / 2;
        int y = (this.height - guiHeight) / 2;

        // --- Background ---
        context.drawTexture(BACKGROUND, x, y, 0, 0, guiWidth, guiHeight, guiWidth, guiHeight);

        // --- Scroll area background ---
        int scrollX = x + 245;
        int scrollY = y + 69;
        int scrollWidth = 18;
        context.drawTexture(SLIDE_BACKGROUND, scrollX, scrollY, 0, 0, scrollWidth, scrollHeight, scrollWidth, scrollHeight);

        // --- Missions ---
        int missionWidth = 223;
        int missionHeight = 37;

        int firstVisible = scrollOffset / rowHeight;
        int yOffset = -(scrollOffset % rowHeight);
        int visibleCount = Math.floorDiv(scrollHeight, rowHeight) + 1;

        List<Missions> playerMissionList = this.playerMissions.keySet().stream().toList();

        for (int i = 0; i < visibleCount && (firstVisible + i) < playerMissions.size(); i++) {
            Missions mission = playerMissionList.get(firstVisible + i);
            Identifier texture = playerMissions.get(mission) ? MISSION_COMPLETED : MISSION_PENDING;

            int missionX = x + 20;
            int rowY = scrollY + yOffset + (i * rowHeight);

            if (rowY + missionHeight > scrollY && rowY < scrollY + scrollHeight) {
                context.drawTexture(texture, missionX, rowY, 0, 0, missionWidth, missionHeight, missionWidth, missionHeight);

                int textX = missionX + 20;
                int textY = rowY + 10;
                int maxTextWidth = missionWidth - 40;

                List<OrderedText> lines = textRenderer.wrapLines(mission.getSummary(), maxTextWidth);

                for (int j = 0; j < Math.min(2, lines.size()); j++) {
                    OrderedText line = lines.get(j);

                    context.getMatrices().push();
                    float scale = 0.9f;
                    context.getMatrices().scale(scale, scale, 1.0f);

                    int drawX = (int) (textX / scale);
                    int drawY = (int) ((textY + j * (textRenderer.fontHeight + 2)) / scale);

                    context.drawText(textRenderer, line, drawX, drawY, 0x444444, false);
                    context.getMatrices().pop();
                }
            }
        }

        // --- Scroll bar (siempre visible) ---
        int contentHeight = playerMissionList.size() * rowHeight;
        int maxScroll = Math.max(0, contentHeight - scrollHeight);

        int scrollbarWidth = 10;
        int scrollbarHeight = 28;
        int scrollbarRange = (scrollHeight - scrollbarHeight) - 8;

        // si no hay scroll, lo dejamos arriba fijo
        int scrollbarY = scrollY + 4 + (maxScroll == 0 ? 0 : (scrollOffset * scrollbarRange / maxScroll));
        int scrollbarX = scrollX + 4;

        context.drawTexture(SLIDE, scrollbarX, scrollbarY, 0, 0, scrollbarWidth, scrollbarHeight, scrollbarWidth, scrollbarHeight);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int contentHeight = playerMissions.keySet().size() * rowHeight;
        int maxScroll = Math.max(0, contentHeight - scrollHeight);

        int scrollStep = 8;
        scrollOffset = Math.max(0, Math.min(scrollOffset - (int) amount * scrollStep, maxScroll));

        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}