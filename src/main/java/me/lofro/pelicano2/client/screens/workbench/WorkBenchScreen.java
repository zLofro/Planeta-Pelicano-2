package me.lofro.pelicano2.client.screens.workbench;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class WorkBenchScreen extends Screen {

    public WorkBenchScreen(Text title) {
        super(title);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context);


    }

    private void renderItemWithTooltip(DrawContext context, int mouseX, int mouseY, ItemStack stack, int x, int y) {
        // Dibuja el ítem
        context.drawItem(stack, x, y);
        context.drawItemInSlot(this.textRenderer, stack, x, y); // dibuja cantidad, encantamientos, etc.

        // Detecta si el ratón está encima (área de 16x16 píxeles)
        if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
            context.drawItemTooltip(this.textRenderer, stack, mouseX, mouseY);
        }
    }

}
