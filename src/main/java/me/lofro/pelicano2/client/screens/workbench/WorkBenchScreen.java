package me.lofro.pelicano2.client.screens.workbench;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.workbench.WorkBenchRecipe;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WorkBenchScreen extends Screen {

    private final Identifier BACKGROUND = new Identifier(ModConstants.MOD_ID, "textures/gui/workbench/background.png");
    private final Identifier SLIDE_BACKGROUND = new Identifier(ModConstants.MOD_ID, "textures/gui/workbench/slide_background.png");
    private final Identifier SLIDE = new Identifier(ModConstants.MOD_ID, "textures/gui/workbench/slide.png");
    private final Identifier START_BUTTON = new Identifier(ModConstants.MOD_ID, "textures/gui/workbench/start.png");
    private final Identifier INFORMATION_PANEL = new Identifier(ModConstants.MOD_ID, "textures/gui/workbench/information_panel.png");

    private int scrollOffset = 0;
    private final int rowHeight = 46;
    private final int scrollHeight = 182;

    private final int content = WorkBenchRecipe.values().length;

    private int secondsLeft;

    private int tickCounter = 0;

    private final boolean activeCraft;

    public WorkBenchScreen(int secondsLeft, boolean activeCraft) {
        super(Text.of("WorkBench"));

        this.secondsLeft = secondsLeft;
        this.activeCraft = activeCraft;
    }

    @Override
    public void tick() {
        super.tick();

        tickCounter++;

        if (tickCounter >= 20 && secondsLeft >= 0) {
            tickCounter = 0;
            secondsLeft--;
        }
    }

    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context);

        int guiWidth = 427;
        int guiHeight = 222;

        int x = (this.width - guiWidth) / 2;
        int y = (this.height - guiHeight) / 2;

        // Fondo principal
        context.drawTexture(BACKGROUND, x, y, 0, 0, guiWidth, guiHeight, guiWidth, guiHeight);

        int scrollX = (this.width / 2) + 30;
        int scrollY = y + 20;
        int scrollWidth = 18;

        // Fondo de la barra de scroll
        context.drawTexture(SLIDE_BACKGROUND, scrollX, scrollY, 0, 0, scrollWidth, scrollHeight, scrollWidth, scrollHeight);

        int panelWidth = 208;
        int panelHeight = 39;

        int firstVisible = (scrollOffset / rowHeight);
        int yOffset = -(scrollOffset % rowHeight);
        int visibleCount = (int) Math.ceil((double) scrollHeight / (rowHeight));

        int clipTop = (int) (scrollY + (rowHeight / 1.5F));
        int clipBottom = (int) (scrollY + scrollHeight - rowHeight / 1.5F);

        // Render de cada panel visible
        for (int i = 0; i < visibleCount && (firstVisible + i) < content; i++) {
            int panelX = x + 20;
            int rowY = (scrollY + 5) + yOffset + (i * rowHeight);

            if (rowY + panelHeight > clipTop && rowY < clipBottom) {
                // Panel de fondo
                context.drawTexture(INFORMATION_PANEL, panelX, rowY, 0, 0, panelWidth, panelHeight, panelWidth, panelHeight);

                // Obtener receta correspondiente
                WorkBenchRecipe recipe = WorkBenchRecipe.values()[firstVisible + i];

                // Render ítems
                int itemY = rowY + 11;
                int leftStartX = panelX + 12;

                int[] addPositions = {0, 28, 57, 84, 113};

                // Renderizar los ingredientes (4)
                for (int j = 0; j < recipe.getRecipe().length; j++) {
                    ItemStack ingredientStack = new ItemStack(recipe.getRecipe()[j]);
                    int itemX = leftStartX + addPositions[j];
                    renderItemWithTooltip(context, mouseX, mouseY, ingredientStack, itemX, itemY);
                }

                // Renderizar el resultado a la derecha
                int resultX = panelX + panelWidth - 29;
                renderItemWithTooltip(context, mouseX, mouseY, new ItemStack(recipe.getResult()), resultX, itemY);
            }
        }

        // Cálculo del scroll
        int contentHeight = rowHeight * content;
        int maxScroll = Math.max(0, contentHeight - scrollHeight);

        int slideWidth = 10;
        int slideHeight = 28;
        int slideRange = (scrollHeight - slideHeight) - 8;

        int slideY = scrollY + 4 + (maxScroll == 0 ? 0 : (scrollOffset * slideRange / maxScroll));
        int slideX = scrollX + 4;

        // Dibuja el slider
        context.drawTexture(SLIDE, slideX, slideY, 0, 0, slideWidth, slideHeight, slideWidth, slideHeight);

        // Botón de start
        int buttonWidth = 88;
        int buttonHeight = 35;
        int buttonX = scrollX + 68;
        int buttonY = scrollY + scrollHeight - buttonHeight;

        this.addDrawableChild(new IconButton(buttonWidth, buttonHeight, buttonX, buttonY, START_BUTTON, () -> {
            PacketByteBuf packetByteBuf = PacketByteBufs.create();

            packetByteBuf.writeBoolean(secondsLeft < 0);

            ClientPlayNetworking.send(PacketIDS.WORKBENCH_BUTTON_CLICK_C2S, packetByteBuf);

            this.close();
        }));

        float textScale = 1.5F; // 1.5 más grande

        int textX = (int) ((buttonX + ((float) buttonWidth / 2)) / textScale);
        int textY = (int) ((buttonY - 70) / textScale); // más arriba del botón

        if (secondsLeft >= 0) {
            String timeLabel = "TIEMPO RESTANTE";
            String formatted = formatTime(secondsLeft);

            context.getMatrices().push();
            context.getMatrices().scale(textScale, textScale, 1.0f);

            context.drawCenteredTextWithShadow(this.textRenderer, timeLabel, textX, textY, 0xFFFFFF);

            context.drawCenteredTextWithShadow(this.textRenderer, formatted, textX, textY + 14, 0xFFD700);

            context.getMatrices().pop();
        } else if (this.activeCraft) {
            String timeLabel = "ITEM LISTO";

            context.getMatrices().push();
            context.getMatrices().scale(textScale, textScale, 1.0f);

            context.drawCenteredTextWithShadow(this.textRenderer, timeLabel, textX, textY, 0xFFFFFF);

            context.getMatrices().pop();
        }

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderItemWithTooltip(DrawContext context, int mouseX, int mouseY, ItemStack stack, int x, int y) {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();

        context.drawItem(stack, x, y);
        context.drawItemInSlot(this.textRenderer, stack, x, y);

        if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
            context.drawItemTooltip(this.textRenderer, stack, mouseX, mouseY);
        }

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int contentHeight = content * rowHeight;
        int maxScroll = Math.max(0, contentHeight - scrollHeight);

        int scrollStep = 10;
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

    // Botón con textura
    private static class IconButton extends ButtonWidget {
        private final Identifier texture;
        private final Runnable onClick;

        public IconButton(int width, int height, int x, int y, Identifier texture, Runnable onClick) {
            super(x, y, width, height, Text.empty(), button -> {}, DEFAULT_NARRATION_SUPPLIER);
            this.texture = texture;
            this.onClick = onClick;
        }

        @Override
        public void onPress() {
            this.onClick.run();
        }

        @Override
        public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            RenderSystem.enableBlend();
            context.drawTexture(texture, this.getX(), this.getY(), 0, 0, width, height, width, height);
            RenderSystem.disableBlend();
        }
    }
}