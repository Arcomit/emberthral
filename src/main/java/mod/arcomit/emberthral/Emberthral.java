package mod.arcomit.emberthral;

import com.mojang.logging.LogUtils;
import mod.arcomit.emberthral.client.creativefilter.Filter;
import mod.arcomit.emberthral.client.creativefilter.FilterManager;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(Emberthral.MODID)
public class Emberthral {

    public static final String MODID = "emberthral";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Emberthral() {
        LOGGER.info("Emberthral is loaded!");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        //示例-仅在开发环境生效
        if (!FMLEnvironment.production) {
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

            FilterManager.registerTabFilters(CreativeModeTabs.FOOD_AND_DRINKS,test,test2,test3,test4,test5);

            Filter test6 = new Filter("test",new ItemStack(Items.IRON_INGOT));
            test6.accept(Items.IRON_INGOT);

            Filter test7 = new Filter("test2",new ItemStack(Items.IRON_INGOT));

            Filter test8 = new Filter("test3",new ItemStack(Items.GOLD_INGOT));
            test8.accept(Items.GOLD_INGOT);

            Filter test9 = new Filter("test4",new ItemStack(Items.DIAMOND));
            test9.accept(Items.DIAMOND);

            Filter test10 = new Filter("test5",new ItemStack(Items.MAGENTA_BED));
            test10.accept(Blocks.MAGENTA_BED);
            FilterManager.registerTabFilters(CreativeModeTabs.FUNCTIONAL_BLOCKS,test6,test7,test8,test9,test10);
        }
    }
}
