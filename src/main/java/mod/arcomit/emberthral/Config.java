package mod.arcomit.emberthral;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Emberthral.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ALWAYS_INCOMPATIBLE;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ALWAYS_COMPATIBLE;


    static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Configurable Enchantment Conflict");
        // 构建冲突对配置
        ALWAYS_INCOMPATIBLE = builder.comment("Format: 'minecraft:sharpness;minecraft:unbreaking', that will make this two enchantments incompatible.")
                .defineList("alwaysIncompatible", new ObjectArrayList<>(), o -> o instanceof String);

        // 构建非冲突对配置
        ALWAYS_COMPATIBLE = builder.comment("Format: 'minecraft:sharpness;minecraft:smite', that will make this two enchantments compatible.")
                .defineList("alwaysCompatible", new ObjectArrayList<>(), o -> o instanceof String);
        builder.pop();
        SPEC = builder.build();
    }

    public static List<? extends String> alwaysIncompatible;
    public static List<? extends String> alwaysCompatible;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        alwaysIncompatible = ALWAYS_INCOMPATIBLE.get();
        alwaysCompatible = ALWAYS_COMPATIBLE.get();
    }
}
