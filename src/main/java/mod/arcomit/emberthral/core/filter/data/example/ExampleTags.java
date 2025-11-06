package mod.arcomit.emberthral.core.filter.data.example;

import mod.arcomit.emberthral.EmberthralMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ExampleTags {
    public static class Items
    {
        public static final TagKey<Item> EXAMPLE_ITEM_TAG =
                TagKey.create(Registries.ITEM, EmberthralMod.prefix("example_items"));

        public static final TagKey<Item> EGG_TAG =
                TagKey.create(Registries.ITEM, EmberthralMod.prefix("egg"));

        public static final TagKey<Item> COPY_BLOCK_TAG =
                TagKey.create(Registries.ITEM, EmberthralMod.prefix("copy_block"));
    }

    public static class Blocks
    {
        public static final TagKey<Block> EXAMPLE_BLOCK_TAG =
                TagKey.create(Registries.BLOCK, EmberthralMod.prefix("example_blocks"));
    }
}
