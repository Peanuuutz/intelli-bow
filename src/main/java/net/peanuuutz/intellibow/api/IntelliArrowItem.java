package net.peanuuutz.intellibow.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public interface IntelliArrowItem {
    ItemStack asItemStack(ItemStack arrow);

    void onHit(ItemStack arrow, LivingEntity target);
}
