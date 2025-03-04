package mod.arcomit.emberthral.mixin;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreativeModeInventoryScreen.class)
public interface ICreativeInventoryMixin {
    @Accessor("selectedTab")
    static CreativeModeTab getSelectedTab() {
        throw new AssertionError("Mixin accessor未正确应用！");
    }//获取当前选中的创造物品栏
}