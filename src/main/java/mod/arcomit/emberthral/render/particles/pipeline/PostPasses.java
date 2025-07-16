package mod.arcomit.emberthral.render.particles.pipeline;

import mod.arcomit.emberthral.render.particles.shaderpasses.*;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class PostPasses {

    public static PostPassBase blit;
    public static DepthCull depth_cull;

    public static DownSampling downSampler;
    public static UpSampling upSampler;
    public static UnityComposite unity_composite;

    public static void register(RegisterShadersEvent event){
        try {
            System.out.println("Load Shader");
            ResourceManager rm = Minecraft.getInstance().getResourceManager();
            blit = new PostPassBase("emberthral:blit",rm);
            depth_cull = new DepthCull("emberthral:depth_cull", rm);
            downSampler = new DownSampling("emberthral:down_sampling",rm);
            upSampler = new UpSampling("emberthral:up_sampling",rm);
            unity_composite = new UnityComposite("emberthral:unity_composite",rm);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
