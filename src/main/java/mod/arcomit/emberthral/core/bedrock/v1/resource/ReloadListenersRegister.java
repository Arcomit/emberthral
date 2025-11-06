package mod.arcomit.emberthral.core.bedrock.v1.resource;

import mod.arcomit.emberthral.EmberthralMod;
import mod.arcomit.emberthral.core.bedrock.v1.event.RegisterBedrockAnimationEvent;
import mod.arcomit.emberthral.core.bedrock.v1.event.RegisterBedrockAnimationReloadListenerEvent;
import mod.arcomit.emberthral.core.bedrock.v1.event.RegisterBedrockModelEvent;
import mod.arcomit.emberthral.core.bedrock.v1.event.RegisterBedrockModelReloadListenerEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

public class ReloadListenersRegister {
    @OnlyIn(Dist.CLIENT)
    @EventBusSubscriber(modid = EmberthralMod.MODID, value = Dist.CLIENT)
    public static class BedrockModelClientRegister {
        @SubscribeEvent
        public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
            RegisterBedrockModelEvent event1 = new RegisterBedrockModelEvent(Dist.CLIENT);
            ModLoader.postEvent(event1);
            RegisterBedrockModelReloadListenerEvent event2 = new RegisterBedrockModelReloadListenerEvent();
            ModLoader.postEvent(event2);
            BedrockModelResourceSet.INSTANCE = new BedrockModelResourceSet(event1.getModelRegistry(), event2.getListeners());


            RegisterBedrockAnimationEvent event3 = new RegisterBedrockAnimationEvent(Dist.CLIENT);
            ModLoader.postEvent(event3);
            RegisterBedrockAnimationReloadListenerEvent event4 = new RegisterBedrockAnimationReloadListenerEvent();
            ModLoader.postEvent(event4);
            BedrockAnimationResourceSet.INSTANCE = new BedrockAnimationResourceSet(event3.getAnimationRegistry(), event4.getListeners());


            event.registerReloadListener(BedrockModelResourceSet.INSTANCE);
            event.registerReloadListener(BedrockAnimationResourceSet.INSTANCE);
        }
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @EventBusSubscriber(modid = EmberthralMod.MODID,  value = Dist.DEDICATED_SERVER)
    public static class BedrockModelServerRegister {
        @SubscribeEvent
        public static void onRegisterReloadListener(AddReloadListenerEvent event) {
            RegisterBedrockModelEvent event1 = new RegisterBedrockModelEvent(Dist.DEDICATED_SERVER);
            ModLoader.postEvent(event1);
            RegisterBedrockModelReloadListenerEvent event2 = new RegisterBedrockModelReloadListenerEvent();
            ModLoader.postEvent(event2);
            BedrockModelResourceSet.INSTANCE = new BedrockModelResourceSet(event1.getModelRegistry(), event2.getListeners());


            RegisterBedrockAnimationEvent event3 = new RegisterBedrockAnimationEvent(Dist.DEDICATED_SERVER);
            ModLoader.postEvent(event3);
            RegisterBedrockAnimationReloadListenerEvent event4 = new RegisterBedrockAnimationReloadListenerEvent();
            ModLoader.postEvent(event4);
            BedrockAnimationResourceSet.INSTANCE = new BedrockAnimationResourceSet(event3.getAnimationRegistry(), event4.getListeners());


            event.addListener(BedrockModelResourceSet.INSTANCE);
            event.addListener(BedrockAnimationResourceSet.INSTANCE);
        }
    }
}
