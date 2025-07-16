package mod.arcomit.emberthral.example.data;

import mod.arcomit.emberthral.Emberthral;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class ExampleTags {
    public static class Items
    {
        public static final TagKey<Item> EXAMPLE_ITEM_TAG =
                TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), Emberthral.prefix("example_items"));

        public static final TagKey<Item> EGG_TAG =
                TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), Emberthral.prefix("egg"));

        public static final TagKey<Item> COPY_BLOCK_TAG =
                TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), Emberthral.prefix("copy_block"));
    }

    public static class Blocks
    {
        public static final TagKey<Block> EXAMPLE_BLOCK_TAG =
                TagKey.create(ForgeRegistries.BLOCKS.getRegistryKey(), Emberthral.prefix("example_blocks"));
    }
}
