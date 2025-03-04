package mod.arcomit.emberthral.client.event.filter;

import mod.arcomit.emberthral.client.creativefilter.FilterManager;
import mod.arcomit.emberthral.client.gui.filter.FilterButton;
import mod.arcomit.emberthral.client.gui.filter.PageButton;
import mod.arcomit.emberthral.mixin.ICreativeInventoryMixin;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "emberthral", bus = Mod.EventBusSubscriber.Bus.FORGE ,value = Dist.CLIENT)
public class AddFilterEvent {
    @SubscribeEvent
    public static void onGuiInit(ScreenEvent.Init.Post event) {
        if(!(event.getScreen() instanceof CreativeModeInventoryScreen screen)) return;
        PageButton.PAGE_STATE.updatePageCount(ICreativeInventoryMixin.getSelectedTab());
        FilterButton.refreshAllButtonFilters();
        FilterManager.currentScreen = screen;
        FilterManager.refreshesCurrentItems();

        for (int i = 0; i <= 3; i++) {
            AbstractWidget button = new FilterButton(
                    screen.getGuiLeft() - 22, // 离创造物品栏左侧2格宽
                    screen.getGuiTop() + 9 + (i * 29),
                    20, 20,
                    i
            );
            event.addListener(button);
        }

        AbstractWidget upButton = new PageButton(
                screen.getGuiLeft() - 13,// 离创造物品栏左侧2格宽
                screen.getGuiTop() + 9 + (3 * 29) + 21,// 过滤类型按钮下方一格
                false);
        AbstractWidget downButton = new PageButton(
                screen.getGuiLeft() - 13,// 离创造物品栏左侧2格宽
                screen.getGuiTop() + 9 + (3 * 29) + 31,
                true);

        event.addListener(upButton);
        event.addListener(downButton);

    }
}
