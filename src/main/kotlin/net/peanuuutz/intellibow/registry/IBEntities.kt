package net.peanuuutz.intellibow.registry

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.SpawnGroup
import net.peanuuutz.intellibow.entity.trackerarrow.TrackerArrowEntity
import net.peanuuutz.intellibow.entity.trackerarrow.TrackerArrowEntityRenderer
import net.peanuuutz.intellibow.util.entity

val TRACKER_ARROW_ENTITY by lazy {
    entity(
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

fun registerEntities() {
    TRACKER_ARROW_ENTITY
}

fun registerEntityRenderers() {
    EntityRendererRegistry.INSTANCE.register(TRACKER_ARROW_ENTITY) { _, _ -> TrackerArrowEntityRenderer }
}