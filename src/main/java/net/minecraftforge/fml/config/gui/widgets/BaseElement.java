package net.minecraftforge.fml.config.gui.widgets;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeConfigSpec;

public abstract class BaseElement<A, T extends ForgeConfigSpec.ConfigValue<A>> extends EntryElement implements IBaseElement {
    private final Minecraft minecraft;
    private final T config;
    private final boolean isValidValue;
    private final String description;

    protected final ForgeConfigSpec.ValueSpec spec;

    private A value;

    public BaseElement(T config, ForgeConfigSpec.ValueSpec spec) {
        this.config = config;
        this.spec = spec;
        isValidValue = hasValidValue();
        value = config.get();
        minecraft = Minecraft.getInstance();
        description = getDescription();
        init(Lists.newArrayList(TextFormatting.GREEN.toString() + config.get()), Lists.newArrayList(TextFormatting.GREEN.toString() + spec.getDefault()), this::undoBtnPress, this::resetBtnPress);
    }

    @Override
    public void render(int mouseX, int mouseY, float partial) {
        super.render(mouseX, mouseY, partial);
        drawString(minecraft.fontRenderer, description, getX() + 5, getY() + (getHeight() - minecraft.fontRenderer.FONT_HEIGHT) / 2, -1);
    }

    @Override
    public void renderTooltip(Screen screen, int mouseX, int mouseY) {
        super.renderTooltip(screen, mouseX, mouseY);
    }

    @Override
    public boolean isDefault() {
        if (isValidValue) {
            return config.get() == spec.getDefault();
        } else {
            return true;
        }
    }

    @Override
    public boolean isChanged() {
        if (isValidValue) {
            return config.get() != value;
        } else {
            return false;
        }
    }

    @Override
    public boolean isValidValue() {
        return isValidValue;
    }

    public String getDescription() {
        String result = I18n.format(spec.getTranslationKey());

        if (result.equals(spec.getTranslationKey())) {
            String[] comment = spec.getComment().split("\n");

            if (comment.length > 0) {
                result = comment[0];
            }
        }

        return result;
    }

    public boolean hasValue() {
        return isValidValue;
    }

    private boolean hasValidValue() {
        try {
            config.set(config.get());
            return true;
        } catch (NullPointerException exception) {
            return false;
        }
    }

    public A getValue() {
        return value;
    }

    public void setValue(A value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    private void undoBtnPress(Button button) {
        value = (A) spec.getDefault();
        button.active = isChanged();
    }

    @SuppressWarnings("unchecked")
    private void resetBtnPress(Button button) {
        config.set((A) spec.getDefault());
        button.active = isDefault();
    }
}
