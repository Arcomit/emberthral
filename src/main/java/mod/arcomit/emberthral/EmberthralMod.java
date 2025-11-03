package mod.arcomit.emberthral;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-08-06 14:59
 * @Description: 模组主类
 */
@Mod(EmberthralMod.MODID)
public class EmberthralMod {

    public static final String MODID  = "emberthral";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EmberthralMod(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.debug("this is debug");
        LOGGER.info("this is info");
        LOGGER.warn("this is warn");
        LOGGER.error("this is error");
    }

    public static ResourceLocation prefix(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
