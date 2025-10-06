package me.lofro.pelicano2.client.screens.selection;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.global.utils.ModConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SelectionScreen extends Screen {

    private final Identifier SELECTION_OVERLAY = new Identifier(ModConstants.MOD_ID, "textures/gui/selection/selection_overlay.png");

    private final Identifier UPDATES_ICON = new Identifier(ModConstants.MOD_ID,"textures/gui/selection/updates_icon.png");
    private final Identifier MISSIONS_ICON = new Identifier(ModConstants.MOD_ID,"textures/gui/selection/missions_icon.png");
    private final Identifier SKILLS_ICON = new Identifier(ModConstants.MOD_ID,"textures/gui/selection/skills_icon.png");
    private final Identifier BOSSES_ICON = new Identifier(ModConstants.MOD_ID,"textures/gui/selection/bosses_icon.png");

    // Tamaños
    private static final int SELECTION_OVERLAY_WIDTH = 292;
    private static final int SELECTION_OVERLAY_HEIGHT = 294;

    public SelectionScreen() {
        super(Text.of("Selection Screen"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addDrawableChild(new IconButton(55, 72, centerX - 90, centerY + 30, UPDATES_ICON, () -> onIconClick(Buttons.UPDATES)));
        this.addDrawableChild(new IconButton(65, 50, centerX - 105, centerY - 90, MISSIONS_ICON, () -> onIconClick(Buttons.MISSIONS)));
        this.addDrawableChild(new IconButton(56, 71, centerX + 50, centerY + 30, SKILLS_ICON, () -> onIconClick(Buttons.SKILLS)));
        this.addDrawableChild(new IconButton(54, 49, centerX + 50, centerY - 80, BOSSES_ICON, () -> onIconClick(Buttons.BOSSES)));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void onIconClick(Buttons button) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeEnumConstant(button);

        ClientPlayNetworking.send(PacketIDS.CLICK_SELECTION_ICON_C2S, packetByteBuf);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Dibuja la imagen centrada
        RenderSystem.enableBlend();
        context.drawTexture(
                SELECTION_OVERLAY,
                centerX - SELECTION_OVERLAY_WIDTH / 2,
                centerY - SELECTION_OVERLAY_HEIGHT / 2,
                0, 0,
                SELECTION_OVERLAY_WIDTH, SELECTION_OVERLAY_HEIGHT,
                SELECTION_OVERLAY_WIDTH, SELECTION_OVERLAY_HEIGHT
        );
        RenderSystem.disableBlend();

        super.render(context, mouseX, mouseY, delta);
    }

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

    public enum Buttons {
        UPDATES, MISSIONS, BOSSES, SKILLS;
    }

}
