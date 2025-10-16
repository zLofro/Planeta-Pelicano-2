package me.lofro.pelicano2.client.screens.bosses;

import me.lofro.pelicano2.global.utils.ModConstants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BossesScreen extends Screen {

    private final ClientPlayerEntity player;

    private final Identifier BACKGROUND = new Identifier(ModConstants.MOD_ID, "textures/gui/skills/background.png");

    private final Identifier IRON_GOLEM = new Identifier(ModConstants.MOD_ID, "textures/gui/bosses/iron_golem_boss.png");
    private final Identifier IRON_GOLEM_SHADOW = new Identifier(ModConstants.MOD_ID, "textures/gui/bosses/iron_golem_boss_shadow.png");

    private final Identifier WARDEN = new Identifier(ModConstants.MOD_ID, "textures/gui/bosses/warden.png");
    private final Identifier WARDEN_KNIFE = new Identifier(ModConstants.MOD_ID, "textures/gui/bosses/warden_knife.png");
    private final Identifier WARDEN_SHADOW = new Identifier(ModConstants.MOD_ID, "textures/gui/bosses/warden_shadow.png");

    private final Identifier WITHER = new Identifier(ModConstants.MOD_ID, "textures/gui/bosses/wither.png");
    private final Identifier WITHER_KNIFE = new Identifier(ModConstants.MOD_ID, "textures/gui/bosses/wither_knife.png");
    private final Identifier WITHER_SHADOW = new Identifier(ModConstants.MOD_ID, "textures/gui/bosses/wither_shadow.png");

    private final Identifier DRAGON = new Identifier(ModConstants.MOD_ID, "textures/gui/bosses/dragon.png");
    private final Identifier DRAGON_SHADOW = new Identifier(ModConstants.MOD_ID, "textures/gui/bosses/dragon_shadow.png");
    private final Identifier DRAGON_KNIFE = new Identifier(ModConstants.MOD_ID, "textures/gui/bosses/dragon_knife.png");

    public BossesScreen(ClientPlayerEntity player) {
        super(Text.of("Bosses"));

        this.player = player;

        player.playSound(SoundEvent.of(new Identifier(ModConstants.MOD_ID, "open_missions")), 1.0F, 1.0F);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);

        int guiWidth = 300;
        int guiHeight = 272;

        int x = (this.width - guiWidth) / 2;
        int y = (this.height - guiHeight) / 2;

        context.drawTexture(BACKGROUND, x, y, 0, 0, guiWidth, guiHeight, guiWidth, guiHeight);

        int witherWidth = 37;
        int witherHeight = 48;

        int witherX = (this.width - witherWidth) / 2;
        int witherY = (this.height - witherHeight) / 2;

        context.drawTexture(WITHER_SHADOW, witherX, witherY, 0, 0, witherWidth, witherHeight, witherWidth, witherHeight);

        int wardenWidth = 32;
        int wardenHeight = 44;

        int wardenX = ((this.width - wardenWidth) / 2);
        int wardenY = ((this.height - wardenHeight) / 2) - 50;

        context.drawTexture(WARDEN_SHADOW, wardenX, wardenY, 0, 0, wardenWidth, wardenHeight, wardenWidth, wardenHeight);

        int dragonWidth = 123;
        int dragonHeight = 77;

        int dragonX = (this.width - dragonWidth) / 2;
        int dragonY = ((this.height - dragonHeight) / 2) + 60;

        context.drawTexture(DRAGON_SHADOW, dragonX, dragonY, 0, 0, dragonWidth, dragonHeight, dragonWidth, dragonHeight);

        float textScale = 2f;

        int textX = (int) (((float) this.width / 2) / textScale);
        int textY = (int) (((float) (this.height - guiHeight) - 80) / textScale);

        context.getMatrices().push();
        context.getMatrices().scale(textScale, textScale, 1.0f);

        context.drawCenteredTextWithShadow(this.textRenderer, "Objetivos", textX, textY, 0xAAAAAA);

        context.getMatrices().pop();

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        player.playSound(SoundEvent.of(new Identifier(ModConstants.MOD_ID, "close_mission_screen")), 1.0F, 1.0F);
        super.close();
    }

}
