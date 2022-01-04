package net.peanuuutz.intellibow.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TippedArrowItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.peanuuutz.intellibow.api.IntelliArrowItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TippedArrowItem.class)
public class MixinTippedArrowItem implements IntelliArrowItem {
    @Override
    public ItemStack asItemStack(ItemStack arrow) {
        return arrow;
    }

    @Override
    public void onHit(ItemStack arrow, LivingEntity target) {
        PotionUtil.getPotion(arrow).getEffects().forEach(effect -> {
            target.addStatusEffect(new StatusEffectInstance(
                    effect.getEffectType(),
                    Math.max(1, effect.getDuration() / 8),
                    effect.getAmplifier(),
                    effect.isAmbient(),
                    effect.shouldShowParticles()
            ));
        });
        PotionUtil.getCustomPotionEffects(arrow).stream()
                .map(StatusEffectInstance::new)
                .forEach(target::addStatusEffect);
    }
}
