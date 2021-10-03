package net.peanuuutz.intellibow.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.peanuuutz.intellibow.registry.Constants

fun registerClientPacketReceivers() {
    ClientPlayNetworking.registerGlobalReceiver(Constants.TRACKER_ARROW_SPAWN) { client, _, buf, _ ->
        val arrowEntity = TrackerArrowSpawnPacketFactory.receive(buf, client.world!!)
        client.execute { client.world!!.addEntity(arrowEntity.entityId, arrowEntity) }
    }
}