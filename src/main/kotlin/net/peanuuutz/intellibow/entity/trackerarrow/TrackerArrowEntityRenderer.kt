package net.peanuuutz.intellibow.entity.trackerarrow

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.entity.ProjectileEntityRenderer
import net.minecraft.util.Identifier
import net.peanuuutz.intellibow.IntelliBow
import net.peanuuutz.intellibow.registry.Constants

object TrackerArrowEntityRenderer : ProjectileEntityRenderer<TrackerArrowEntity>(MinecraftClient.getInstance().entityRenderDispatcher) {
    override fun getTexture(entity: TrackerArrowEntity?) = Identifier(IntelliBow.MOD_ID, "textures/entity/${Constants.TRACKER_ARROW}.png")
}