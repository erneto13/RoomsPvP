package dev.erneto.ui

import dev.erneto.manager.RoomManager
import dev.erneto.utils.Message
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.Gui
import org.bukkit.Material
import org.bukkit.entity.Player

object RoomMenu {

    fun open(player: Player) {
        val title = Message.parse(Message.getMsg("room-menu-title"))

        val gui = Gui.gui()
            .title(title)
            .rows(6)
            .create()

        RoomManager.getAllRooms().forEach { room ->
            val item = ItemBuilder.from(Material.IRON_DOOR)
                .name(Message.parse("<yellow>${room.name}"))
                .lore(
                    Message.parse("<gray>Level: <white>${room.level}"),
                    Message.parse("<gray>Ores: <white>${room.ores}"),
                    Message.parse("<gray>Crops: <white>${room.crops}"),
                )
                .asGuiItem {
                    player.teleport(room.corner1)
                }
            gui.addItem(item)
        }
        gui.open(player)
    }
}