package me.lofro.pelicano2.client.screens;

import me.lofro.pelicano2.global.utils.ModConstants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class MissionLogScreen extends Screen {

    // --- Textures ---
    private static final Identifier BACKGROUND = new Identifier(ModConstants.MOD_ID, "textures/gui/bitacora/background.png");
    private static final Identifier SLIDE = new Identifier(ModConstants.MOD_ID, "textures/gui/bitacora/slide.png");
    private static final Identifier SLIDE_BACKGROUND = new Identifier(ModConstants.MOD_ID, "textures/gui/bitacora/slide_background.png");
    private static final Identifier MISSION_COMPLETED = new Identifier(ModConstants.MOD_ID, "textures/gui/bitacora/mission_completed.png");
    private static final Identifier MISSION_PENDING = new Identifier(ModConstants.MOD_ID, "textures/gui/bitacora/mission_pending.png");

    private final List<Mission> missions;
    private int scrollOffset = 0;

    // Layout
    private final int guiWidth = 284;
    private final int guiHeight = 284;
    private final int rowHeight = 40;
    private final int scrollHeight = 194;

    public MissionLogScreen(List<Mission> missions) {
        super(Text.literal("Bitácora de Kaleb"));
        this.missions = missions;
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

        // Calculo de qué fila es la primera visible y el offset parcial
        int firstVisible = scrollOffset / rowHeight;
        int yOffset = -(scrollOffset % rowHeight);
        int visibleCount = Math.floorDiv(scrollHeight, rowHeight) + 1;

        for (int i = 0; i < visibleCount && (firstVisible + i) < missions.size(); i++) {
            Mission mission = missions.get(firstVisible + i);
            Identifier texture = mission.completed ? MISSION_COMPLETED : MISSION_PENDING;

            int missionX = x + 20;
            int rowY = scrollY + yOffset + (i * rowHeight);

            // dibujar solo si la fila entra dentro del área visible
            if (rowY + missionHeight > scrollY && rowY < scrollY + scrollHeight) {
                context.drawTexture(texture, missionX, rowY, 0, 0, missionWidth, missionHeight, missionWidth, missionHeight);
                context.drawText(textRenderer, mission.title, missionX + 20, rowY + 15, 0x444444, false);
            }
        }

        // --- Scroll bar ---
        int contentHeight = missions.size() * rowHeight;
        int maxScroll = Math.max(0, contentHeight - scrollHeight);

        if (maxScroll > 0) {
            int scrollbarWidth = 10;
            int scrollbarHeight = 28;
            int scrollbarRange = (scrollHeight - scrollbarHeight) - 8;
            int scrollbarY = scrollY + 4 + (scrollOffset * scrollbarRange / maxScroll);
            int scrollbarX = scrollX + 4;

            context.drawTexture(SLIDE, scrollbarX, scrollbarY, 0, 0, scrollbarWidth, scrollbarHeight, scrollbarWidth, scrollbarHeight);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int contentHeight = missions.size() * rowHeight;
        int maxScroll = Math.max(0, contentHeight - scrollHeight);

        int scrollStep = 8; // píxeles por tick de scroll (ajustable para suavidad)
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

    @Override
    protected void init() {
        super.init();
        if (this.client != null) {
            this.client.mouse.unlockCursor();
        }
    }

    @Override
    public void close() {
        super.close();
        if (this.client != null) {
            this.client.mouse.lockCursor();
        }
    }
}
