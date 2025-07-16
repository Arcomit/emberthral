package mod.arcomit.emberthral.render.particles;

import com.google.common.collect.Maps;
import mod.arcomit.emberthral.Emberthral;
import mod.arcomit.emberthral.render.particles.custom.BloomParticleRenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class EMBERTHRALRenderType {
    private static int bloomIdx = 0;
    public static final HashMap<ResourceLocation, BloomParticleRenderType> BloomRenderTypes = Maps.newHashMap();
    public static BloomParticleRenderType getBloomRenderTypeByTexture(ResourceLocation texture){
        if(BloomRenderTypes.containsKey(texture)){
            return BloomRenderTypes.get(texture);
        } else {
            BloomParticleRenderType bloomType = new BloomParticleRenderType(Emberthral.prefix("bp_" + bloomIdx++), texture);
            BloomRenderTypes.put(texture, bloomType);
            return bloomType;
        }
    }
}
