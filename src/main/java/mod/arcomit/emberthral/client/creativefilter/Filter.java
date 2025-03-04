package mod.arcomit.emberthral.client.creativefilter;

import mod.arcomit.emberthral.mixin.ICreativeInventoryMixin;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import net.minecraft.world.level.ItemLike;

import java.util.Collection;
import java.util.List;

// 过滤器
public class Filter {
    private final String name;
    private final ItemStack icon;//显示图标

    private boolean isEnable = true;

    private Collection<ItemStack> filteredItems = ItemStackLinkedSet.createTypeAndTagSet();

    public Filter(String name, ItemStack icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return this.name;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    // 切换当前过滤器状态（开启or关闭）
    public void toggleState() {
        CreativeModeTab selectedTab = ICreativeInventoryMixin.getSelectedTab();
        List<Filter> filters = FilterManager.getFiltersForTab(selectedTab);
        boolean shift = Screen.hasShiftDown();
        boolean ctrl = Screen.hasControlDown();
        boolean alt = Screen.hasAltDown();

        if (shift) {
            enableOnlyThisFilter(filters);
        } else if (ctrl) {
            syncAllFiltersToCurrent(filters);
        } else if (alt) {
            invertAllFilters(filters);
        } else {
            toggleCurrentFilter();
        }

        FilterManager.refreshesCurrentItems();
    }

    private void enableOnlyThisFilter(List<Filter> filters) {
        // Shift: 开启当前过滤器并关闭其他(单选)
        filters.forEach(filter -> filter.setEnable(filter == this));
    }

    private void syncAllFiltersToCurrent(List<Filter> filters) {
        // Ctrl: 同步所有过滤器到当前状态(全选)
        boolean newState = !this.isEnable();
        this.setEnable(newState);
        filters.stream()
                .filter(filter -> filter != this)
                .forEach(filter -> filter.setEnable(newState));
    }

    private void invertAllFilters(List<Filter> filters) {
        // Alt: 反选所有过滤器(反选)
        filters.forEach(filter -> filter.setEnable(!filter.isEnable()));
    }

    private void toggleCurrentFilter() {
        // 无修饰键：切换当前过滤器状态
        this.setEnable(!this.isEnable());
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isEnable() {
        return isEnable;
    }

    // 为当前过滤器添加能过滤的物品(单个)
    public void accept(ItemStack stack) {
        if (stack.getCount() != 1) {
            throw new IllegalArgumentException("Stack size must be exactly 1");
        } else {
            boolean flag = this.filteredItems.contains(stack);
            if (flag) {
                throw new IllegalStateException("Accidentally adding the same item stack twice " + stack.getDisplayName().getString());
            } else {
                this.filteredItems.add(stack.copy());
            }
        }
    }

    // 为当前过滤器添加能过滤的物品(单个)
    public void accept(ItemLike item) {
        ItemStack stack = new ItemStack(item);
        this.accept(stack);
    }

    // 为当前过滤器添加能过滤的物品(多个)
    public void accept(ItemStack... stackList) {
        for (ItemStack stack : stackList){
            if (stack.getCount() != 1) {
                throw new IllegalArgumentException("Stack size must be exactly 1");
            } else {
                boolean flag = this.filteredItems.contains(stack);
                if (flag) {
                    throw new IllegalStateException("Accidentally adding the same item stack twice " + stack.getDisplayName().getString());
                } else {
                    this.filteredItems.add(stack.copy());
                }
            }
        }
    }

    // 为当前过滤器添加能过滤的物品(多个)
    public void accept(ItemLike... itemList) {
        for (ItemLike item : itemList){
            this.accept(item);
        }
    }


    // 获取所有能过滤的物品
    public Collection<ItemStack> getFilteredItems() {
        return this.filteredItems;
    }


}
