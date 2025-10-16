package me.lofro.pelicano2.client.screens.updates;

import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.updates.Updates;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class UpdatesLogScreen extends Screen {

    private final ClientPlayerEntity player;

    // --- Textures ---
    private static final Identifier BACKGROUND = new Identifier(ModConstants.MOD_ID, "textures/gui/bitacora/background.png");
    private static final Identifier SLIDE = new Identifier(ModConstants.MOD_ID, "textures/gui/bitacora/slide.png");
    private static final Identifier SLIDE_BACKGROUND = new Identifier(ModConstants.MOD_ID, "textures/gui/bitacora/slide_background.png");
    private static final Identifier UPDATE_SEEN = new Identifier(ModConstants.MOD_ID, "textures/gui/bitacora/update_seen.png");

    private final List<Updates> updatesInfo;
    private int scrollOffset = 0;

    private final int rowHeight = 40;
    private final int scrollHeight = 194;

    public UpdatesLogScreen(List<Updates> updatesInfo, ClientPlayerEntity player) {
        super(Text.literal("Bitácora de Kaleb"));
        this.updatesInfo = updatesInfo;

        this.player = player;

        player.playSound(SoundEvent.of(new Identifier(ModConstants.MOD_ID, "open_kaleb")), 1.0F, 1.0F);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        // Layout
        int guiWidth = 284;
        int x = (this.width - guiWidth) / 2;
        int guiHeight = 284;
        int y = (this.height - guiHeight) / 2;

        // --- Background ---
        context.drawTexture(BACKGROUND, x, y, 0, 0, guiWidth, guiHeight, guiWidth, guiHeight);

        // --- Scroll area background ---
        int scrollX = x + 245;
        int scrollY = y + 69;
        int scrollWidth = 18;
        context.drawTexture(SLIDE_BACKGROUND, scrollX, scrollY, 0, 0, scrollWidth, scrollHeight, scrollWidth, scrollHeight);

        // --- Updates ---
        int updateWidth = 223;
        int updateHeight = 37;

        // Calculo de qué fila es la primera visible y el offset parcial
        int firstVisible = scrollOffset / rowHeight;
        int yOffset = -(scrollOffset % rowHeight);
        int visibleCount = (int) Math.ceil((double) scrollHeight / (rowHeight));

        int clipTop = scrollY + rowHeight / 2;
        int clipBottom = scrollY + scrollHeight - rowHeight / 2;

        for (int i = 0; i < visibleCount && (firstVisible + i) < updatesInfo.size(); i++) {
            Updates updatesInfo = this.updatesInfo.get(firstVisible + i);

            int updateX = x + 20;
            int rowY = scrollY + yOffset + (i * rowHeight);

            if (rowY + updateHeight > clipTop && rowY < clipBottom) {
                context.drawTexture(UPDATE_SEEN, updateX, rowY, 0, 0, updateWidth, updateHeight, updateWidth, updateHeight);

                int textX = updateX + 20;
                int textY = rowY + 10;
                int maxTextWidth = updateWidth - 40;

                List<OrderedText> lines = textRenderer.wrapLines(updatesInfo.getSummary(), maxTextWidth);

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
        int contentHeight = updatesInfo.size() * rowHeight;
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
        int contentHeight = updatesInfo.size() * rowHeight;
        int maxScroll = Math.max(0, contentHeight - scrollHeight);

        int scrollStep = 6; // píxeles por tick de scroll (ajustable para suavidad)
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

        player.playSound(SoundEvent.of(new Identifier(ModConstants.MOD_ID, "close_kaleb")), 1.0F, 1.0F);
    }
}
