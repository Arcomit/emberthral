package mod.arcomit.emberthral;

import com.mojang.logging.LogUtils;
import mod.arcomit.emberthral.client.creativefilter.Filter;
import mod.arcomit.emberthral.client.creativefilter.FilterManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(Emberthral.MODID)
public class Emberthral {

    public static final String MODID = "emberthral";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Emberthral() {
        LOGGER.info("Emberthral is loaded!");
        //示例-仅在开发环境生效
        if (FMLEnvironment.production) {
            Filter test = new Filter("test",new ItemStack(Items.IRON_INGOT));
            test.accept(Items.IRON_INGOT);

            Filter test2 = new Filter("test2",new ItemStack(Items.IRON_INGOT));

            Filter test3 = new Filter("test3",new ItemStack(Items.GOLD_INGOT));
            test3.accept(Items.GOLD_INGOT);

            Filter test4 = new Filter("test4",new ItemStack(Items.DIAMOND));
            test4.accept(Items.DIAMOND);

            Filter test5 = new Filter("test5",new ItemStack(Items.MAGENTA_BED));
            test5.accept(Blocks.MAGENTA_BED);

            FilterManager.registerTabFilters(CreativeModeTabs.BUILDING_BLOCKS,test,test2,test3,test4,test5);
        }
    }
}
