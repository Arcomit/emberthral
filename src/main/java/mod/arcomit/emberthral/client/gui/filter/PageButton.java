package mod.arcomit.emberthral.client.gui.filter;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.arcomit.emberthral.client.creativefilter.FilterManager;
import mod.arcomit.emberthral.mixin.ICreativeInventoryMixin;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;

import java.util.HashMap;
import java.util.Map;

public class PageButton extends Button {
    public static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("textures/gui/resource_packs.png");
    private static final int TEXTURE_WIDTH = 256;  // 纹理图整体宽度
    private static final int TEXTURE_HEIGHT = 256; // 纹理图整体高度

    // 箭头UV坐标常量（单位：像素）
    private static final int DOWN_ARROW_U = 83;    // 向下箭头在纹理图中的水平起始位置
    private static final int UP_ARROW_U = 115;     // 向上箭头在纹理图中的水平起始位置
    private static final int HOVERED_DOWN_V = 52;  // 悬停状态下向下箭头的垂直起始位置
    private static final int UNHOVERED_DOWN_V = 20;// 普通状态下向下箭头的垂直起始位置
    private static final int HOVERED_UP_V = 36;     // 悬停状态下向上箭头的垂直起始位置
    private static final int UNHOVERED_UP_V = 4;   // 普通状态下向上箭头的垂直起始位置

    // 分页系统常量
    private static final int DEFAULT_PAGE = 1;     // 默认/初始页码（从1开始）
    private static final int FILTERS_PER_PAGE = 4; // 每页最大显示的过滤器数量

    private final boolean isDown;
    public static final PageState PAGE_STATE = new PageState();

    public PageButton(int x, int y, boolean isDown) {
        super(x, y, 11, 10, Component.empty(), btn -> {}, DEFAULT_NARRATION);
        this.isDown = isDown;
    }

    @Override
    public void onPress() {
        if (isDown) {
            PAGE_STATE.incrementPage();
        } else {
            PAGE_STATE.decrementPage();
        }
        FilterButton.refreshAllButtonFilters();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.visible = FilterManager.tabRequiresFilter(ICreativeInventoryMixin.getSelectedTab());
        if (!visible) return;

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        int u = isDown ? DOWN_ARROW_U : UP_ARROW_U;
        int v = calculateVOffset();

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        guiGraphics.blit(BUTTON_TEXTURE, getX(), getY(), u, v, 11, 10, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private int calculateVOffset() {
        boolean hovered = isHovered();
        return isDown ?
                (hovered ? HOVERED_DOWN_V : UNHOVERED_DOWN_V) :
                (hovered ? HOVERED_UP_V : UNHOVERED_UP_V);
    }

    public static class PageState {
        private final Map<CreativeModeTab, Integer> tabPageCache = new HashMap<>();
        private CreativeModeTab currentTab;
        private int currentPage = DEFAULT_PAGE;
        private int pageCount = DEFAULT_PAGE;

        //仅在打开创造物品栏、切换创造物品栏时更新总页码
        public void updatePageCount(CreativeModeTab newTab) {
            if (currentTab != null) {
                tabPageCache.put(currentTab, currentPage);
            }

            currentTab = newTab;
            currentPage = tabPageCache.getOrDefault(newTab, DEFAULT_PAGE);
            updateTotalPages();
        }

        private void updateTotalPages() {
            int filterCount = FilterManager.getFiltersForTab(currentTab).size();
            pageCount = Math.max((filterCount + FILTERS_PER_PAGE - 1) / FILTERS_PER_PAGE, 1);
        }

        public void decrementPage() {
            currentPage = Mth.clamp(currentPage - 1, 1, pageCount);
        }

        public void incrementPage() {
            currentPage = Mth.clamp(currentPage + 1, 1, pageCount);
        }

        public int getCurrentPage() {
            return currentPage;
        }
    }
}