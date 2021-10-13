package net.peanuuutz.intellibow.item.module

import net.minecraft.util.Rarity
import net.peanuuutz.intellibow.util.Constants
import net.peanuuutz.intellibow.util.settings

object ScopeItem : ModuleItem(settings()) {
    override val id = Constants.SCOPE
    override val tier = 1
}

object LighterItem : ModuleItem(settings {
    rarity(Rarity.UNCOMMON)
}) {
    override val id = Constants.LIGHTER
    override val tier = 2
}

object TrajectorySimulatorItem : ModuleItem(settings {
    rarity(Rarity.UNCOMMON)
}) {
    override val id = Constants.TRAJECTORY_SIMULATOR
    override val tier = 2
}

object PullingDeviceItem : ModuleItem(settings {
    rarity(Rarity.RARE)
}) {
    override val id = Constants.PULLING_DEVICE
    override val tier = 3
}

object TrackerItem : ModuleItem(settings {
    rarity(Rarity.RARE)
}) {
    override val id = Constants.TRACKER
    override val tier = 3
}