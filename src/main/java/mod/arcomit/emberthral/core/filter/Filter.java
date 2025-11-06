package mod.arcomit.emberthral.core.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.arcomit.emberthral.EmberthralMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;

public class Filter {
    public static final Codec<Filter> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("name","")
                            .forGetter(f -> f.name),
                    ItemStack.CODEC.optionalFieldOf("icon", ItemStack.EMPTY)
                            .forGetter(f -> f.icon),
                    ResourceLocation.CODEC.fieldOf("filterTagKey")
                            .forGetter(fc -> fc.filterTag.location()),
                    ResourceLocation.CODEC.fieldOf("beFilteredTabId")
                            .forGetter(f -> f.beFilteredTabId)
            ).apply(instance, Filter::new)
    );
    public static final ResourceKey<Registry<Filter>> REGISTRY_KEY =
            ResourceKey.createRegistryKey(EmberthralMod.prefix("filter"));

    // 过滤器的名字，图标，过滤标签，被过滤的创造物品栏
    private final String name;
    private final ItemStack icon;
    private final TagKey<Item> filterTag;
    private final ResourceLocation beFilteredTabId;
    //过滤后的物品列表
    private ArrayList<ItemStack> items = new ArrayList<ItemStack>();

    public Filter(String name, ItemStack icon, ResourceLocation tagLocation, ResourceLocation beFilteredTabID){
        this.name = name;
        this.icon = icon;
        this.filterTag = TagKey.create(Registries.ITEM, tagLocation);
        //this.filterTag = TagKey.create(Registries.ITEM, tagLocation);
        this.beFilteredTabId = beFilteredTabID;
    }

    public void loadItems()
    {
        if (getBeFilteredTab() == null) return;
        this.items.clear();
        for (ItemStack itemstack : getBeFilteredTab().getDisplayItems()){
            if(itemstack.is(filterTag)){
                this.items.add(itemstack);
                System.out.println(itemstack);
            }
        }
    }

    private boolean isEnable = false;

    public boolean isEnable() {
        return isEnable;
    }

    public void setSwitch(boolean enable) {
        isEnable = enable;
    }
    public void enable() {
        isEnable = true;
    }
    public void disable() {
        isEnable = false;
    }
    public void toggleState() {
        isEnable = !isEnable;

    }

    public String getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public CreativeModeTab getBeFilteredTab() {
        if (BuiltInRegistries.CREATIVE_MODE_TAB.containsKey(beFilteredTabId)) {
            return BuiltInRegistries.CREATIVE_MODE_TAB.get(beFilteredTabId);
        }
        return null;
    }

    public ArrayList<ItemStack> getFilteredItems() {
        return items;
    }

}
