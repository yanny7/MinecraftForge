package net.minecraftforge.debug.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

@Mod("config_test")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigTest {
    private static final String MODID = "config_test";

    @SubscribeEvent
    public static void init(@Nonnull FMLCommonSetupEvent event) {
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, ConfigHolder.SERVER_SPEC);
    }

    @SubscribeEvent
    public static void onModConfigEvent(@Nonnull ModConfig.ModConfigEvent event) {
        final ModConfig config = event.getConfig();

        if (!config.getModId().equals(MODID)) {
            return;
        }

        if (config.getSpec() == ConfigHolder.CLIENT_SPEC) {
            ConfigHelper.bakeClient();
        } else if (config.getSpec() == ConfigHolder.SERVER_SPEC) {
            ConfigHelper.bakeServer();
        }
    }

    static class ConfigHelper {
        public static void bakeServer() {

        }

        public static void bakeClient() {

        }
    }

    static class ConfigHolder {
        public static final ForgeConfigSpec CLIENT_SPEC;
        public static final ForgeConfigSpec SERVER_SPEC;
        static final ClientConfig CLIENT;
        static final ServerConfig SERVER;

        static {
            {
                final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
                CLIENT = specPair.getLeft();
                CLIENT_SPEC = specPair.getRight();
            }
            {
                final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
                SERVER = specPair.getLeft();
                SERVER_SPEC = specPair.getRight();
            }
        }
    }

    static class ClientConfig {
        final ForgeConfigSpec.BooleanValue topTest;
        final ForgeConfigSpec.BooleanValue top1Test;
        final ForgeConfigSpec.BooleanValue top2Test;
        final ForgeConfigSpec.IntValue top3Test;
        final ForgeConfigSpec.DoubleValue top4Test;
        final ForgeConfigSpec.LongValue top5Test;

        final ForgeConfigSpec.BooleanValue booleanTest;
        final ForgeConfigSpec.BooleanValue subTest;
        final ForgeConfigSpec.BooleanValue sub2Test;

        ClientConfig(@Nonnull final ForgeConfigSpec.Builder builder) {
            topTest = builder
                    .comment("Test boolean value")
                    .translation(MODID + ".config.boolean_test")
                    .define("topTest", true);
            top1Test = builder
                    .comment("Test boolean value")
                    .translation(MODID + ".config.boolean_test")
                    .define("top1Test", true);
            top2Test = builder
                    .comment("Test boolean value")
                    .translation(MODID + ".config.boolean_test")
                    .define("top2Test", true);
            top3Test = builder
                    .comment("Test integer value")
                    .translation(MODID + ".config.integer_test")
                    .defineInRange("top3Test", 5, 0, 1000);
            top4Test = builder
                    .comment("Test real value")
                    .translation(MODID + ".config.double_test")
                    .defineInRange("top4Test", 2.53, -4.0, 10.9);
            top5Test = builder
                    .comment("Test long value")
                    .translation(MODID + ".config.integer_test")
                    .defineInRange("top5Test", 5L, -1000L, 1000L);
            builder.push("general");
            booleanTest = builder
                    .comment("Test boolean value")
                    .translation(MODID + ".config.boolean_test")
                    .define("booleanTest", true);
            builder.push("sub1");
            subTest = builder
                    .comment("Test boolean value")
                    .translation(MODID + ".config.boolean_test")
                    .define("subTest", true);
            builder.push("sub2");
            sub2Test = builder
                    .comment("Test boolean value")
                    .translation(MODID + ".config.boolean_test")
                    .define("sub2Test", true);
            builder.pop();
            builder.pop();
            builder.pop();
        }
    }

    static class ServerConfig {
        final ForgeConfigSpec.BooleanValue booleanTest;

        ServerConfig(@Nonnull final ForgeConfigSpec.Builder builder) {
            builder.push("general");
            booleanTest = builder
                    .comment("Test boolean value")
                    .translation(MODID + ".config.boolean_test")
                    .define("booleanTest", true);
            builder.pop();
        }
    }
}
