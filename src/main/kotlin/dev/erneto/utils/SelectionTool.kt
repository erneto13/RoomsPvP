package dev.erneto.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

object SelectionTool {

    private const val TOOL_NAME = "Room Selection Wand"

    fun createSelectionAxe(): ItemStack {
        val axe = ItemStack(Material.GOLDEN_AXE)
        val meta = axe.itemMeta!!

        meta.displayName(
            Component.text(TOOL_NAME)
                .color(NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false)
        )

        meta.addEnchant(Enchantment.UNBREAKING, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        meta.isUnbreakable = true
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)

        axe.itemMeta = meta
        return axe
    }

    fun isSelectionAxe(item: ItemStack): Boolean {
        if (item.type != Material.GOLDEN_AXE) return false
        val meta = item.itemMeta ?: return false
        val name = meta.displayName() ?: return false
        return (name as? TextComponent)?.content()?.contains(TOOL_NAME) == true
    }

    fun removeSelectionAxe(inventory: PlayerInventory) {
        inventory.contents.forEach { item ->
            if (item != null && isSelectionAxe(item)) {
                inventory.remove(item)
            }
        }
    }
}