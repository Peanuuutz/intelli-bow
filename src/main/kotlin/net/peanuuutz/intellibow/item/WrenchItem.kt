package net.peanuuutz.intellibow.item

import net.minecraft.item.Item
import net.peanuuutz.intellibow.util.Constants
import net.peanuuutz.intellibow.util.IDInjector
import net.peanuuutz.intellibow.util.settings

object WrenchItem : IDInjector, Item(settings {
    maxCount(1)
}) {
    override val id = Constants.WRENCH
}