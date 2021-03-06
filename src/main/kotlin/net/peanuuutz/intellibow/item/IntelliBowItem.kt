package net.peanuuutz.intellibow.item

import net.minecraft.util.math.MathHelper.clamp
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.resource.language.I18n
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ArrowItem
import net.minecraft.item.BowItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.world.World
import net.peanuuutz.intellibow.IntelliBow
import net.peanuuutz.intellibow.entity.TrackerArrowEntity
import net.peanuuutz.intellibow.item.module.LighterItem
import net.peanuuutz.intellibow.item.module.PullingDeviceItem
import net.peanuuutz.intellibow.item.module.TrackerItem
import net.peanuuutz.intellibow.network.TrackerArrowSpawnS2CPacketFactory
import net.peanuuutz.intellibow.util.*

abstract class IntelliBowItem(settings: Settings) : BowItem(settings), IDInjector {
    open val baseSpeedModifier = 0.0f
    open val baseDamageModifier = 0.0
    open val moduleCapacity = 0
    open val maxModuleTier = 0

    private val pullingDeviceSpeedModifier = IntelliBow.config.moduleAttributes.pullingDeviceSpeedModifier

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity?, remainingUseTicks: Int) {
        if (user is PlayerEntity) {
            val isCreative = user.isCreative
            val infinity = EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0
            val ammo = user.getArrowType(stack)
            if (!ammo.isEmpty) {
                val pullProgress = getPullProgress(stack, getMaxUseTime(stack) - remainingUseTicks)
                if (pullProgress > 0.1f) {
                    val noConsume = (isCreative || infinity) && ammo.item == Items.ARROW
                    if (!world.isClient) {
                        val lighter = stack.findModule(LighterItem)?.getInt(Constants.LIGHTER_MODE_TAG)?.equals(1) == true
                        val arrow = (if (ammo.item is ArrowItem) ammo.item else Items.ARROW) as ArrowItem
                        val arrowEntity = if (stack.findModule(TrackerItem) != null) {
                            TrackerArrowEntity(world, ammo, user)
                        } else {
                            arrow.createArrow(world, ammo, user)
                        }
                        with(arrowEntity) {
                            setProperties(
                                user, user.pitch, user.yaw,
                                0.0f, 3.0f * pullProgress, 1.0f
                            )
                            if (pullProgress == 1.0f) {
                                isCritical = true
                            }
                            val power = EnchantmentHelper.getLevel(Enchantments.POWER, stack)
                            if (power > 0) {
                                damage += (power + 1) * 0.5
                            }
                            damage += baseDamageModifier
                            val punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack)
                            if (punch > 0) {
                                setPunch(punch)
                            }
                            if (lighter || EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0
                            ) {
                                setOnFireFor(100)
                            }
                            if (noConsume ||
                                isCreative && (ammo.item == Items.SPECTRAL_ARROW || ammo.item == Items.TIPPED_ARROW)
                            ) {
                                pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY
                            }
                        }
                        world.spawnEntity(arrowEntity)
                        if (arrowEntity is TrackerArrowEntity) {
                            TrackerArrowSpawnS2CPacketFactory.send(arrowEntity)
                        }
                        stack.damage(if (lighter) 2 else 1, user) { it.sendToolBreakStatus(user.activeHand) }
                    }
                    world.playSound(
                        user.x, user.y, user.z,
                        SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS,
                        1.0f, 1.0f / (RANDOM.nextFloat() * 0.4f + 1.2f) + pullProgress * 0.5f,
                        false
                    )
                    if (!noConsume && !isCreative) {
                        ammo.decrement(1)
                        if (ammo.isEmpty) {
                            user.inventory.removeOne(ammo)
                        }
                    }
                    user.incrementStat(Stats.USED.getOrCreateStat(this))
                }
            }
        }
    }

    fun getPullProgress(stack: ItemStack, useTicks: Int): Float {
        var speedModifier = baseSpeedModifier
        if (stack.findModule(PullingDeviceItem) != null) {
            speedModifier += pullingDeviceSpeedModifier
        }
        val pullTime = clamp(useTicks / 20.0f * (1 + speedModifier), 0.0f, 1.0f)
        return pullTime * (2.0f - pullTime)
    }

    override fun onCraft(stack: ItemStack, world: World?, player: PlayerEntity?) {
        stack.modules
        if (player != null) {
            val removedModuleId = stack.getSubTag(Constants.MOD_TAG)!!.let {
                val temp = it.getString(Constants.REMOVED_MODULE_TAG)
                it.remove(Constants.REMOVED_MODULE_TAG)
                temp
            }
            if (removedModuleId.isNotEmpty()) {
                val module = getItemById(removedModuleId)?.defaultStack ?: return
                player.giveItemStack(module)
            }
        }
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        val modulesByName = stack.modules.joinToString { I18n.translate("item.intellibow.${it.id}") }
        if (modulesByName.isNotEmpty()) {
            tooltip.add(TranslatableText("item.intellibow.intellibow.tooltip", modulesByName)
                .formatted(Formatting.DARK_GREEN, Formatting.ITALIC))
        }
    }
}