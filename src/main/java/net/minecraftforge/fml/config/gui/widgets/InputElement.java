package net.minecraftforge.fml.config.gui.widgets;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Function;

public class InputElement<T extends Comparable<T>, U extends ForgeConfigSpec.ConfigValue<T>> extends BaseElement<T, U> {
    private final FocusableTextFieldWidget input;
    private final Function<T, String> toString;
    private final Function<String, T> fromString;

    public InputElement(U config, ForgeConfigSpec.ValueSpec spec, Screen screen, Function<T, String> toString, Function<String, T> fromString) {
        super(config, spec);
        input = new FocusableTextFieldWidget(screen.getMinecraft().fontRenderer, 0, 0, 0, 20 - 4, "");
        this.toString = toString;
        this.fromString = fromString;
        input.setVisible(true);
        input.setText(toString.apply(getValue()));
        input.setCursorPosition(0);
        input.setFocusChangedEvent(this::loseFocus);
        input.setTooltip(Lists.newArrayList("Range: " + TextFormatting.GREEN + "[" + spec.getRange().getMin() + " ~ " + spec.getRange().getMax() + "]"));
        input.setEnabled(hasValue());
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        input.setWidth(width / 4 - 4);
    }

    @Override
    public void render(int mouseX, int mouseY, float partial) {
        super.render(mouseX, mouseY, partial);
        input.x = getX() + getWidth() - 2 * 20 - 2 * 5 - 10 - getWidth() / 4 + 2;
        input.y = getY() + 2;
        input.render(mouseX, mouseY, partial);
    }

    @Override
    public void mouseMoved(double xPos, double mouseY) {
        input.mouseMoved(xPos, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return input.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return input.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        return input.mouseDragged(mouseX, mouseY, button, p_mouseDragged_6_, p_mouseDragged_8_);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return input.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return input.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return input.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
        return input.charTyped(p_charTyped_1_, p_charTyped_2_);
    }

    @Override
    public boolean changeFocus(boolean focus) {
        return input.changeFocus(focus);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return input.isMouseOver(mouseX, mouseY);
    }

    @Override
    public void tick() {
        input.tick();
    }

    @Override
    public void renderTooltip(Screen screen, int mouseX, int mouseY) {
        super.renderTooltip(screen, mouseX, mouseY);
        input.renderToolTip(screen, mouseX, mouseY);
    }

    private boolean doublePredicate(String s) {
        if (s.isEmpty()) {
            return true;
        }

        try {
            fromString.apply(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void loseFocus(boolean isFocused) {
        if (!isFocused) {
            ForgeConfigSpec.Range<T> range = spec.getRange();

            if (doublePredicate(input.getText()) && !input.getText().isEmpty()) {
                setValue(fromString.apply(input.getText()));
            }

            if (getValue().compareTo(range.getMax()) > 0) {
                input.setText(toString.apply(range.getMax()));
            } else if (getValue().compareTo(range.getMin()) < 0) {
                input.setText(toString.apply(range.getMin()));
            } else {
                input.setText(toString.apply(getValue()));
            }
        }
    }
}
