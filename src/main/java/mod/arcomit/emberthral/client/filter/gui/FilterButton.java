package mod.arcomit.emberthral.client.filter.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import mod.arcomit.emberthral.client.filter.Filter;
import mod.arcomit.emberthral.client.filter.FilterManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

// 过滤器按钮
public class FilterButton extends Button {

    // 其实是顺序，0-3，对应4个按钮
    private final int ID;
    // 当前按钮对应的过滤器
    private Filter currentFilter;
    // 按钮贴图
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("textures/gui/slider.png");

    public FilterButton(int width, int height, int buttonID) {
        super(0, 0, width, height, Component.empty(), button -> {
        }, DEFAULT_NARRATION);
        this.ID = buttonID;
    }

    @Override
    public void onPress() {
        //按下后切换过滤器状态
        boolean shift = Screen.hasShiftDown();
        boolean ctrl = Screen.hasControlDown();
        boolean alt = Screen.hasAltDown();

        LinkedHashSet<Filter> filters = FilterManager.filterTabMap.get(CreativeModeInventoryScreen.selectedTab);
        if (shift) {
            filters.forEach(filter -> filter.setSwitch(filter == this.currentFilter));
        } else if (ctrl) {
            this.currentFilter.toggleState();
            filters.stream().filter(filter -> filter != this.currentFilter).forEach(filter -> filter.setSwitch(this.currentFilter.isEnable()));
        } else if (alt) {
            filters.forEach(filter -> filter.setSwitch(!filter.isEnable()));
        } else {
            currentFilter.toggleState();
        }

        FilterManager.refreshesCurrentItems();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        int leftPos = (minecraft.getWindow().getGuiScaledWidth() - 195) / 2;
        int topPos = (minecraft.getWindow().getGuiScaledHeight() - 136) / 2;
        this.x = leftPos - 22;
        this.y = topPos + 9 + (ID * 29);

        this.visible = currentFilter != null && FilterManager.isNeedsToBeFiltered(CreativeModeInventoryScreen.selectedTab);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean isHovered = this.isHovered();

        // 渲染按钮背景
        renderBackgroud(guiGraphics);

        // 别动这行，这行是玄学代码，没它会出现文字渲染黑色的问题
        this.renderString(guiGraphics, Minecraft.getInstance().font, this.getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);

        // 渲染物品图标(含阴影)
        renderItemWithShadow(guiGraphics);

        // 鼠标悬停时显示提示
        if (isHovered) {
            renderTooltip(guiGraphics, mouseX, mouseY);
        }
    }

    private void renderBackgroud(GuiGraphics guiGraphics) {
        // 配置渲染状态
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        // 渲染按钮背景
        guiGraphics.blitNineSliced(BUTTON_TEXTURE,
                this.getX(), this.getY(),
                this.getWidth(), this.getHeight(),
                20, 4, 200, 20,
                0, getTextureY(isHovered)
        );
    }

    //0-1按下，2-3未按下。
    private int getTextureY(boolean isHovered) {
        return (currentFilter.isEnable()
                ? (isHovered ? 1 : 0)
                : (isHovered ? 3 : 2)) * 20;
    }

    private void renderItemWithShadow(GuiGraphics guiGraphics) {
        final int ICON_SIZE = 16;
        final int POS_X = this.getX() + (this.width - ICON_SIZE) / 2;
        final int POS_Y = this.getY() + (this.height - ICON_SIZE) / 2 - 1;

        // 阴影渲染
        guiGraphics.setColor(0.0F, 0.0F, 0.0F, 0.5F);
        guiGraphics.renderItem(currentFilter.getIcon(), POS_X + 1, POS_Y + 1);

        // 恢复颜色并渲染本体
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.renderItem(currentFilter.getIcon(), POS_X, POS_Y);
    }

    private static final List<Component> tooltip = new ArrayList<>();

    private void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Minecraft minecraft = Minecraft.getInstance();

        tooltip.add(Component.translatable("filter." + currentFilter.getName()));
        boolean shift = Screen.hasShiftDown();
        boolean ctrl = Screen.hasControlDown();
        boolean alt = Screen.hasAltDown();

        if (shift) {
            tooltip.add(Component.translatable("filter.emberthral.tips.shift"));
        } else if (ctrl) {
            tooltip.add(Component.translatable("filter.emberthral.tips.ctrl"));
        } else if (alt) {
            tooltip.add(Component.translatable("filter.emberthral.tips.alt"));
        }

        guiGraphics.renderComponentTooltip(minecraft.font,
                tooltip,
                mouseX, mouseY);
        tooltip.clear();
    }

    public void refreshFilter() {
        LinkedHashSet<Filter> filters = FilterManager.filterTabMap.getOrDefault(CreativeModeInventoryScreen.selectedTab, null);
        this.currentFilter = safeGet(filters, (PageButton.PAGE_STATE.getCurrentPage() - 1) * 4 + ID);
    }

    public static Filter safeGet(LinkedHashSet<Filter> filter, int index) {
        if (filter == null) return null;
        List<Filter> list = new ArrayList<>(filter);
        if (index < 0 || index >= list.size()) return null;
        return list.get(index);
    }

//—————————————————————————实例—————————————————————————//
        public static FilterButton INSTANCE_0 = new FilterButton(20, 20, 0);
        public static FilterButton INSTANCE_1 = new FilterButton(20, 20, 1);
        public static FilterButton INSTANCE_2 = new FilterButton(20, 20, 2);
        public static FilterButton INSTANCE_3 = new FilterButton(20, 20, 3);

}