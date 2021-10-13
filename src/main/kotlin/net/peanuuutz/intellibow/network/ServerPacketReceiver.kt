package net.peanuuutz.intellibow.network

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.peanuuutz.intellibow.item.IntelliBowItem
import net.peanuuutz.intellibow.util.Constants
import net.peanuuutz.intellibow.util.switchLighterMode

fun registerServerPacketReceivers() {
    ServerPlayNetworking.registerGlobalReceiver(Constants.LIGHTER_MODE_CHANGE) { server, player, _, _, _ ->
        server.execute {
            val bow = when {
                player.mainHandStack.item is IntelliBowItem -> player.mainHandStack
                player.offHandStack.item is IntelliBowItem -> player.offHandStack
                else -> return@execute
            }
            bow.switchLighterMode()
            player.refreshScreenHandler(player.playerScreenHandler)
        }
    }
}