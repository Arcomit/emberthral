package mod.arcomit.emberthral.client.filter.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.arcomit.emberthral.client.filter.FilterManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;

import java.util.HashMap;
import java.util.Map;

//翻页按钮
public class PageButton extends Button {
    public static final ResourceLocation BUTTON_TEXTURE = ResourceLocation.parse("textures/gui/resource_packs.png");

    private final boolean isDown;
    public static final PageState PAGE_STATE = new PageState();

    public PageButton(boolean isDown) {
        super(0, 0, 11, 10, Component.empty(), btn -> {}, DEFAULT_NARRATION);
        this.isDown = isDown;
    }

    @Override
    public void onPress() {
        if (isDown) {
            PAGE_STATE.incrementPage();
        } else {
            PAGE_STATE.decrementPage();
        }
        FilterManager.refreshesButtonsFilter();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        int leftPos = (minecraft.getWindow().getGuiScaledWidth() - 195) / 2;
        int topPos = (minecraft.getWindow().getGuiScaledHeight() - 136) / 2;
        this.x = leftPos - 13;
        this.y = topPos + 9 + (3 * 29) + 21;
        if (isDown) {
            this.y = topPos + 9 + (3 * 29) + 31;
        }


        this.visible = FilterManager.isNeedsToBeFiltered(CreativeModeInventoryScreen.selectedTab);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        int u = isDown ? 83 : 115;
        int v = calculateVOffset();

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        guiGraphics.blit(BUTTON_TEXTURE, getX(), getY(), u, v, 11, 10, 256, 256);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private int calculateVOffset() {
        boolean hovered = isHovered();
        return isDown ?
                (hovered ? 52 : 20) :
                (hovered ? 36 : 4);
    }

    public static class PageState {
        private final Map<CreativeModeTab, Integer> tabPageCache = new HashMap<>();
        private CreativeModeTab currentTab;
        private int currentPage = 1;
        private int pageCount = 1;

        //仅在打开创造物品栏、切换创造物品栏时更新总页码
        public void updatePageCount(CreativeModeTab newTab) {
            if (!FilterManager.isNeedsToBeFiltered(newTab)) return;
            if (currentTab != null) {
                tabPageCache.put(currentTab, currentPage);
            }

            currentTab = newTab;
            currentPage = tabPageCache.getOrDefault(newTab, 1);
            updateTotalPages();
        }

        private void updateTotalPages() {
            int filterCount = FilterManager.filterTabMap.get(currentTab).size();
            pageCount = Math.max((filterCount + 4 - 1) / 4, 1);
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

    //—————————————————————————实例—————————————————————————//
    public static PageButton INSTANCE_0 = new PageButton(false);
    public static PageButton INSTANCE_1 = new PageButton(true);

}