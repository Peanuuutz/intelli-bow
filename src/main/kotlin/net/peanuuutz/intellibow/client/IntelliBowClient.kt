package net.peanuuutz.intellibow.client

import net.fabricmc.api.ClientModInitializer
import net.peanuuutz.intellibow.network.registerClientPacketReceivers
import net.peanuuutz.intellibow.registry.registerEntityRenderers
import net.peanuuutz.intellibow.registry.registerItemModels

object IntelliBowClient : ClientModInitializer {
    override fun onInitializeClient() {
        registerItemModels()
        registerEntityRenderers()
        registerClientPacketReceivers()
    }
}

