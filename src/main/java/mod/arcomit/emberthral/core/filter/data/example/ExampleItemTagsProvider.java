package mod.arcomit.emberthral.core.filter.data.example;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.concurrent.CompletableFuture;

public class ExampleItemTagsProvider extends ItemTagsProvider {
    public ExampleItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, CompletableFuture<TagLookup<Block>> blockTagProvider, String modId, ExistingFileHelper helper) {
        super(output, registries, blockTagProvider, modId, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // 生成物品标签

        tag(ExampleTags.Items.EXAMPLE_ITEM_TAG)
                .add(Blocks.SPAWNER.asItem()) // 添加方块物品(添加物品同理)
                .addOptional(ResourceLocation.fromNamespaceAndPath("nb", "666")); // 添加其它模组物品(无需依赖该模组);

        // 添加所有生成蛋物品
        BuiltInRegistries.ITEM.stream()
                .filter(item -> item instanceof SpawnEggItem)
                .forEach(this.tag(ExampleTags.Items.EGG_TAG)::add);

        // 复制方块标签到物品标签
        copy(ExampleTags.Blocks.EXAMPLE_BLOCK_TAG, ExampleTags.Items.COPY_BLOCK_TAG);
    }

}
