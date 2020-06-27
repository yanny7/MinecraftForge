package net.minecraftforge.fml.config.gui;

public interface IRenderable extends ITooltipRenderer {
    void render(int mouseX, int mouseY, float partial);
}
