package mod.arcomit.emberthral.client.event.filter;

import mod.arcomit.emberthral.client.creativefilter.Filter;
import mod.arcomit.emberthral.client.creativefilter.FilterManager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static mod.arcomit.emberthral.Emberthral.MODID;

//自动将过滤器中的物品添加到对应的创造物品栏中
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTabHandler {
    @SubscribeEvent
    public static void onCreativeTabBuild(BuildCreativeModeTabContentsEvent event) {
        if (FilterManager.tabRequiresFilter(event.getTab())){
            for (Filter filter : FilterManager.getFiltersForTab(event.getTab())){
                for (ItemStack stack : filter.getFilteredItems()){
                    if (stack != null){
                        event.accept(stack);
                    }
                }
            }
        }
    }
}
