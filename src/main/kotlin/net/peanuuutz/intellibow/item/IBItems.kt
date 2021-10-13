package net.peanuuutz.intellibow.item

import net.minecraft.item.Item
import net.peanuuutz.intellibow.item.module.*
import net.peanuuutz.intellibow.util.item
import net.peanuuutz.intellibow.util.model

val ITEMS = listOf<Item>(
    RecurveBowItem, CompositeBowItem,
    ScopeItem, LighterItem, TrajectorySimulatorItem, PullingDeviceItem, TrackerItem,
    WrenchItem
)

fun registerItems() {
    ITEMS.forEach(::item)
}

fun registerItemModels() {
    arrayOf(RecurveBowItem, CompositeBowItem).forEach {
        model(it, "pulling") { stack, _, livingEntity ->
            if (livingEntity != null && livingEntity.activeItem == stack && livingEntity.isUsingItem) {
                1.0f
            } else {
                0.0f
            }
        }
        model(it, "pull") { stack, _, livingEntity ->
            if (livingEntity != null && livingEntity.activeItem == stack) {
                (stack.item as IntelliBowItem).getPullProgress(stack, livingEntity.itemUseTime)
            } else {
                0.0f
            }
        }
    }
}