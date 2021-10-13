package net.peanuuutz.intellibow.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.peanuuutz.intellibow.util.Constants

fun registerClientPacketReceivers() {
    ClientPlayNetworking.registerGlobalReceiver(Constants.TRACKER_ARROW_SPAWN) { client, _, buf, _ ->
        val arrowEntity = TrackerArrowSpawnS2CPacketFactory.createArrow(buf, client.world!!)
        client.execute { client.world!!.addEntity(arrowEntity.entityId, arrowEntity) }
    }
}