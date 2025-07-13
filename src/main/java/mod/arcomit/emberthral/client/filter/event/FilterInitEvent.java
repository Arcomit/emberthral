package mod.arcomit.emberthral.client.filter.event;

import mod.arcomit.emberthral.Emberthral;
import mod.arcomit.emberthral.client.filter.Filter;
import mod.arcomit.emberthral.client.filter.FilterManager;
import mod.arcomit.emberthral.client.filter.gui.FilterButton;
import mod.arcomit.emberthral.client.filter.gui.PageButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedHashSet;

//初始化过滤器相关内容
@Mod.EventBusSubscriber(modid = Emberthral.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FilterInitEvent {
    public static boolean init = false;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onGuiInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof CreativeModeInventoryScreen screen)) return;

        //初始化，加载过滤器能过滤的物品，并将过滤器与创造物品栏绑定
        if (!init){
            RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
            registryAccess.registry(Filter.REGISTRY_KEY).get().forEach(filter -> {
                CreativeModeTab tab = filter.getBeFilteredTab();
                if (tab == null) {
                    Emberthral.LOGGER.warn("Filter：[{}] has no tab to be filtered!", registryAccess.registryOrThrow(Filter.REGISTRY_KEY).getResourceKey(filter).get().location());
                    return;
                }
                filter.loadItems();
                if (!FilterManager.filterTabMap.containsKey(tab)){
                    LinkedHashSet<Filter> filters = new LinkedHashSet<>();
                    filters.add(filter);
                    FilterManager.filterTabMap.put(tab, filters);
                }else{
                    FilterManager.filterTabMap.get(tab).add(filter);
                }
            });
            init = true;
        }

        FilterManager.creativeScreen = screen;
        FilterManager.refreshesPageCount();
        FilterManager.refreshesButtonsFilter();
        FilterManager.refreshesCurrentItems();

        //添加按钮
        event.addListener(FilterButton.INSTANCE_0);
        event.addListener(FilterButton.INSTANCE_1);
        event.addListener(FilterButton.INSTANCE_2);
        event.addListener(FilterButton.INSTANCE_3);
        event.addListener(PageButton.INSTANCE_0);
        event.addListener(PageButton.INSTANCE_1);

    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        init = false;
        FilterManager.filterTabMap.clear();
    }
}
