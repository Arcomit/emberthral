package mod.arcomit.emberthral;

import com.mojang.logging.LogUtils;
import mod.arcomit.emberthral.render.particles.ParticleRegistry;
import mod.arcomit.emberthral.render.particles.pipeline.PostPasses;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

/**
 * @author Arcomit
 */
@SuppressWarnings("removal")
@Mod(Emberthral.MODID)
public class Emberthral {

    public static final String MODID = "emberthral";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Emberthral() {
        LOGGER.info("Emberthral is loaded!");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        ParticleRegistry.register(modEventBus);
        modEventBus.addListener(PostPasses::register);
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(Emberthral.MODID, path);
    }


}
