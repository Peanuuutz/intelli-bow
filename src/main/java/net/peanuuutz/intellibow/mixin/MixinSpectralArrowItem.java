package net.peanuuutz.intellibow.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpectralArrowItem;
import net.minecraft.nbt.CompoundTag;
import net.peanuuutz.intellibow.api.IntelliArrowItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpectralArrowItem.class)
public class MixinSpectralArrowItem implements IntelliArrowItem {
    @Override
    public ItemStack asItemStack(ItemStack arrow) {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }

    @Override
    public void onHit(ItemStack arrow, LivingEntity target) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200));
    }
}
