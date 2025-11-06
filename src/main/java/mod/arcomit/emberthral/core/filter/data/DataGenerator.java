package mod.arcomit.emberthral.core.filter.data;

import mod.arcomit.emberthral.EmberthralMod;
import mod.arcomit.emberthral.core.filter.Filter;
import mod.arcomit.emberthral.core.filter.data.example.ExampleBlockTagsProvider;
import mod.arcomit.emberthral.core.filter.data.example.ExampleItemTagsProvider;
import mod.arcomit.emberthral.core.filter.data.example.ExampleTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = EmberthralMod.MODID)
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
                        new RegistrySetBuilder().add(Filter.REGISTRY_KEY, bootstrap -> {

                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    EmberthralMod.prefix("example")
                            ),new Filter( EmberthralMod.MODID + ".example", Items.DIAMOND.getDefaultInstance(),
                                    ExampleTags.Items.EXAMPLE_ITEM_TAG.location(),
                                    CreativeModeTabs.SPAWN_EGGS.location()));

                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    EmberthralMod.prefix("spawn_egg")
                            ),new Filter(EmberthralMod.MODID + ".spawn_egg", Items.ALLAY_SPAWN_EGG.getDefaultInstance(),
                                    ExampleTags.Items.EGG_TAG.location(),
                                    CreativeModeTabs.SPAWN_EGGS.location()));

                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    EmberthralMod.prefix("spawn_egg2")
                            ),new Filter(EmberthralMod.MODID + ".spawn_egg2", Items.ALLAY_SPAWN_EGG.getDefaultInstance(),
                                    ExampleTags.Items.EGG_TAG.location(),
                                    CreativeModeTabs.SPAWN_EGGS.location()));

                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    EmberthralMod.prefix("spawn_egg3")
                            ),new Filter(EmberthralMod.MODID + ".spawn_egg3", Items.ALLAY_SPAWN_EGG.getDefaultInstance(),
                                    ExampleTags.Items.EGG_TAG.location(),
                                    CreativeModeTabs.SPAWN_EGGS.location()));

                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    EmberthralMod.prefix("spawn_egg4")
                            ),new Filter(EmberthralMod.MODID + ".spawn_egg4", Items.ALLAY_SPAWN_EGG.getDefaultInstance(),
                                    ExampleTags.Items.EGG_TAG.location(),
                                    CreativeModeTabs.SPAWN_EGGS.location()));

                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    EmberthralMod.prefix("copy")
                            ),new Filter(EmberthralMod.MODID + ".copy", Items.STONE.getDefaultInstance(),
                                    ExampleTags.Items.COPY_BLOCK_TAG.location(),
                                    CreativeModeTabs.BUILDING_BLOCKS.location()));

                            //使用已有的ItemTag
                            bootstrap.register(ResourceKey.create(Filter.REGISTRY_KEY,
                                    EmberthralMod.prefix("egg")
                            ),new Filter(EmberthralMod.MODID + ".egg", Items.EGG.getDefaultInstance(),
                                    Tags.Items.EGGS.location(),
                                    CreativeModeTabs.INGREDIENTS.location()));
                        }),
                        Set.of(EmberthralMod.MODID)
                )
        );

        ExampleBlockTagsProvider blockTags = new ExampleBlockTagsProvider(packOutput, lookupProvider, EmberthralMod.MODID ,existingFileHelper);
        dataGenerator.addProvider(event.includeServer(), blockTags);
        dataGenerator.addProvider(event.includeServer(), new ExampleItemTagsProvider(packOutput, lookupProvider, blockTags.contentsGetter(), EmberthralMod.MODID ,existingFileHelper));

    }
}
