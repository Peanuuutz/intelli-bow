package net.peanuuutz.intellibow.util

import net.fabricmc.fabric.api.`object`.builder.v1.client.model.FabricModelPredicateProviderRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.client.item.ModelPredicateProvider
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.peanuuutz.intellibow.IntelliBow

fun <T : Item> item(
    item: T
): T = Registry.register(
    Registry.ITEM,
    Identifier(IntelliBow.MOD_ID, (item as IDInjector).id),
    item
)

inline fun settings(
    settings: FabricItemSettings.() -> FabricItemSettings = { this }
): Item.Settings = FabricItemSettings().settings()

inline fun <T : Entity> entity(
    id: String,
    spawnGroup: SpawnGroup,
    provider: EntityType.EntityFactory<T>,
    settings: FabricEntityTypeBuilder<T>.() -> FabricEntityTypeBuilder<T> = { this }
): EntityType<T> = Registry.register(
    Registry.ENTITY_TYPE,
    Identifier(IntelliBow.MOD_ID, id),
    FabricEntityTypeBuilder
        .create(spawnGroup, provider)
        .settings()
        .build()
)

fun model(
    item: Item,
    id: String,
    modelProvider: ModelPredicateProvider
) = FabricModelPredicateProviderRegistry.register(
    item,
    Identifier(id),
    modelProvider
)