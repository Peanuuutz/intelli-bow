package net.peanuuutz.intellibow.registry

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

    // Entities
    const val TRACKER_ARROW = "tracker_arrow"

    // Tags
    const val MOD_TAG = "IntelliBow"
    const val ID_TAG = "id"

    const val MODULES_TAG = "Modules"
    const val MODULE_STATE_TAG = "state"

    const val IMITATOR_TAG = "Imitator"
    const val IMITATOR_DATA_TAG = "Data"

    // Network
    val TRACKER_ARROW_SPAWN = Identifier(IntelliBow.MOD_ID, "tracker_arrow_spawn")
}