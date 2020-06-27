package net.minecraftforge.fml.config.gui.widgets;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;

import java.util.ArrayList;
import java.util.List;

public class FocusableTextFieldWidget extends TextFieldWidget {
    private FocusChangedEvent focusChangedEvent;
    private List<String> tooltip = new ArrayList<>();
    private boolean focus;

    public FocusableTextFieldWidget(FontRenderer fontIn, int xIn, int yIn, int widthIn, int heightIn, String msg) {
        super(fontIn, xIn, yIn, widthIn, heightIn, msg);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean oldFocus = focus;
        focus = isMouseOver(mouseX, mouseY);

        if (oldFocus != focus) {
            focusChangedEvent.onFocusChanged(focus);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return visible && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void setFocusChangedEvent(FocusChangedEvent focusChangedEvent) {
        this.focusChangedEvent = focusChangedEvent;
    }

    public void setTooltip(List<String> tooltip) {
        this.tooltip = tooltip;
    }

    public void renderToolTip(Screen screen, int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            screen.renderTooltip(tooltip, mouseX, mouseY);
        }
    }

    public interface FocusChangedEvent {
        void onFocusChanged(boolean isFocused);
    }
}
