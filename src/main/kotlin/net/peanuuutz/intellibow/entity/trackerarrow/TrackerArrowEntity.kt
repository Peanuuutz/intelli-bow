package net.peanuuutz.intellibow.entity.trackerarrow

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.*
import net.minecraft.nbt.CompoundTag
import net.minecraft.potion.PotionUtil
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import net.peanuuutz.intellibow.registry.Constants
import net.peanuuutz.intellibow.registry.TRACKER_ARROW_ENTITY
import net.peanuuutz.intellibow.util.IDInjector
import net.peanuuutz.intellibow.util.getCompoundOrCreate
import kotlin.math.max

class TrackerArrowEntity(
    type: EntityType<TrackerArrowEntity> = TRACKER_ARROW_ENTITY,
    world: World
) : PersistentProjectileEntity(type, world), IDInjector {
    override val id = Constants.TRACKER_ARROW

    constructor(world: World, imitator: ItemStack, owner: LivingEntity) : this(world = world) {
        updatePosition(owner.x, owner.eyeY - 0.1, owner.z)
        setOwner(owner)
        if (owner is PlayerEntity) {
            pickupType = PickupPermission.ALLOWED
        }
        if (imitator.item != Items.ARROW) {
            dataTracker.set(IMITATOR, imitator)
        }
    }

    override fun tick() {

    }

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(IMITATOR, ItemStack(Items.ARROW))
    }

    override fun asItemStack(): ItemStack = dataTracker[IMITATOR] // Why it's protected??

    override fun onHit(target: LivingEntity) { // Why it's protected??
        super.onHit(target)
        val ammo = dataTracker[IMITATOR]
        when (ammo.item) {
            is SpectralArrowItem -> {
                target.addStatusEffect(StatusEffectInstance(StatusEffects.GLOWING, 200))
            }
            is TippedArrowItem -> {
                PotionUtil.getPotion(ammo).effects.forEach {
                    target.addStatusEffect(StatusEffectInstance(
                        it.effectType,
                        max(1, it.duration / 8),
                        it.amplifier,
                        it.isAmbient,
                        it.shouldShowParticles()
                    ))
                }
                PotionUtil.getCustomPotionEffects(ammo)
                    .map(::StatusEffectInstance)
                    .forEach(target::addStatusEffect)
            }
        }
    }

    override fun writeCustomDataToTag(tag: CompoundTag) { // Too lazy to write DSL
        super.writeCustomDataToTag(tag)
        tag.put(Constants.MOD_TAG, CompoundTag().apply {
            put(Constants.IMITATOR_TAG, CompoundTag().apply {
                val ammo = dataTracker[IMITATOR]
                putString(Constants.ID_TAG, Registry.ITEM.getId(ammo.item).toString())
                if (ammo.tag?.isEmpty == false) {
                    put(Constants.IMITATOR_DATA_TAG, ammo.tag)
                }
            })
        })
    }

    override fun readCustomDataFromTag(tag: CompoundTag) {
        super.readCustomDataFromTag(tag)
        if (!tag.getCompoundOrCreate(Constants.MOD_TAG).isEmpty) {
            val imitatorTag = tag.getCompound(Constants.MOD_TAG).getCompoundOrCreate(Constants.IMITATOR_TAG)
            val id = Identifier.tryParse(imitatorTag.getString(Constants.ID_TAG))
            val item = if (id != null) {
                Registry.ITEM[id]
            } else {
                Items.ARROW
            }
            dataTracker.set(IMITATOR, ItemStack(item).apply {
                val data = imitatorTag.getCompound(Constants.IMITATOR_DATA_TAG)
                if (!data.isEmpty) {
                    this.tag = data
                }
            })
        }
    }

    companion object {
        private val IMITATOR = DataTracker.registerData(TrackerArrowEntity::class.java, TrackedDataHandlerRegistry.ITEM_STACK)
    }
}