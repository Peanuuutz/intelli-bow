package net.peanuuutz.intellibow.entity

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.entity.ProjectileEntityRenderer
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
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
import net.peanuuutz.intellibow.IntelliBow
import net.peanuuutz.intellibow.util.Constants
import net.peanuuutz.intellibow.util.IDInjector
import net.peanuuutz.intellibow.util.entityType
import net.peanuuutz.intellibow.util.getCompoundOrCreate
import kotlin.math.max

class TrackerArrowEntity(
    type: EntityType<TrackerArrowEntity> = ENTITY_TYPE,
    world: World
) : PersistentProjectileEntity(type, world), IDInjector {
    override val id = Constants.TRACKER_ARROW

    constructor(world: World, imitator: ItemStack, owner: LivingEntity) : this(world = world) {
        updatePosition(owner.x, owner.eyeY - 0.1, owner.z)
        setOwner(owner)
        if (owner is PlayerEntity) {
            pickupType = PickupPermission.ALLOWED
        }
        dataTracker[IMITATOR] = imitator
    }

    override fun tick() {
        super.tick() // TODO: implementation
    }

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(IMITATOR, ItemStack.EMPTY)
    }

    override fun onTrackedDataSet(data: TrackedData<*>) {
        super.onTrackedDataSet(data)
        if (IMITATOR == data) {
            dataTracker[IMITATOR].holder = this
        }
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

    override fun writeCustomDataToTag(tag: CompoundTag) {
        super.writeCustomDataToTag(tag)
        tag.put(Constants.MOD_TAG, CompoundTag().apply {
            put(Constants.IMITATOR_TAG, dataTracker[IMITATOR].toTag(CompoundTag()))
        })
    }

    override fun readCustomDataFromTag(tag: CompoundTag) {
        super.readCustomDataFromTag(tag)
        val rootTag = tag.getCompoundOrCreate(Constants.MOD_TAG)
        if (!rootTag.isEmpty) {
            val imitatorTag = rootTag.getCompoundOrCreate(Constants.IMITATOR_TAG)
            dataTracker[IMITATOR] = if (!imitatorTag.isEmpty) {
                ItemStack.fromTag(imitatorTag)
            } else {
                ItemStack(Items.ARROW)
            }
        }
    }

    companion object {
        private val IMITATOR = DataTracker.registerData(TrackerArrowEntity::class.java, TrackedDataHandlerRegistry.ITEM_STACK)

        val ENTITY_TYPE: EntityType<TrackerArrowEntity> = entityType(
            id = Constants.TRACKER_ARROW,
            spawnGroup = SpawnGroup.MISC,
            provider = ::TrackerArrowEntity
        ) {
            disableSummon()
            dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            trackRangeChunks(4)
            trackedUpdateRate(20)
        }
    }
}

object TrackerArrowEntityRenderer : ProjectileEntityRenderer<TrackerArrowEntity>(MinecraftClient.getInstance().entityRenderDispatcher) {
    init {
        EntityRendererRegistry.INSTANCE.register(TrackerArrowEntity.ENTITY_TYPE) { _, _ -> TrackerArrowEntityRenderer }
    }

    override fun getTexture(entity: TrackerArrowEntity?) = Identifier(IntelliBow.MOD_ID, "textures/entity/${Constants.TRACKER_ARROW}.png")
}