package net.peanuuutz.intellibow.recipe

import net.minecraft.inventory.CraftingInventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.recipe.SpecialRecipeSerializer
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World
import net.peanuuutz.intellibow.IntelliBow
import net.peanuuutz.intellibow.item.IntelliBowItem
import net.peanuuutz.intellibow.item.WrenchItem
import net.peanuuutz.intellibow.item.module.ModuleItem
import net.peanuuutz.intellibow.util.*

object ModuleTweakingRecipe : SpecialCraftingRecipe(Identifier(IntelliBow.MOD_ID, Constants.MODULE_TWEAKING)) {
    private val RECIPE_SERIALIZER: SpecialRecipeSerializer<ModuleTweakingRecipe> =
        recipeSerializer(Constants.MODULE_TWEAKING, SpecialRecipeSerializer { ModuleTweakingRecipe })

    override fun matches(inventory: CraftingInventory, world: World): Boolean {
        var hasWrench = false
        var hasBow = false
        for (i in 0 until inventory.size()) {
            val stack = inventory.getStack(i)
            if (!stack.isEmpty) {
                when (stack.item) {
                    is IntelliBowItem -> hasBow = true
                    is WrenchItem -> hasWrench = true
                }
            }
        }
        return hasBow && hasWrench
    }

    override fun craft(inventory: CraftingInventory): ItemStack {
        val modulesForCrafting = mutableListOf<ModuleItem>()
        var bow = ItemStack.EMPTY
        for (i in 0 until inventory.size()) {
            val stack = inventory.getStack(i)
            if (!stack.isEmpty) {
                when (stack.item) {
                    is IntelliBowItem -> bow = stack.copy()
                    is ModuleItem -> modulesForCrafting.add(stack.item as ModuleItem)
                }
            }
        }
        return if (!bow.isEmpty) {
            if (modulesForCrafting.isEmpty()) {
                val removeModuleId = bow.removeModule()
                if (removeModuleId != null) {
                    bow.apply { getSubTag(Constants.MOD_TAG)!!.putString(Constants.REMOVED_MODULE_TAG, removeModuleId) }
                } else {
                    ItemStack.EMPTY
                }
            } else {
                val bowItem = bow.item as IntelliBowItem
                val modules = bow.modules
                if (modules.size + modulesForCrafting.size <= bowItem.moduleCapacity &&
                    modulesForCrafting.all {
                        it.tier <= bowItem.maxModuleTier &&
                        modules.none { existModule -> existModule.id == it.id }
                    }
                ) {
                    bow.apply { modulesForCrafting.forEach(this::addModule) }
                } else {
                    ItemStack.EMPTY
                }
            }
        } else {
            ItemStack.EMPTY
        }
    }

    override fun fits(width: Int, height: Int) = width * height >= 2

    override fun getSerializer() = RECIPE_SERIALIZER

    override fun getRemainingStacks(inventory: CraftingInventory): DefaultedList<ItemStack> {
        val defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY)
        for (i in defaultedList.indices) {
            val item = inventory.getStack(i).item
            if (item is WrenchItem) {
                defaultedList[i] = ItemStack(item)
            }
        }
        return defaultedList
    }
}