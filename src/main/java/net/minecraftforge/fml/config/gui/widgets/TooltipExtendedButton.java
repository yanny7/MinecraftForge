package net.minecraftforge.fml.config.gui.widgets;

import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.config.gui.ITooltipRenderer;

import java.util.ArrayList;
import java.util.List;

public class TooltipExtendedButton extends ExtendedButton implements ITooltipRenderer {
    List<String> tooltip = new ArrayList<>();

    public TooltipExtendedButton(int xPos, int yPos, int width, int height, String displayString, IPressable handler) {
        super(xPos, yPos, width, height, displayString, handler);
    }

    public void setTooltip(List<String> tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return visible && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void renderTooltip(Screen screen, int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            screen.renderTooltip(tooltip, mouseX, mouseY);
        }
    }
}
