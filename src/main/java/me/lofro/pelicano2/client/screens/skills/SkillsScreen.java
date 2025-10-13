package me.lofro.pelicano2.client.screens.skills;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lofro.pelicano2.client.screens.selection.SelectionScreen;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.skills.data.Skills;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Map;

public class SkillsScreen extends Screen {

    private final ClientPlayerEntity player;

    private final Identifier BACKGROUND = new Identifier(ModConstants.MOD_ID, "textures/gui/skills/background.png");
    private final Identifier EMPTY_BAR = new Identifier(ModConstants.MOD_ID,"textures/gui/skills/empty_bar.png");
    private final Identifier RESISTANCE = new Identifier(ModConstants.MOD_ID,"textures/gui/skills/resistance.png");
    private final Identifier SPEED = new Identifier(ModConstants.MOD_ID,"textures/gui/skills/speed.png");
    private final Identifier STRENGTH = new Identifier(ModConstants.MOD_ID,"textures/gui/skills/strength.png");

    private final Map<Skills, Integer> skills;
    private final int upgradePoints;

    public SkillsScreen(Map<Skills, Integer> skills, int upgradePoints, ClientPlayerEntity player) {
        super(Text.of("Skills"));

        this.skills = skills;
        this.upgradePoints = upgradePoints;

        this.player = player;

        player.playSound(SoundEvent.of(new Identifier(ModConstants.MOD_ID, "open_skills")), 1.0F, 1.0F);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);

        int guiWidth = 300;
        int guiHeight = 272;

        int x = (this.width - guiWidth) / 2;
        int y = (this.height - guiHeight) / 2;

        context.drawTexture(BACKGROUND, x, y, 0, 0, guiWidth, guiHeight, guiWidth, guiHeight);

        int emptyBarWidth = 177;
        int emptyBarHeight = 7;

        int barWidth = 177;
        int barHeight = 2;

        int iconSize = 36;
        int crossSize = 11;

        int emptyBarX = (this.width / 2) - 80;

        int iconX = emptyBarX - 40;

        int crossX = (this.width / 2) + 100;

        int resistanceBarY = (this.height / 2);
        int resistanceY = resistanceBarY - 15;

        context.drawTexture(EMPTY_BAR, emptyBarX, resistanceBarY, 0, 0, emptyBarWidth, emptyBarHeight, emptyBarWidth, emptyBarHeight);
        context.drawTexture(RESISTANCE, iconX, resistanceY, 0, 0, iconSize, iconSize, iconSize, iconSize);

        String barPath = "textures/gui/skills/bar_";

        int skillLevel = skills.get(Skills.RESISTANCE);

        barPath += skillLevel;

        if ((!barPath.endsWith("0") || barPath.endsWith("10")) && !barPath.endsWith("_"))
            context.drawTexture(new Identifier(ModConstants.MOD_ID, barPath + ".png"), emptyBarX, resistanceBarY + 2, 0, 0, barWidth, barHeight, barWidth, barHeight);

        barPath = skillLevel < 10 ? barPath.substring(0, barPath.length() - 1) : barPath.substring(0, barPath.length() - 2);

        int MAX_LEVEL = 10;
        if (upgradePoints > 0 && !skills.isEmpty() && skills.get(Skills.RESISTANCE) < MAX_LEVEL) {
            this.addDrawableChild(new CrossButton(crossSize, crossSize, crossX, resistanceBarY - 3, () -> onCrossClick(Skills.RESISTANCE)));
        }

        int strengthBarY = resistanceBarY - 40;
        int strengthY = strengthBarY - 15;

        skillLevel = skills.get(Skills.STRENGTH);

        barPath += skillLevel;

        context.drawTexture(EMPTY_BAR, emptyBarX, strengthBarY, 0, 0, emptyBarWidth, emptyBarHeight, emptyBarWidth, emptyBarHeight);
        context.drawTexture(STRENGTH, iconX, strengthY, 0, 0, iconSize, iconSize, iconSize, iconSize);

        if ((!barPath.endsWith("0") || barPath.endsWith("10")) && !barPath.endsWith("_"))
            context.drawTexture(new Identifier(ModConstants.MOD_ID, barPath + ".png"), emptyBarX, strengthBarY + 2, 0, 0, barWidth, barHeight, barWidth, barHeight);

        barPath = skillLevel < 10 ? barPath.substring(0, barPath.length() - 1) : barPath.substring(0, barPath.length() - 2);

        if (upgradePoints > 0 && !skills.isEmpty() && skills.get(Skills.STRENGTH) < MAX_LEVEL) {
            this.addDrawableChild(new CrossButton(crossSize, crossSize, crossX, strengthBarY - 3, () -> onCrossClick(Skills.STRENGTH)));
        }

        int speedBarY = resistanceBarY + 40;
        int speedY = speedBarY - 15;

        skillLevel = skills.get(Skills.SPEED);

        barPath += skillLevel;

        context.drawTexture(EMPTY_BAR, emptyBarX, speedBarY, 0, 0, emptyBarWidth, emptyBarHeight, emptyBarWidth, emptyBarHeight);
        context.drawTexture(SPEED, iconX, speedY, 0, 0, iconSize, iconSize, iconSize, iconSize);

        if ((!barPath.endsWith("0") || barPath.endsWith("10")) && !barPath.endsWith("_"))
            context.drawTexture(new Identifier(ModConstants.MOD_ID, barPath + ".png"), emptyBarX, speedBarY + 2, 0, 0, barWidth, barHeight, barWidth, barHeight);

        if (upgradePoints > 0 && !skills.isEmpty() && skills.get(Skills.SPEED) < MAX_LEVEL) {
            this.addDrawableChild(new CrossButton(crossSize, crossSize, crossX, speedBarY - 3, () -> onCrossClick(Skills.SPEED)));
        }

        String label = "Puntos de mejora: ";
        String value = String.valueOf(upgradePoints);

        int labelWidth = this.textRenderer.getWidth(label);
        int valueWidth = this.textRenderer.getWidth(value);
        int totalWidth = labelWidth + valueWidth;

        int startX = (this.width / 2) - (totalWidth / 2);

        context.drawTextWithShadow(this.textRenderer, label, startX, speedBarY + 40, 0xCCCCCC);

        context.drawTextWithShadow(this.textRenderer, value, startX + labelWidth, speedBarY + 40, 0x00C8FF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void onCrossClick(Skills skill) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeEnumConstant(skill);

        ClientPlayNetworking.send(PacketIDS.CLICK_UPDATE_SKILL_C2S, packetByteBuf);

        player.playSound(SoundEvent.of(new Identifier(ModConstants.MOD_ID, "level_up")), 1.0F, 1.0F);

        this.close();

        PacketByteBuf newBuf = PacketByteBufs.create();

        newBuf.writeEnumConstant(SelectionScreen.Buttons.SKILLS);

        ClientPlayNetworking.send(PacketIDS.CLICK_SELECTION_ICON_C2S, newBuf);
    }

    @Override
    public void close() {
        player.playSound(SoundEvent.of(new Identifier(ModConstants.MOD_ID, "close_skills")), 1.0F, 1.0F);
        super.close();
    }

    private static class CrossButton extends ButtonWidget {
        private final Identifier CROSS = new Identifier(ModConstants.MOD_ID,"textures/gui/skills/cross.png");
        private final Runnable onClick;

        public CrossButton(int width, int height, int x, int y, Runnable onClick) {
            super(x, y, width, height, Text.empty(), button -> {}, DEFAULT_NARRATION_SUPPLIER);
            this.onClick = onClick;
        }

        @Override
        public void onPress() {
            this.onClick.run();
        }

        @Override
        public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            RenderSystem.enableBlend();
            context.drawTexture(CROSS, this.getX(), this.getY(), 0, 0, width, height, width, height);
            RenderSystem.disableBlend();
        }
    }

}
