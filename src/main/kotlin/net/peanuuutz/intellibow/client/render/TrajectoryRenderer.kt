package net.peanuuutz.intellibow.client.render

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.DustParticleEffect
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.peanuuutz.intellibow.item.IntelliBowItem
import net.peanuuutz.intellibow.item.module.TrajectorySimulatorItem
import net.peanuuutz.intellibow.util.findModule
import net.minecraft.util.math.MathHelper.*
import net.peanuuutz.intellibow.IntelliBow
import kotlin.math.PI

object TrajectoryRenderer {
    private const val degree = PI.toFloat() / 180.0f

    private val range = IntelliBow.config.moduleAttributes.trajectorySimulatorRange

    fun register() {
        WorldRenderEvents.LAST.register { it.run {
            if (!camera().isThirdPerson) {
                val player = MinecraftClient.getInstance().player ?: return@run
                val bow = when {
                    player.mainHandStack.item is IntelliBowItem -> player.mainHandStack
                    player.offHandStack.item is IntelliBowItem -> player.offHandStack
                    else -> return@run
                }
                if (player.activeItem.item is IntelliBowItem) {
                    if (bow.findModule(TrajectorySimulatorItem) != null) {
                        val pullProgress = (bow.item as IntelliBowItem).getPullProgress(bow, player.itemUseTime)
                        if (pullProgress >= 0.1f) {
                            renderCurve(matrixStack(), tickDelta(), world(), player, pullProgress)
                        }
                    }
                }
            }
        } }
    }

    private fun renderCurve(
        matrixStack: MatrixStack,
        tickDelta: Float,
        world: World,
        player: PlayerEntity,
        pullProgress: Float
    ) { // TODO: implementation
        val vertices = calculateVertices(world, player, pullProgress)
        vertices.forEach { world.addParticle(DustParticleEffect(1.0f, 1.0f, 1.0f, 0.5f), true, it.x.toDouble(), it.y.toDouble(), it.z.toDouble(), 0.0, 0.0, 0.0) }
    }

    private fun calculateVertices(
        world: World,
        player: PlayerEntity,
        pullProgress: Float
    ): List<Vector3f> {
        val pitch = player.pitch
        val yaw = player.yaw
        val tickMovement = Vector3f(
            -cos(pitch * degree) * sin(yaw * degree),
            -sin(pitch * degree),
            cos(pitch * degree) * cos(yaw * degree)
        ).apply {
            normalize()
            scale(pullProgress * 3.0f)
        }
        var currentVertex = Vector3f(player.x.toFloat(), (player.eyeY - 0.1).toFloat(), player.z.toFloat())
        var currentBlockPos = currentVertex.toBlockPos()
        val vertices = arrayListOf<Vector3f>()
        while (world.isAir(currentBlockPos) && currentBlockPos.isWithinDistance(player.pos, range.toDouble())) {
            vertices.add(currentVertex)
            currentVertex = currentVertex.copy().apply { add(tickMovement) }
            currentBlockPos = currentVertex.toBlockPos()
            tickMovement.add(0.0f, -0.05f, 0.0f)
        }
        return vertices
    }

    private fun Vector3f.toBlockPos() = BlockPos(x.toInt(), y.toInt(), z.toInt())
}