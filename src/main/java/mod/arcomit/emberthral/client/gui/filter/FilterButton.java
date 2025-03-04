package mod.arcomit.emberthral.client.gui.filter;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.arcomit.emberthral.client.creativefilter.Filter;
import mod.arcomit.emberthral.client.creativefilter.FilterManager;
import mod.arcomit.emberthral.mixin.ICreativeInventoryMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;

import java.util.ArrayList;
import java.util.List;

public class FilterButton extends Button {
    private final int ID;
    // 当前按钮对应的过滤器
    private Filter currentFilter = null;
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("textures/gui/slider.png");
    private static boolean needRefresh = false;

    public FilterButton(int x, int y, int width, int height, int buttonID) {
        super(x, y, width, height, Component.empty(), button -> {}, DEFAULT_NARRATION);
        this.ID = buttonID;
    }

    @Override
    public void onPress() {
        //按下后切换过滤器状态
        if (currentFilter != null){
            currentFilter.toggleState();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (needRefresh){
            refreshFilter();
        }

        boolean shouldHide = (currentFilter == null) || !FilterManager.tabRequiresFilter(ICreativeInventoryMixin.getSelectedTab());
        this.visible = !shouldHide;
        if(!this.visible) return;

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    // 刷新，用于更新当前对应的过滤器
    public void refreshFilter(){
        CreativeModeTab currentTab = ICreativeInventoryMixin.getSelectedTab();
        this.currentFilter = safeGet(FilterManager.getFiltersForTab(currentTab), (PageButton.PAGE_STATE.getCurrentPage() - 1) * 4 + ID);
        if (ID == 3) {
            needRefresh = false;
        }//如果刷新到最后一个按钮，则停止本轮刷新
    }

    // 如果列表为空、索引越界，返回 null
    public static <T> T safeGet(List<T> list, int index) {
        if (list == null || index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    // 刷新所有过滤器按钮
    public static void refreshAllButtonFilters(){
        needRefresh = true;
    }


    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean isHovered = this.isHovered();

        // 渲染按钮背景
        renderBackgroud(guiGraphics);

        // 别动这行，这行是玄学代码，没它会出现文字渲染黑色的问题
        int i = getFGColor();
        this.renderString(guiGraphics, Minecraft.getInstance().font, i | Mth.ceil(this.alpha * 255.0F) << 24);

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

        tooltip.add(Component.translatable("filter.negorerouse." + currentFilter.getName()));
        boolean shift = Screen.hasShiftDown();
        boolean ctrl = Screen.hasControlDown();
        boolean alt = Screen.hasAltDown();

        if (shift) {
            tooltip.add(Component.translatable("filter.negorerouse.tips.shift"));
        } else if (ctrl) {
            tooltip.add(Component.translatable("filter.negorerouse.tips.ctrl"));
        } else if (alt) {
            tooltip.add(Component.translatable("filter.negorerouse.tips.alt"));
        }

        guiGraphics.renderComponentTooltip(minecraft.font,
                tooltip,
                mouseX, mouseY);
        tooltip.clear();
    }

}
