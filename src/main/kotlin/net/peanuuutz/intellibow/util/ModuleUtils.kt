package net.peanuuutz.intellibow.util

import net.minecraft.item.ItemStack
import net.minecraft.nbt.*
import net.peanuuutz.intellibow.item.IntelliBowItem
import net.peanuuutz.intellibow.item.module.LighterItem
import net.peanuuutz.intellibow.item.module.ModuleItem
import net.peanuuutz.intellibow.registry.Constants

val ItemStack.modules: ListTag
    get() = getOrCreateSubTag(Constants.MOD_TAG)!!.getListOrCreate(Constants.MODULES_TAG, 10)

val Tag.id: String
    get() = (this as CompoundTag).getString(Constants.ID_TAG)

fun ItemStack.findModule(module: ModuleItem) = modules.find { it.id == module.id } as? CompoundTag

fun ItemStack.addModule(module: ModuleItem): Boolean {
    val modules = this.modules
    if (modules.size < (item as IntelliBowItem).moduleCapacity) {
        if (modules.find { it.id == module.id } == null) {
            return CompoundTag().apply {
                put(Constants.ID_TAG, StringTag.of(module.id))
                if (module == LighterItem) {
                    put(Constants.MODULE_STATE_TAG, IntTag.of(0))
                }
            }.let(modules::add)
        }
    }
    return false
}

fun ItemStack.removeModule(id: String) = modules.removeIf { it.id == id }

fun ItemStack.switchLighterMode() {
    findModule(LighterItem)?.let {
        it.putInt(Constants.MODULE_STATE_TAG, 1 - it.getInt(Constants.MODULE_STATE_TAG))
    }
}
