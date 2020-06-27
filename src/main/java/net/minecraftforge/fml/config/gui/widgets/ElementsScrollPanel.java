package net.minecraftforge.fml.config.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraftforge.client.gui.ScrollPanel;
import net.minecraftforge.fml.config.gui.ITooltipRenderer;

import java.util.ArrayList;
import java.util.List;

public class ElementsScrollPanel extends ScrollPanel implements ITooltipRenderer, IGuiEventListener {
    List<IBaseElement> list = new ArrayList<>();

    public ElementsScrollPanel(Minecraft client, int width, int height, int top, int left) {
        super(client, width, height, top, left);
    }

    @Override
    protected int getContentHeight() {
        return list.stream().mapToInt(value -> value.getHeight() + 5).sum();
    }

    @Override
    protected void drawPanel(int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {
        int offset = 0;

        for (IBaseElement element : list) {
            element.setX(left);
            element.setY(relativeY + offset);
            offset += element.getHeight() + 5;
            element.render(mouseX, mouseY, 0);
        }
    }

    @Override
    public void renderTooltip(Screen screen, int mouseX, int mouseY) {
        list.forEach(baseElement -> baseElement.renderTooltip(screen, mouseX, mouseY));
    }

    @Override
    public void mouseMoved(double xPos, double mouseY) {
        list.forEach(baseElement -> baseElement.mouseMoved(xPos, mouseY));
        super.mouseMoved(xPos, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        list.forEach(baseElement -> baseElement.mouseClicked(mouseX, mouseY, button));
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        list.forEach(baseElement -> baseElement.mouseReleased(mouseX, mouseY, button));
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        list.forEach(baseElement -> baseElement.mouseDragged(mouseX, mouseY, button, p_mouseDragged_6_, p_mouseDragged_8_));
        return super.mouseDragged(mouseX, mouseY, button, p_mouseDragged_6_, p_mouseDragged_8_);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        list.forEach(baseElement -> baseElement.mouseScrolled(mouseX, mouseY, amount));
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        list.forEach(baseElement -> baseElement.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        list.forEach(baseElement -> baseElement.keyReleased(keyCode, scanCode, modifiers));
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
        list.forEach(baseElement -> baseElement.charTyped(p_charTyped_1_, p_charTyped_2_));
        return super.charTyped(p_charTyped_1_, p_charTyped_2_);
    }

    @Override
    public boolean changeFocus(boolean focus) {
        list.forEach(baseElement -> baseElement.changeFocus(focus));
        return super.changeFocus(focus);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        list.forEach(baseElement -> baseElement.isMouseOver(mouseX, mouseY));
        return super.isMouseOver(mouseX, mouseY);
    }

    public void add(IBaseElement renderable) {
        list.add(renderable);
        renderable.setWidth(width);
    }

    public void tick() {
        list.forEach(ITickable::tick);
    }
}
