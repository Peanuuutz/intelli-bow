package net.peanuuutz.intellibow.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.peanuuutz.intellibow.api.IntelliArrowItem;
import net.peanuuutz.intellibow.entity.TrackerArrowEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArrowItem.class)
public class MixinArrowItem implements IntelliArrowItem {
    @Override
    public ItemStack asItemStack(ItemStack arrow) {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public void onHit(ItemStack arrow, LivingEntity target) {
    }
}
