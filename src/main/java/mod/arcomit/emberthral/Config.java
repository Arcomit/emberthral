package mod.arcomit.emberthral;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.arcomit.emberthral.util.EnchantmentHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static mod.arcomit.emberthral.util.EnchantmentHelper.processEnchantmentPairs;

@Mod.EventBusSubscriber(modid = Emberthral.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ALWAYS_INCOMPATIBLE;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ALWAYS_COMPATIBLE;

    static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Configurable Enchantment Conflict");

        ALWAYS_COMPATIBLE = builder.comment("Format: 'minecraft:sharpness;minecraft:smite', that will make this two enchantments compatible.")
                .defineList("alwaysCompatible", new ObjectArrayList<>(), o -> o instanceof String);

        ALWAYS_INCOMPATIBLE = builder.comment("Format: 'minecraft:sharpness;minecraft:unbreaking', that will make this two enchantments incompatible.")
                .defineList("alwaysIncompatible", new ObjectArrayList<>(), o -> o instanceof String);
        builder.pop();
        SPEC = builder.build();
    }

    //兼容的附魔
    public static Set<EnchantmentHelper.EnchantmentPair> compatibleEnchantment = new HashSet<>();
    //不兼容的附魔
    public static Set<EnchantmentHelper.EnchantmentPair> incompatibleEnchantment = new HashSet<>();


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        compatibleEnchantment = processEnchantmentPairs(ALWAYS_COMPATIBLE.get());
        incompatibleEnchantment = processEnchantmentPairs(ALWAYS_INCOMPATIBLE.get());
    }
}
