package net.peanuuutz.intellibow.util

import net.minecraft.util.Identifier
import net.peanuuutz.intellibow.IntelliBow

object Constants {
    // Items
    const val RECURSIVE_BOW = "recursive_bow"
    const val COMPOSITE_BOW = "composite_bow"

    const val SCOPE = "scope"
    const val LIGHTER = "lighter"
    const val TRAJECTORY_SIMULATOR = "trajectory_simulator"
    const val PULLING_DEVICE = "pulling_device"
    const val TRACKER = "tracker"

    const val WRENCH = "wrench"

    // Entities
    const val TRACKER_ARROW = "tracker_arrow"

    // Sounds
    const val SCOPE_OPEN = "scope_open"
    const val SCOPE_CLOSE = "scope_close"

    // Tags
    const val MOD_TAG = "IntelliBow"
    const val ID_TAG = "id"

    const val MODULES_TAG = "Modules"
    const val REMOVED_MODULE_TAG = "Removed"
    const val LIGHTER_MODE_TAG = "Flame"

    const val IMITATOR_TAG = "Imitator"
    const val IMITATOR_DATA_TAG = "Data"

    // Recipes
    const val MODULE_TWEAKING = "crafting_special_moduletweaking"

    // Network
    val TRACKER_ARROW_SPAWN = Identifier(IntelliBow.MOD_ID, "tracker_arrow_spawn")
    val LIGHTER_MODE_CHANGE = Identifier(IntelliBow.MOD_ID, "lighter_mode_change")
}