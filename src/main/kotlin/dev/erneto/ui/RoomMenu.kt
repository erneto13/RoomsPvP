package dev.erneto.ui

import dev.erneto.RoomsPvP
import dev.erneto.manager.RoomManager
import dev.erneto.model.RoomStatus
import dev.erneto.utils.Head
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
            .rows(3)
            .create()

        val lifecycle = RoomsPvP.getInstance()
            .getRoomLifecycleManager()

        val startRoom = ItemBuilder
            .from(Head.fromBase64(MenuHeads.START_ROOM))
            .name(Message.parse("<#3fe52a>Start Room</#3fe52a>"))
            .asGuiItem {
                Message.parse("send a new room...", player)
            }

        val searchRooms = ItemBuilder
            .from(Head.fromBase64(MenuHeads.LIST_ROOMS))
            .name(Message.parse("<#3fe52a>Search Rooms</#3fe52a>"))
            .asGuiItem {
                gui.close(player)
                RoomMenu.listRooms(player)
            }

        gui.setItem(11, startRoom)
        gui.setItem(15, searchRooms)
        gui.open(player)
    }

    private fun listRooms(player: Player) {
        val title = Message.parse(Message.getMsg("room-list-title"))

        val gui = Gui.gui()
            .title(title)
            .rows(1)
            .create()

        val item = ItemBuilder
            .from(Material.NETHERITE_SWORD)
            .name(Message.parse("desde otro menú"))
            .asGuiItem {
                Message.parse("send a new room...", player)
            }

        gui.setItem(2, item)
        gui.open(player)
    }
}