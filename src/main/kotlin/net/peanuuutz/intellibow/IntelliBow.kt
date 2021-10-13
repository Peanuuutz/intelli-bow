package net.peanuuutz.intellibow

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.util.Identifier
import net.peanuuutz.intellibow.config.IBConfigProvider
import net.peanuuutz.intellibow.entity.TrackerArrowEntity
import net.peanuuutz.intellibow.item.CompositeBowItem
import net.peanuuutz.intellibow.item.ITEMS
import net.peanuuutz.intellibow.item.registerItems
import net.peanuuutz.intellibow.network.registerServerPacketReceivers
import net.peanuuutz.intellibow.recipe.ModuleTweakingRecipe
import net.peanuuutz.intellibow.sound.registerSounds
import net.peanuuutz.intellibow.util.Constants
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object IntelliBow : ModInitializer {
    const val MOD_ID = "intellibow"

    val LOGGER: Logger = LogManager.getLogger(Constants.MOD_TAG)

    val config = IBConfigProvider.load()

    init {
        FabricItemGroupBuilder.create(Identifier(MOD_ID, "general"))
            .icon { CompositeBowItem.defaultStack }
            .appendItems { it.addAll(ITEMS.map { item ->
                item.defaultStack.apply {
                    item.onCraft(this, null, null)
                }
            }) }
            .build()
    }

    override fun onInitialize() {
        registerItems()
        registerSounds()
        TrackerArrowEntity
        ModuleTweakingRecipe
        registerServerPacketReceivers()

        LOGGER.info("Mod loaded")
    }
}