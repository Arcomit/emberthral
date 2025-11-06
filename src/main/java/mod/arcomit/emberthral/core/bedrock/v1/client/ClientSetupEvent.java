package mod.arcomit.emberthral.core.bedrock.v1.client;

import mod.arcomit.emberthral.EmberthralMod;
import mod.arcomit.emberthral.core.bedrock.v1.client.compat.sodium.SodiumCompat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = EmberthralMod.MODID)
public class ClientSetupEvent {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        SodiumCompat.init();
    }
}