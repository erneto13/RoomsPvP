package dev.erneto.utils

import com.destroystokyo.paper.profile.PlayerProfile
import com.destroystokyo.paper.profile.ProfileProperty
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

object Head {

    fun fromBase64(texture: String): ItemStack {
        val item = ItemStack(Material.PLAYER_HEAD)
        val meta = item.itemMeta as SkullMeta

        val profile: PlayerProfile = Bukkit.createProfile(UUID.randomUUID())
        profile.properties.add(ProfileProperty("textures", texture))

        meta.playerProfile = profile
        item.itemMeta = meta
        return item
    }

    fun fromPlayer(name: String): ItemStack {
        val item = ItemStack(Material.PLAYER_HEAD)
        val meta = item.itemMeta as SkullMeta
        meta.owningPlayer = Bukkit.getOfflinePlayer(name)
        item.itemMeta = meta
        return item
    }
}
