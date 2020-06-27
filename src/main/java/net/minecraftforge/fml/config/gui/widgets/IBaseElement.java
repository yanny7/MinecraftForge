package net.minecraftforge.fml.config.gui.widgets;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraftforge.fml.config.gui.IRenderable;

public interface IBaseElement extends IRenderable, IGuiEventListener, ITickable {
    int getWidth();
    int getHeight();
    int getX();
    int getY();

    void setWidth(int width);
    void setHeight(int height);
    void setX(int x);
    void setY(int y);
}
