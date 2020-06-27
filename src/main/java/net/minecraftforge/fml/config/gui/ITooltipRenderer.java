package net.minecraftforge.fml.config.gui;

import net.minecraft.client.gui.screen.Screen;

public interface ITooltipRenderer {
    void renderTooltip(Screen screen, int mouseX, int mouseY);
}
