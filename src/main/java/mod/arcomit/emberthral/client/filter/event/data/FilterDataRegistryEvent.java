package mod.arcomit.emberthral.client.filter.event.data;

import mod.arcomit.emberthral.Emberthral;
import mod.arcomit.emberthral.client.filter.Filter;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;

//数据包注册
@Mod.EventBusSubscriber(modid = Emberthral.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class FilterDataRegistryEvent {
    @SubscribeEvent
    public static void onNewRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(Filter.REGISTRY_KEY, Filter.CODEC, Filter.CODEC);
    }
}
