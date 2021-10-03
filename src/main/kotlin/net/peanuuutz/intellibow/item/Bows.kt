package net.peanuuutz.intellibow.item

import net.minecraft.util.Rarity
import net.peanuuutz.intellibow.IntelliBow
import net.peanuuutz.intellibow.registry.Constants
import net.peanuuutz.intellibow.util.settings

object RecurveBowItem : IntelliBowItem(settings {
    maxDamage(576)
}) {
    override val id = Constants.RECURSIVE_BOW

    override val baseSpeedModifier = IntelliBow.config.recursiveBowAttribute.baseSpeedModifier
    override val baseDamageModifier = IntelliBow.config.recursiveBowAttribute.baseDamageModifier

    override val moduleCapacity = IntelliBow.config.recursiveBowAttribute.moduleCapacity
}

object CompositeBowItem : IntelliBowItem(settings {
    rarity(Rarity.UNCOMMON)
    maxDamage(768)
}) {
    override val id = Constants.COMPOSITE_BOW

    override val baseSpeedModifier = IntelliBow.config.compositeBowAttribute.baseSpeedModifier
    override val baseDamageModifier = IntelliBow.config.compositeBowAttribute.baseDamageModifier

    override val moduleCapacity = IntelliBow.config.compositeBowAttribute.moduleCapacity
}