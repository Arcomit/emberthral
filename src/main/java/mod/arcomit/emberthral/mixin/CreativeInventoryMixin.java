package mod.arcomit.emberthral.mixin;

import mod.arcomit.emberthral.client.creativefilter.FilterManager;
import mod.arcomit.emberthral.client.gui.filter.FilterButton;
import mod.arcomit.emberthral.client.gui.filter.PageButton;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryMixin {
    @Shadow
    private static CreativeModeTab selectedTab;

    @Inject(
            method = "selectTab(Lnet/minecraft/world/item/CreativeModeTab;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$ItemPickerMenu;scrollTo(F)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void onSelectTab(CreativeModeTab tab, CallbackInfo ci) {
        if (FilterManager.tabRequiresFilter(selectedTab)){
            PageButton.PAGE_STATE.updatePageCount(selectedTab);
            FilterButton.refreshAllButtonFilters();
            FilterManager.refreshesCurrentItems();
        }//切换创造物品栏时，如果是需要过滤器功能的创造物品栏则刷新页数、刷新筛选按钮、刷新当前物品栏
    }

}
