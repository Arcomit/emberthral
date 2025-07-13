package mod.arcomit.emberthral.data;

import mod.arcomit.emberthral.Emberthral;
import mod.arcomit.emberthral.client.filter.Filter;
import mod.arcomit.emberthral.data.example.ExampleBlockTagsProvider;
import mod.arcomit.emberthral.data.example.ExampleItemTagsProvider;
import mod.arcomit.emberthral.data.example.ExampleTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Emberthral.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        dataGenerator.addProvider(
                event.includeServer(), new DatapackBuiltinEntriesProvider(
                        packOutput,
                        lookupProvider,
                        new RegistrySetBuilder().add(Filter.REGISTRY_KEY,bootstrap -> {

                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    Emberthral.prefix("example")
                            ),new Filter( Emberthral.MODID + ".example", Items.DIAMOND.getDefaultInstance(),
                                    ExampleTags.Items.EXAMPLE_ITEM_TAG.location(),
                                    CreativeModeTabs.SPAWN_EGGS.location()));

                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    Emberthral.prefix("spawn_egg")
                            ),new Filter(Emberthral.MODID + ".spawn_egg", Items.ALLAY_SPAWN_EGG.getDefaultInstance(),
                                    ExampleTags.Items.EGG_TAG.location(),
                                    CreativeModeTabs.SPAWN_EGGS.location()));

                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    Emberthral.prefix("spawn_egg2")
                            ),new Filter(Emberthral.MODID + ".spawn_egg2", Items.ALLAY_SPAWN_EGG.getDefaultInstance(),
                                    ExampleTags.Items.EGG_TAG.location(),
                                    CreativeModeTabs.SPAWN_EGGS.location()));

                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    Emberthral.prefix("spawn_egg3")
                            ),new Filter(Emberthral.MODID + ".spawn_egg3", Items.ALLAY_SPAWN_EGG.getDefaultInstance(),
                                    ExampleTags.Items.EGG_TAG.location(),
                                    CreativeModeTabs.SPAWN_EGGS.location()));

                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    Emberthral.prefix("spawn_egg4")
                            ),new Filter(Emberthral.MODID + ".spawn_egg4", Items.ALLAY_SPAWN_EGG.getDefaultInstance(),
                                    ExampleTags.Items.EGG_TAG.location(),
                                    CreativeModeTabs.SPAWN_EGGS.location()));

                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    Emberthral.prefix("copy")
                            ),new Filter(Emberthral.MODID + ".copy", Items.STONE.getDefaultInstance(),
                                    ExampleTags.Items.COPY_BLOCK_TAG.location(),
                                    CreativeModeTabs.BUILDING_BLOCKS.location()));

                            //使用已有的ItemTag
                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    Emberthral.prefix("egg")
                            ),new Filter(Emberthral.MODID + ".egg", Items.EGG.getDefaultInstance(),
                                    Tags.Items.EGGS.location(),
                                    CreativeModeTabs.INGREDIENTS.location()));
                        }),
                        Set.of(Emberthral.MODID)
                )
        );

        ExampleBlockTagsProvider blockTags = new ExampleBlockTagsProvider(packOutput, lookupProvider, Emberthral.MODID ,existingFileHelper);
        dataGenerator.addProvider(event.includeServer(), blockTags);
        dataGenerator.addProvider(event.includeServer(), new ExampleItemTagsProvider(packOutput, lookupProvider, blockTags.contentsGetter(), Emberthral.MODID ,existingFileHelper));

    }
}
