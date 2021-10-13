package net.peanuuutz.intellibow.util

import net.minecraft.item.Item
import net.peanuuutz.intellibow.item.ITEMS

interface IDInjector {
    val id: String
}

fun getItemById(id: String): Item? = ITEMS.find { (it as IDInjector).id == id }