package net.peanuuutz.intellibow.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.peanuuutz.intellibow.item.IntelliBowItem;
import net.peanuuutz.intellibow.sound.IBSoundsKt;
import net.peanuuutz.intellibow.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class MixinPlayerInventory {
    @Shadow @Final public PlayerEntity player;

    private boolean isScopeOpen = false;

    @Inject(method = "scrollInHotbar", at = @At("HEAD"), cancellable = true)
    private void switchScope(double scrollAmount, CallbackInfo ci) { // TODO: implementation
        if (player.getActiveItem().getItem() instanceof IntelliBowItem && hasScope(player.getActiveItem())) {
            if (scrollAmount > 0.0d && !isScopeOpen) {
                player.world.playSound(player, player.getX(), player.getEyeY(), player.getZ(), IBSoundsKt.getSCOPE_OPEN_SOUND(), SoundCategory.PLAYERS, 1.0f, 1.0f);
                isScopeOpen = true;
            } else if (scrollAmount < 0.0d && isScopeOpen) {
                player.world.playSound(player, player.getX(), player.getEyeY(), player.getZ(), IBSoundsKt.getSCOPE_CLOSE_SOUND(), SoundCategory.PLAYERS, 1.0f, 1.0f);
                isScopeOpen = false;
            }
            ci.cancel();
        }
    }

    private boolean hasScope(@NotNull ItemStack stack) {
        return stack.getOrCreateSubTag(Constants.MOD_TAG)
                .getList(Constants.MODULES_TAG, 10)
                .stream()
                .anyMatch(tag -> ((CompoundTag) tag)
                        .getString(Constants.ID_TAG)
                        .equals(Constants.SCOPE));
    }
}
