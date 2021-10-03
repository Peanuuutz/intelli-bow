package net.peanuuutz.intellibow.item.module

import net.minecraft.item.Item
import net.peanuuutz.intellibow.util.IDInjector

abstract class ModuleItem(settings: Settings) : Item(settings), IDInjector {
    abstract val tier: Int
}