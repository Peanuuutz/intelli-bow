package net.peanuuutz.intellibow.util

import net.minecraft.item.ItemStack
import net.minecraft.nbt.*
import net.peanuuutz.intellibow.item.IntelliBowItem
import net.peanuuutz.intellibow.item.module.LighterItem
import net.peanuuutz.intellibow.item.module.ModuleItem

val ItemStack.modules: ListTag
    get() = if (item is IntelliBowItem) {
            getOrCreateSubTag(Constants.MOD_TAG)!!.getListOrCreate(Constants.MODULES_TAG, 10)
        } else {
            throw IllegalArgumentException("Cannot have modules on non-Intelli-Bow items")
        }

val Tag.id: String
    get() = (this as CompoundTag).getString(Constants.ID_TAG)

fun ItemStack.findModule(module: ModuleItem) = modules.find { it.id == module.id } as? CompoundTag

fun ItemStack.addModule(module: ModuleItem) {
    modules.add(CompoundTag().apply {
        put(Constants.ID_TAG, StringTag.of(module.id))
        if (module == LighterItem) {
            putBoolean(Constants.LIGHTER_MODE_TAG, false)
        }
    })
}

fun ItemStack.removeModule(): String? {
    val modules = this.modules
    return if (modules.size > 0) {
        modules.removeAt(0).id
    } else {
        null
    }
}

fun ItemStack.switchLighterMode() {
    findModule(LighterItem)?.let {
        it.putBoolean(Constants.LIGHTER_MODE_TAG, !it.getBoolean(Constants.LIGHTER_MODE_TAG))
    }
}
