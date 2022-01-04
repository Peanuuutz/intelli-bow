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
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.*
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.world.World
import net.peanuuutz.intellibow.IntelliBow
import net.peanuuutz.intellibow.api.IntelliArrowItem
import net.peanuuutz.intellibow.util.Constants
import net.peanuuutz.intellibow.util.IDInjector
import net.peanuuutz.intellibow.util.entityType
import net.peanuuutz.intellibow.util.getCompoundOrCreate

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
        dataTracker[IMITATOR] = imitator.copy()
    }

    override fun tick() { // TODO: implementation
        super.tick()
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

    override fun asItemStack(): ItemStack {
        val ammo = dataTracker[IMITATOR]
        val arrowItem = ammo.item.asIntelliArrowItem()
        return arrowItem.asItemStack(ammo)
    }

    override fun onHit(target: LivingEntity) {
        super.onHit(target)
        val ammo = dataTracker[IMITATOR]
        val arrowItem = ammo.item.asIntelliArrowItem()
        arrowItem.onHit(ammo, target)
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

    private fun Item.asIntelliArrowItem() = this as? IntelliArrowItem ?: Items.ARROW as IntelliArrowItem

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