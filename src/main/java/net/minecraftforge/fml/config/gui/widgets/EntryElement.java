package net.minecraftforge.fml.config.gui.widgets;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.List;

public abstract class EntryElement extends AbstractGui implements IBaseElement {
    @Nullable private TooltipExtendedButton undoBtn;
    @Nullable private TooltipExtendedButton resetBtn;

    private int x;
    private int y;
    private int width;
    private int height;

    EntryElement() {
        height = 20;
    }

    void init(List<String> undoTooltip, List<String> resetTooltip, Button.IPressable undoBtnPress, Button.IPressable resetBtnPress) {
        List<String> resetList = Lists.newArrayList("Reset To Default");
        List<String> undoList = Lists.newArrayList("Undo Changes");

        resetList.addAll(resetTooltip);
        undoList.addAll(undoTooltip);

        height = 20;
        undoBtn = new TooltipExtendedButton(0, 0, 20, 20, TextFormatting.BOLD.toString() + "\u293A", undoBtnPress);
        undoBtn.setTooltip(undoList);
        undoBtn.active = isChanged() && isValidValue();
        resetBtn = new TooltipExtendedButton(0, 0, 20, 20, TextFormatting.BOLD.toString() + "\u27F2", resetBtnPress);
        resetBtn.setTooltip(resetList);
        resetBtn.active = !isDefault() && isValidValue();
    }



    abstract boolean isDefault();

    abstract boolean isChanged();

    abstract boolean isValidValue();

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(int mouseX, int mouseY, float partial) {
        if (undoBtn != null && resetBtn != null) {
            undoBtn.x = x + width - 2 * 20 - 5 - 10;
            resetBtn.x = x + width - 20 - 10;
            undoBtn.y = resetBtn.y = y;
            undoBtn.render(mouseX, mouseY, partial);
            resetBtn.render(mouseX, mouseY, partial);
        }
    }

    @Override
    public void renderTooltip(Screen screen, int mouseX, int mouseY) {
        if (undoBtn != null && resetBtn != null) {
            undoBtn.renderTooltip(screen, mouseX, mouseY);
            resetBtn.renderTooltip(screen, mouseX, mouseY);
        }
    }
}
