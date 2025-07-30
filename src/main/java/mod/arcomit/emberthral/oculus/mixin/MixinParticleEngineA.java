package mod.arcomit.emberthral.oculus.mixin;

import com.google.common.collect.Sets;
import mod.arcomit.emberthral.render.particles.pipeline.PostParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.Queue;
import java.util.Set;

// 不要让原版粒子引擎渲染我们
@Mixin(ParticleEngine.class)
public abstract class MixinParticleEngineA {
    @Redirect(
            method = {"render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V"},
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;keySet()Ljava/util/Set;"
            ),
            remap = false
    )
    private Set<ParticleRenderType> epicacg$selectParticlesToRender(Map<ParticleRenderType, Queue<Particle>> instance) {
        Set<ParticleRenderType> keySet = instance.keySet();
        return Sets.filter(keySet, (type) -> !(type instanceof PostParticleRenderType));
    }
}
