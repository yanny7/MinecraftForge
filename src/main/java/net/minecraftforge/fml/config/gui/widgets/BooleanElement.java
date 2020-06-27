package net.minecraftforge.fml.config.gui.widgets;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class BooleanElement extends BaseElement<Boolean, ForgeConfigSpec.BooleanValue> {
    private final Button valueBtn;

    public BooleanElement(ForgeConfigSpec.BooleanValue config, ForgeConfigSpec.ValueSpec spec) {
        super(config, spec);
        valueBtn = new ExtendedButton(0, 0, 0, 20, Boolean.toString(getValue()), this::onPress);
        valueBtn.active = hasValue();
        valueBtn.setFGColor(getColor(getValue()));
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        valueBtn.setWidth(width / 4);
    }

    @Override
    public void render(int mouseX, int mouseY, float partial) {
        super.render(mouseX, mouseY, partial);
        valueBtn.x = getX() + getWidth() - 2 * 20 - 2 * 5 - 10 - getWidth() / 4;
        valueBtn.y = getY();
        valueBtn.render(mouseX, mouseY, partial);
    }

    private void onPress(Button button) {
        setValue(!getValue());
        button.setFGColor(getColor(getValue()));
        button.setMessage(Boolean.toString(getValue()));
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        return valueBtn.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }

    private static int getColor(final boolean b) {
        return b ? 0x55FF55 : 0xFF5555;
    }
}
