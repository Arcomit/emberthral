package mod.arcomit.emberthral.client.filter;

import mod.arcomit.emberthral.client.filter.gui.FilterButton;
import mod.arcomit.emberthral.client.filter.gui.PageButton;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class FilterManager {
    public static HashMap<CreativeModeTab, LinkedHashSet<Filter>> filterTabMap = new HashMap<>();
    //创造物品栏窗口
    public static CreativeModeInventoryScreen creativeScreen;

    public static boolean isNeedsToBeFiltered(CreativeModeTab tab) {
        return filterTabMap.containsKey(tab);
    }

    // 刷新当前页数-仅在【创造物品栏界面打开、切换创造物品栏】时调用
    public static void refreshesPageCount(){
        PageButton.PAGE_STATE.updatePageCount(CreativeModeInventoryScreen.selectedTab);
    }

    // 刷新过滤器按钮-仅在【创造物品栏界面打开、切换创造物品栏、翻页按钮按下】时调用
    public static void refreshesButtonsFilter() {
        FilterButton.INSTANCE_0.refreshFilter();
        FilterButton.INSTANCE_1.refreshFilter();
        FilterButton.INSTANCE_2.refreshFilter();
        FilterButton.INSTANCE_3.refreshFilter();
    }

    // 刷新当前创造物品栏内的物品列表-仅在【创造物品栏界面打开、切换创造物品栏、开关过滤器】时调用
    public static void refreshesCurrentItems() {
        if (creativeScreen == null) return;
        CreativeModeTab currentTab = CreativeModeInventoryScreen.selectedTab;
        if (currentTab == null || !isNeedsToBeFiltered(currentTab)) return;
        NonNullList<ItemStack> displays_item = creativeScreen.getMenu().items;
        boolean isEnableFilter = false;
        Collection<ItemStack> filteredItems = ItemStackLinkedSet.createTypeAndTagSet();
        for (Filter filter : filterTabMap.get(currentTab)) {
            if (filter.isEnable()){
                filteredItems.addAll(filter.getFilteredItems());
                isEnableFilter = true;
            }
        }
        displays_item.clear();
        if (isEnableFilter) {
            displays_item.addAll(filteredItems);
        }else if (currentTab.hasSearchBar()){
            creativeScreen.refreshSearchResults();
        }else {
            displays_item.addAll(currentTab.getDisplayItems());
        }
        creativeScreen.scrollOffs = 0.0f;
        creativeScreen.getMenu().scrollTo(0.0f);
    }
}
