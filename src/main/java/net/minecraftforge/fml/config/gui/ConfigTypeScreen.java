package net.minecraftforge.fml.config.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ForgeI18n;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.forgespi.language.IModInfo;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;

public class ConfigTypeScreen extends Screen {
    static final int MARGIN = 10;
    static final int BUTTON_HEIGHT = 20;
    
    private final EnumMap<ModConfig.Type, ModConfig> configs;
    private final IModInfo info;
    private final Screen screen;
    private Button resetBtn = null;
    private String title;

    public ConfigTypeScreen(@Nonnull EnumMap<ModConfig.Type, ModConfig> configs, IModInfo info, Screen screen) {
        super(new TranslationTextComponent("fml.config.gui.title"));
        this.configs = configs;
        this.info = info;
        this.screen = screen;
    }

    @Override
    protected void init() {
        super.init();
        buildConfigTypeGui();
        addButton(new Button(MARGIN, this.height - BUTTON_HEIGHT - MARGIN, (this.width - 8 * MARGIN) / 4, BUTTON_HEIGHT,
                I18n.format("gui.done"), onPress -> this.minecraft.displayGuiScreen(this.screen)));
        resetBtn = new Button(2 * MARGIN + (width - 8 * MARGIN) / 4, this.height - BUTTON_HEIGHT - MARGIN, (this.width - 8 * MARGIN) / 4, BUTTON_HEIGHT,
                I18n.format("fml.config.gui.reset"), onPress -> {});
        addButton(resetBtn);
        resetBtn.active = false;
        title = TextFormatting.GOLD.toString() + TextFormatting.BOLD.toString() + ForgeI18n.parseMessage("fml.config.gui.name", this.info.getDisplayName());
    }

    @Override
    public void removed() {
        super.removed();
        resetBtn = null;
    }

    @Override
    public void render(int mouseX, int mouseY, float partial) {
        renderBackground();
        super.render(mouseX, mouseY, partial);
        this.resetBtn.render(mouseX, mouseY, partial);
        drawCenteredString(font, title, width / 2, MARGIN, -1);
    }

    private void buildConfigTypeGui() {
        int i = 0;

        for (Map.Entry<ModConfig.Type, ModConfig> entry : configs.entrySet()) {
            ConfigScreen configScreen = new ConfigScreen(this, info, entry.getValue());

            addButton(new Button(this.width / 2 - 100, font.FONT_HEIGHT + 2 * MARGIN + i * (BUTTON_HEIGHT + 5), 200, BUTTON_HEIGHT,
                    I18n.format("fml.config.gui.type." + entry.getKey().name().toLowerCase()), onPress -> {
                this.minecraft.displayGuiScreen(configScreen);
            }));

            i++;
        }
    }
}
