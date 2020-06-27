package net.minecraftforge.fml.config.gui;

import com.electronwill.nightconfig.core.Config;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ForgeI18n;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.gui.widgets.*;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

import static net.minecraftforge.fml.config.gui.ConfigTypeScreen.BUTTON_HEIGHT;
import static net.minecraftforge.fml.config.gui.ConfigTypeScreen.MARGIN;

public class ConfigScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Function<?, ?> NO_FACTORY = o -> null;
    private static final Map<Class<?>, TriFunction<ForgeConfigSpec.ConfigValue<?>, ForgeConfigSpec.ValueSpec, Screen, IBaseElement>> CONFIG_ELEMENTS = new HashMap<>();

    static {
        registerElementFactory(ForgeConfigSpec.BooleanValue.class, (configValue, spec, screen) -> new BooleanElement(configValue, spec));
        registerElementFactory(ForgeConfigSpec.IntValue.class, (configValue, spec, screen) -> new InputElement<>(configValue, spec, screen, i -> Integer.toString(i), Integer::parseInt));
        registerElementFactory(ForgeConfigSpec.DoubleValue.class, (configValue, spec, screen) -> new InputElement<>(configValue, spec, screen, d -> Double.toString(d), Double::parseDouble));
        registerElementFactory(ForgeConfigSpec.LongValue.class, (configValue, spec, screen) -> new InputElement<>(configValue, spec, screen, l -> Long.toString(l), Long::parseLong));
    }

    private final ConfigTypeScreen screen;
    private final IModInfo info;
    private final ModConfig config;
    private final Set<IRenderable> items = new HashSet<>();
    private String title;
    private ElementsScrollPanel scroll;

    protected ConfigScreen(ConfigTypeScreen screen, IModInfo info, ModConfig config) {
        super(new TranslationTextComponent("fml.config.gui.title"));
        this.screen = screen;
        this.info = info;
        this.config = config;
    }

    @Override
    protected void init() {
        super.init();
        addButton(new ExtendedButton(MARGIN, height - BUTTON_HEIGHT - MARGIN, (width - 8 * MARGIN) / 4, BUTTON_HEIGHT,
                I18n.format("gui.done"), onPress -> minecraft.displayGuiScreen(screen)));

        scroll = new ElementsScrollPanel(minecraft, this.width - 20, this.height - BUTTON_HEIGHT - 2 * MARGIN - 2 * MARGIN - 2 * font.FONT_HEIGHT - 5, 2 * MARGIN + 2 * font.FONT_HEIGHT + 5, 10);
        children.add(scroll);
        setFocused(scroll);

        title = TextFormatting.GOLD.toString() + TextFormatting.BOLD.toString() + ForgeI18n.parseMessage("fml.config.gui.name", this.info.getDisplayName());

        buildConfigGui();
    }

    @Override
    public void render(int mouseX, int mouseY, float partial) {
        renderBackground();
        super.render(mouseX, mouseY, partial);
        items.forEach(item -> item.render(mouseX, mouseY, partial));
        scroll.render(mouseX, mouseY, partial);
        drawCenteredString(font, title, width / 2, MARGIN, -1);
        drawCenteredString(font, (config.getConfigData() != null ? config.getFullPath() : config.getFileName()) + " - " + config.getType().extension(),
                this.width / 2, MARGIN + font.FONT_HEIGHT + 5, -1);

        postRender(mouseX, mouseY);
    }

    @Override
    public void tick() {
        super.tick();
        scroll.tick();
    }

    private void postRender(int mouseX, int mouseY) {
        items.forEach(item -> item.renderTooltip(screen, mouseX, mouseY));
        scroll.renderTooltip(this, mouseX, mouseY);
    }

    private void buildConfigGui() {
        items.clear();
        final Map<String, Object> specConfigValues = getSpecConfigValues(config);
        specConfigValues.forEach((name, obj) -> addRootEntries(config, name, obj));
    }

    void addRootEntries(@Nonnull ModConfig modConfig, String name, Object obj) {
        if (obj instanceof ForgeConfigSpec.ConfigValue) {
            ForgeConfigSpec.ConfigValue<?> configValue = (ForgeConfigSpec.ConfigValue<?>) obj;
            ForgeConfigSpec.ValueSpec valueSpec = (ForgeConfigSpec.ValueSpec) getValueSpec(modConfig, configValue.getPath());
            Class<?> clazz = configValue.getClass();
            TriFunction<ForgeConfigSpec.ConfigValue<?>, ForgeConfigSpec.ValueSpec, Screen, IBaseElement> element = CONFIG_ELEMENTS.get(clazz);

            if (element != null) {
                scroll.add(element.apply(configValue, valueSpec, this));
            } else {
                LOGGER.warn("Missing type mapping for {}", clazz.getCanonicalName());
            }
        } else if (obj instanceof Config) {
            Config config = (Config) obj;

            scroll.add(new SubcategoryElement(config));
            traverse(config, name);
        } else {
            LOGGER.warn("Invalid config element type {}", obj);
        }
    }

    void traverse(final Config config, String path) {
        config.valueMap().forEach((name, obj) -> {
            if (obj instanceof Map) {
                LOGGER.info("MAP {}", path + " " + name);
            } else if (obj instanceof ForgeConfigSpec.ConfigValue) {
                LOGGER.info("VALUE {}", path + " " + name);
            } else if (obj instanceof Config) {
                traverse((Config) obj, path + "." + name);
            }
        });
    }




    @Nonnull
    public static Map<String, Object> getSpecConfigValues(@Nonnull ModConfig modConfig) {
        // name -> Object (ConfigValue|SimpleConfig)
        return modConfig.getSpec().getValues().valueMap();
    }

    @Nonnull
    public static Map<String, Object> getSpecValueSpecs(@Nonnull ModConfig modConfig) {
        // name -> Object (ValueSpec|SimpleConfig)
        return modConfig.getSpec().valueMap();
    }

    @Nonnull
    public static Map<String, Object> getConfigValues(@Nonnull ModConfig modConfig) {
        // name -> Object (Boolean|Byte|Short|Integer|Long|Float|Double|Enum|String|List|Config)
        return modConfig.getConfigData().valueMap();
    }

    @SuppressWarnings("unchecked")
    public static <E> void registerElementFactory(final Class<E> clazz, final TriFunction<E, ForgeConfigSpec.ValueSpec, Screen, IBaseElement> factory) {
        CONFIG_ELEMENTS.put(clazz, (TriFunction<ForgeConfigSpec.ConfigValue<?>, ForgeConfigSpec.ValueSpec, Screen, IBaseElement>) factory);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static Object getValueSpec(@Nonnull ModConfig modConfig, @Nonnull List<String> path) {
        Object ret = getSpecValueSpecs(modConfig);

        for (String s : path) {
            if (ret instanceof Map) {
                ret = ((Map<String, Object>) ret).get(s);
            } else if (ret instanceof ForgeConfigSpec.ValueSpec) {
                return ret;
            } else if (ret instanceof Config) {
                ret = ((Config) ret).get(s);
            }
        }

        return ret;
    }

    /*
    @Nonnull
    public static IConfigElement<?> makeConfigElement(@Nonnull ModConfig modConfig, @Nonnull String name, @Nonnull Object obj) {
        if (obj instanceof ForgeConfigSpec.ConfigValue) {
            ForgeConfigSpec.ConfigValue<?> configValue = (ForgeConfigSpec.ConfigValue<?>) obj;
            ForgeConfigSpec.ValueSpec valueSpec = (ForgeConfigSpec.ValueSpec) getValueSpec(modConfig, configValue.getPath());
            Class<?> clazz = valueSpec.getClazz();

            if (clazz == Object.class) {
                Object actualValue = configValue.get();
                Class<?> valueClass = actualValue.getClass();

                if (valueClass != Object.class) {
                    clazz = valueClass;
                } else {
                    Object defaultValue = valueSpec.getDefault();

                    if (defaultValue != null) {
                        clazz = defaultValue.getClass();
                    }
                }
            }

            if (clazz == null || clazz == Object.class) {
                throw new IllegalStateException("fml.configgui.error.nullTypeUseConfig");
            }

            Function<IConfigElementContainer<?>, IConfigElement<?>> factory = recursiveGetFactory(clazz, clazz, CONFIG_ELEMENTS);

            if (factory != null) {
                try {
                    return factory.apply(makeConfigElementContainer(modConfig, configValue));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new IllegalStateException("fml.configgui.error.nullTypeUseConfig");
                }
            } else {
                throw new IllegalStateException("fml.configgui.error.nullTypeUseConfig");
            }
        } else if (obj instanceof Config) {
            Config config = (Config) obj;
            List<String> split = ForgeConfigSpec.split(name);
            Object parentConfig;

            split.remove(split.size() - 1);

            if (split.isEmpty()) {
                parentConfig = modConfig.getConfigData();
            } else {
                parentConfig = getValue(modConfig, split);
            }

            if (parentConfig instanceof CommentedConfig) {
                return new ConfigCategoryElement(config, modConfig, ((CommentedConfig) parentConfig), name);
            } else {
                return new ConfigCategoryElement(config, modConfig, null, name);
            }
        } else {
            throw new IllegalStateException("How? " + name + ", " + obj);
        }
    }*/






    @Nullable
    private static <T extends Function<A, B>, A, B> Function<A, B> recursiveGetFactory(@Nonnull Class<?> clazz, @Nonnull Class<?> originalClass, @Nonnull Map<Class<?>, Function<A, B>> factories) {
        if (clazz == Object.class) {
            return null;
        }

        Function<A, B> factory = factories.get(clazz);

        if (factory == null) {
            factory = recursiveGetFactory(clazz.getSuperclass(), clazz, factories);
        }

        if (factory == null) {
            for (final Class<?> anInterface : clazz.getInterfaces()) {
                factory = recursiveGetFactory(anInterface, clazz, factories);

                if (factory != null) {
                    break;
                }
            }
        }

        if (factory == null) {
            factories.put(clazz, (Function<A, B>) NO_FACTORY); // A factory does NOT exist, avoid checking hierarchy as extensively in the future.
        } else {
            factories.put(originalClass, factory); // A factory DOES exist, avoid checking the class' entire hierarchy in the future.
        }

        return factory;
    }

    @Nonnull
    public static Object getValue(@Nonnull ModConfig modConfig, @Nonnull List<String> path) {
        Object ret = getConfigValues(modConfig);

        for (final String s : path) {
            if (ret instanceof Map) {
                ret = ((Map<String, Object>) ret).get(s);
            } else if (ret instanceof Config) {
                ret = ((Config) ret).get(s);
            }
        }

        return ret;
    }
}
