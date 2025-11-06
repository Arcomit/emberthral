package mod.arcomit.emberthral.core.filter.event.data;

import mod.arcomit.emberthral.EmberthralMod;
import mod.arcomit.emberthral.core.filter.Filter;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

//数据包注册
@EventBusSubscriber(modid = EmberthralMod.MODID)
public class FilterDataRegistryEvent {
    @SubscribeEvent
    public static void onNewRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(Filter.REGISTRY_KEY, Filter.CODEC, Filter.CODEC);
    }
}
