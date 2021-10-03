package net.peanuuutz.intellibow.util

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag

fun CompoundTag.getCompoundOrCreate(key: String): CompoundTag {
    return if (getType(key) == 10.toByte()) {
        getCompound(key)
    } else {
        CompoundTag().also { put(key, it) }
    }
}

fun CompoundTag.getListOrCreate(key: String, elementType: Int): ListTag {
    return if (getType(key) == 9.toByte()) {
        getList(key, elementType)
    } else {
        ListTag().also { put(key, it) }
    }
}