package mod.arcomit.emberthral.particles;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import mod.arcomit.emberthral.Emberthral;
import mod.arcomit.emberthral.render.particles.pipeline.ParticleEngineHelper;
import mod.arcomit.emberthral.render.particles.pipeline.PostEffectPipelines;
import mod.arcomit.emberthral.render.particles.pipeline.PostParticleRenderType;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;


@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Emberthral.MODID)
public class ClientTickEvent {
    public static final PriorityQueue<ParticleEngineHelper.PostParticles> emberthral$renderQueue = ParticleEngineHelper.createQueue();


    @SubscribeEvent
    public static void onWorldRender(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

        PostEffectPipelines.active();

        if(!PostEffectPipelines.isActive()) return;

        Frustum clippingHelper = event.getFrustum();
        Camera camera = event.getCamera();
        float partialTick = event.getPartialTick();
        PoseStack posestack_ = event.getPoseStack();
        Minecraft minecraft = Minecraft.getInstance();
        TextureManager textureManager = minecraft.getTextureManager();
        LightTexture lightTexture = minecraft.gameRenderer.lightTexture();
        ParticleEngine particleEngine = minecraft.particleEngine;

        // 添加我们的粒子渲染批次
        lightTexture.turnOnLightLayer();
        RenderSystem.enableDepthTest();
        RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE2);
        RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE0);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.mulPoseMatrix(posestack_.last().pose());
        RenderSystem.applyModelViewMatrix();

        emberthral$renderQueue.clear();
        Iterator<?> rendertypes = particleEngine.particles.keySet().iterator();
        while (rendertypes.hasNext()){
            if(rendertypes.next() instanceof PostParticleRenderType pprt){
                emberthral$renderQueue.add(new ParticleEngineHelper.PostParticles(pprt, particleEngine.particles.get(pprt)));
            }
        }

        ParticleEngineHelper.PostParticles queueItem;
        PostParticleRenderType particlerendertype;
        while (!emberthral$renderQueue.isEmpty()) {
            queueItem = emberthral$renderQueue.poll();
            particlerendertype = queueItem.rt();

            //System.out.println("Now Render:"+particlerendertype.getClass().getName());

            RenderSystem.setShader(GameRenderer::getParticleShader);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            particlerendertype.begin(bufferbuilder, textureManager);
            var particleIterator = queueItem.particles().iterator();


            Particle particle;
            while (particleIterator.hasNext()) {
                particle = particleIterator.next();
                if (clippingHelper == null
                        || !particle.shouldCull()
                        || clippingHelper.isVisible(particle.getBoundingBox())) {
                    try {
                        particlerendertype.callPipeline();
                        particle.render(bufferbuilder, camera, partialTick);
                    } catch (Throwable var18) {
                        CrashReport crashreport = CrashReport.forThrowable(var18, "Rendering Particle" );
                        CrashReportCategory crashreportcategory = crashreport.addCategory("Particle being rendered" );
                        Objects.requireNonNull(particle);
                        crashreportcategory.setDetail("Particle", particle::toString);
                        Objects.requireNonNull(particlerendertype);
                        crashreportcategory.setDetail("Particle Type", particlerendertype::toString);
                        throw new ReportedException(crashreport);
                    }
                }
            }
            particlerendertype.end(tesselator);
        }

        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        lightTexture.turnOffLightLayer();
    }
}