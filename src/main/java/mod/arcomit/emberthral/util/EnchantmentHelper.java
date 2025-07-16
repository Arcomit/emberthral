package mod.arcomit.emberthral.util;

import mod.arcomit.emberthral.Config;
import mod.arcomit.emberthral.Emberthral;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EnchantmentHelper {

    public static boolean isCompat(Enchantment instance, Enchantment arg, boolean originalResult) {
        // 预处理后的数据直接比对实例
        EnchantmentPair currentPair = new EnchantmentPair(instance, arg);

        if (!originalResult) {
            // 检查是否在强制兼容列表中
            return Config.compatibleEnchantment.contains(currentPair);
        } else {
            // 检查是否在强制冲突列表中
            return !Config.incompatibleEnchantment.contains(currentPair);
        }
    }

    public static Set<EnchantmentPair> processEnchantmentPairs(List<? extends String> rawPairs) {
        Set<EnchantmentPair> pairs = new HashSet<>();
        for (String pairStr : rawPairs) {
            String[] parts = pairStr.split(";");
            if (parts.length != 2) {
                Emberthral.LOGGER.warn("Invalid enchantment pair format: {}", pairStr);
                continue;
            }
            Enchantment e1 = ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation.parse(parts[0]));
            Enchantment e2 = ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation.parse(parts[1]));
            if (e1 == null || e2 == null) {
                Emberthral.LOGGER.warn("Enchantment not found in pair: {}", pairStr);
                continue;
            }
            pairs.add(new EnchantmentPair(e1, e2));
        }
        return pairs;
    }

    public record EnchantmentPair(Enchantment e1, Enchantment e2) {
        public boolean matches(Enchantment a, Enchantment b) {
            return (e1 == a && e2 == b) || (e1 == b && e2 == a);
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EnchantmentPair that = (EnchantmentPair) o;
            return (Objects.equals(e1, that.e1) && Objects.equals(e2, that.e2)) ||
                    (Objects.equals(e1, that.e2) && Objects.equals(e2, that.e1));
        }

        @Override
        public int hashCode() {
            return e1.hashCode() + e2.hashCode();
        }
    }
}
