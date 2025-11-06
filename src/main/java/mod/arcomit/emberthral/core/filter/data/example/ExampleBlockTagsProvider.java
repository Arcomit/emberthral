package mod.arcomit.emberthral.core.filter.data.example;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ExampleBlockTagsProvider extends net.neoforged.neoforge.common.data.BlockTagsProvider {
    public ExampleBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable net.neoforged.neoforge.common.data.ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ExampleTags.Blocks.EXAMPLE_BLOCK_TAG)
                .add(Blocks.STONE);
    }
}
