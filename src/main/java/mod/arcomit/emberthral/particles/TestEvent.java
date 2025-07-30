package mod.arcomit.emberthral.particles;

import mod.arcomit.emberthral.Emberthral;
import mod.arcomit.emberthral.render.particles.pipeline.PostEffectPipelines;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Emberthral.MODID)
public class TestEvent {
    @SubscribeEvent
    public static void onWorldRender(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;
        PostEffectPipelines.RenderPost();
    }

    @SubscribeEvent
    public static void onWorldRender2(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;
        PostEffectPipelines.active();
    }

}
