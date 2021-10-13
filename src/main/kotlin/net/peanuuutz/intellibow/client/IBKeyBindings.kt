package net.peanuuutz.intellibow.client

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.options.KeyBinding
import net.peanuuutz.intellibow.item.IntelliBowItem
import net.peanuuutz.intellibow.item.module.LighterItem
import net.peanuuutz.intellibow.network.LighterModeChangeC2SPacketFactory
import net.peanuuutz.intellibow.util.findModule
import net.peanuuutz.intellibow.util.keyBinding
import org.lwjgl.glfw.GLFW

val TOGGLE_LIGHTER_KEY: KeyBinding by lazy {
    keyBinding(
        translationKey = "toggle_lighter",
        key = GLFW.GLFW_KEY_C,
        category = "general"
    )
}

fun registerKeyBindings() {
    TOGGLE_LIGHTER_KEY
    ClientTickEvents.END_CLIENT_TICK.register { client ->
        val bow = when {
            client.player?.mainHandStack?.item is IntelliBowItem -> client.player!!.mainHandStack
            client.player?.offHandStack?.item is IntelliBowItem -> client.player!!.offHandStack
            else -> return@register
        }
        if (bow.findModule(LighterItem) != null) {
            while (TOGGLE_LIGHTER_KEY.wasPressed()) {
                LighterModeChangeC2SPacketFactory.send()
            }
        }
    }
}