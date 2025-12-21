package dev.erneto.commands

import dev.erneto.manager.RoomManager
import dev.erneto.ui.RoomMenu
import dev.erneto.utils.Message
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Named
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission

@Command("room")
class RoomCommand {

    @Command("room")
    fun roomMenu(actor: BukkitCommandActor) {
        if (actor.isPlayer) {
            RoomMenu.open(actor.asPlayer()!!)
        }
    }

    @Subcommand("reload")
    @CommandPermission("rooms.admin")
    fun reload(actor: BukkitCommandActor) {
        RoomManager.reloadRooms()
        Message.send(actor.sender(), "rooms-reloaded")
    }

    @Subcommand("delete")
    @CommandPermission("rooms.admin")
    fun delete(actor: BukkitCommandActor, @Named("room") roomName: String) {
        if (RoomManager.removeRoom(roomName)) {
            val msg = Message.getMsg("room-deleted")
                .replace("%room%", roomName)
            actor.sender().sendMessage(Message.parse(msg))
        } else {
            val msg = Message.getMsg("room-not-found")
                .replace("%room%", roomName)
            actor.sender().sendMessage(Message.parse(msg))
        }
    }

    @Subcommand("list")
    @CommandPermission("rooms.admin")
    fun list(actor: BukkitCommandActor) {
        val rooms = RoomManager.getAllRooms()

        if (rooms.isEmpty()) {
            Message.send(actor.sender(), "no-rooms-available")
            return
        }

        actor.sender().sendMessage(Message.parse("<prefix><green>Rooms disponibles:"))
        rooms.forEach { room ->
            actor.sender().sendMessage(
                Message.parse("<gray>- <yellow>${room.name} <gray>(<white>${room.displayName}<gray>)")
            )
        }
    }
}