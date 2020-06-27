package net.minecraftforge.fml.config.gui.widgets;

import com.electronwill.nightconfig.core.Config;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.widget.button.Button;

public class SubcategoryElement extends EntryElement {
    private final Config config;

    public SubcategoryElement(Config config) {
        this.config = config;
        init(Lists.newArrayList(), Lists.newArrayList(), this::undoBtnPress, this::resetBtnPress);
    }

    @Override
    boolean isDefault() {
        return false;
    }

    @Override
    boolean isChanged() {
        return false;
    }

    @Override
    boolean isValidValue() {
        return false;
    }

    private void undoBtnPress(Button button) {
    }

    private void resetBtnPress(Button button) {
    }
}
