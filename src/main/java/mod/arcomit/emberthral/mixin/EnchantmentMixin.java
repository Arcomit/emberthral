package mod.arcomit.emberthral.mixin;

import mod.arcomit.emberthral.util.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @Inject(method = "isCompatibleWith", at = @At("RETURN"), cancellable = true)
    private void onCheckCompat(Enchantment arg, @NotNull CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantmentHelper.isCompat((Enchantment) (Object)this, arg, cir.getReturnValue()));
    }
}