package mod.arcomit.emberthral.client.creativefilter;

import mod.arcomit.emberthral.mixin.ICreativeInventoryMixin;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.*;

// 过滤管理器
public class FilterManager {
    // 缓存创造物品栏对应的过滤器
    private static final Map<CreativeModeTab, List<Filter>> TAB_FILTER_MAP = new LinkedHashMap<>();
    // 缓存当前创造物品栏界面
    public static CreativeModeInventoryScreen currentScreen;

    // 注册过滤器到创造物品栏
    public static void registerTabFilters(CreativeModeTab tab, Filter... filters) {
        List<Filter> filterList = TAB_FILTER_MAP.computeIfAbsent(tab, k -> new ArrayList<>());
        for (Filter filter : filters) {
            // 添加去重检查（如果Filter需要唯一性）
            if (!filterList.contains(filter)) {
                filterList.add(filter);
            }
        }
    }
    // 注册过滤器到创造物品栏(通过资源键获取的创造物品栏)
    public static void registerTabFilters(ResourceKey<CreativeModeTab> tabKey, Filter... filters) {
        FilterManager.registerTabFilters(BuiltInRegistries.CREATIVE_MODE_TAB.get(tabKey), filters);
    }

    // 判断是否为注册过的创造物品栏
    public static boolean tabRequiresFilter(CreativeModeTab tab) {
        return TAB_FILTER_MAP.containsKey(tab);
    }

    // 获取创造物品栏对应的过滤器(没有则返回空集合)
    public static List<Filter> getFiltersForTab(CreativeModeTab tab) {
        return Collections.unmodifiableList(
                TAB_FILTER_MAP.getOrDefault(tab, new ArrayList<>())
        );
    }

    // 刷新当前创造物品栏内的物品列表-仅在【创造物品栏界面打开、切换创造物品栏、开关过滤器】时调用
    public static void refreshesCurrentItems() {
        if (currentScreen == null) return;
        CreativeModeTab currentTab = ICreativeInventoryMixin.getSelectedTab();
        if (currentTab == null || !FilterManager.tabRequiresFilter(currentTab)) return;
        NonNullList<ItemStack> displays_item = currentScreen.getMenu().items;
        displays_item.clear();//清空当前物品栏显示物品
        for (Filter filter : FilterManager.getFiltersForTab(currentTab)) {
            if (filter.isEnable()){
                displays_item.addAll(filter.getFilteredItems());
            }
        }
        currentScreen.getMenu().scrollTo(0.0f);
    }

}
