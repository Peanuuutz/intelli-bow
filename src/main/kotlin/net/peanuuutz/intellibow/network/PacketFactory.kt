package net.peanuuutz.intellibow.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.world.ClientWorld
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.math.MathHelper
import net.peanuuutz.intellibow.entity.TrackerArrowEntity
import net.peanuuutz.intellibow.util.Constants

object TrackerArrowSpawnS2CPacketFactory {
    fun send(arrowEntity: TrackerArrowEntity) {
        val buf = PacketByteBufs.create().apply {
            writeInt(arrowEntity.entityId)
            writeUuid(arrowEntity.uuid)
            writeDouble(arrowEntity.x)
            writeDouble(arrowEntity.y)
            writeDouble(arrowEntity.z)
            writeInt(MathHelper.floor(arrowEntity.pitch * 256.0f / 360.0f))
            writeInt(MathHelper.floor(arrowEntity.yaw * 256.0f / 360.0f))
            writeInt(arrowEntity.owner?.entityId ?: 0)
        }
        PlayerLookup.tracking(arrowEntity).forEach {
            ServerPlayNetworking.send(it, Constants.TRACKER_ARROW_SPAWN, buf)
        }
    }

    fun createArrow(buf: PacketByteBuf, world: ClientWorld) = TrackerArrowEntity(world = world).apply {
        entityId = buf.readInt()
        uuid = buf.readUuid()
        val x = buf.readDouble()
        val y = buf.readDouble()
        val z = buf.readDouble()
        updatePosition(x, y, z)
        updateTrackedPosition(x, y, z)
        refreshPositionAfterTeleport(x, y, z)
        pitch = buf.readInt() * 360 / 256.0f
        yaw = buf.readInt() * 360 / 256.0f
        owner = world.getEntityById(buf.readInt())
    }
}

object LighterModeChangeC2SPacketFactory {
    fun send() {
        ClientPlayNetworking.send(Constants.LIGHTER_MODE_CHANGE, PacketByteBufs.empty())
    }
}