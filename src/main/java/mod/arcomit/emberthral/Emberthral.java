package mod.arcomit.emberthral;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
        //示例-仅在开发环境生效
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(Emberthral.MODID, path);
    }
}
