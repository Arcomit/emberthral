package mod.arcomit.emberthral.util;

import mod.arcomit.emberthral.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentHelper {
    public static boolean isCompat(Enchantment instance, Enchantment arg, boolean originalResult) {
        boolean checkCompat = originalResult;
        if (!checkCompat) {
            for (String enchantments : Config.alwaysCompatible) {
                Enchantment enchantment1 = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantments.split(";")[0]));
                Enchantment enchantment2 = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantments.split(";")[1]));
                if (enchantment1 == null || enchantment2 == null) continue;
                if (enchantment1.equals(arg) && enchantment2.equals(instance)) {
                    checkCompat = true;
                    break;
                }
            }
        } else {
            for (String enchantments : Config.alwaysIncompatible) {
                Enchantment enchantment1 = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantments.split(";")[0]));
                Enchantment enchantment2 = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantments.split(";")[1]));
                if (enchantment1 == null || enchantment2 == null) continue;
                if (enchantment1.equals(arg) && enchantment2.equals(instance)) {
                    checkCompat = false;
                    break;
                }
            }
        }
        return checkCompat;
    }
}
