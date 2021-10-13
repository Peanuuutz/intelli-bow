package net.peanuuutz.intellibow.client

import net.fabricmc.api.ClientModInitializer
import net.peanuuutz.intellibow.client.render.TrajectoryRenderer
import net.peanuuutz.intellibow.entity.TrackerArrowEntityRenderer
import net.peanuuutz.intellibow.network.registerClientPacketReceivers
import net.peanuuutz.intellibow.item.registerItemModels

object IntelliBowClient : ClientModInitializer {
    override fun onInitializeClient() {
        registerKeyBindings()
        registerItemModels()
        TrackerArrowEntityRenderer
        TrajectoryRenderer.register()
        registerClientPacketReceivers()
    }
}

