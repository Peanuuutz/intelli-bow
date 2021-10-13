package net.peanuuutz.intellibow.item

import net.minecraft.util.Rarity
import net.peanuuutz.intellibow.IntelliBow
import net.peanuuutz.intellibow.util.Constants
import net.peanuuutz.intellibow.util.settings

object RecurveBowItem : IntelliBowItem(settings {
    maxDamage(576)
}) {
    override val id = Constants.RECURSIVE_BOW

    override val baseSpeedModifier: Float
    override val baseDamageModifier: Double
    override val moduleCapacity: Int
    override val maxModuleTier = 2

    init {
        val attributes = IntelliBow.config.recursiveBowAttribute
        baseSpeedModifier = attributes.baseSpeedModifier
        baseDamageModifier = attributes.baseDamageModifier
        moduleCapacity = attributes.moduleCapacity
    }
}

object CompositeBowItem : IntelliBowItem(settings {
    rarity(Rarity.UNCOMMON)
    maxDamage(768)
}) {
    override val id = Constants.COMPOSITE_BOW

    override val baseSpeedModifier: Float
    override val baseDamageModifier: Double
    override val moduleCapacity: Int
    override val maxModuleTier = 3

    init {
        val attributes = IntelliBow.config.compositeBowAttribute
        baseSpeedModifier = attributes.baseSpeedModifier
        baseDamageModifier = attributes.baseDamageModifier
        moduleCapacity = attributes.moduleCapacity
    }
}