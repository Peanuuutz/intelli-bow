package net.peanuuutz.intellibow

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.peanuuutz.intellibow.config.IBConfigProvider
import net.peanuuutz.intellibow.item.CompositeBowItem
import net.peanuuutz.intellibow.registry.ITEMS
import net.peanuuutz.intellibow.registry.Constants
import net.peanuuutz.intellibow.registry.registerEntities
import net.peanuuutz.intellibow.registry.registerItems
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object IntelliBow : ModInitializer {
    const val MOD_ID = "intellibow"

    private val LOGGER: Logger = LogManager.getLogger(Constants.MOD_TAG)

    val ITEM_GROUP: ItemGroup = FabricItemGroupBuilder.create(Identifier(MOD_ID, "general"))
        .icon { CompositeBowItem.defaultStack }
        .appendItems { it.addAll(ITEMS.map { item ->
            item.defaultStack.apply {
                item.onCraft(this, null, null)
            }
        }) }
        .build()

    val config by lazy {
        IBConfigProvider.load()
    }

    override fun onInitialize() {
        config
        registerItems()
        registerEntities()

        LOGGER.info("Mod loaded")
    }
}