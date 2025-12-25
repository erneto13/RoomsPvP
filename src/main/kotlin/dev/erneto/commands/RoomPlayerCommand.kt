package dev.erneto.commands

import dev.erneto.RoomsPvP
import dev.erneto.manager.RoomManager
import dev.erneto.utils.Message
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.actor.BukkitCommandActor

@Command("room")
class RoomPlayerCommand {

    @Subcommand("home")
    fun home(actor: BukkitCommandActor) {
        if (!actor.isPlayer) {
            Message.send(actor.sender(), "player-only")
            return
        }

        val player = actor.asPlayer()!!
        val room = RoomManager.getRoomByPlayer(player.uniqueId)

        if (room == null) {
            Message.send(player, "no-room-owned")
            return
        }

        player.teleport(room.spawnPoint)
        Message.send(player, "teleported-home")
    }

    @Subcommand("unclaim")
    fun unclaim(actor: BukkitCommandActor) {
        if (!actor.isPlayer) {
            Message.send(actor.sender(), "player-only")
            return
        }

        val player = actor.asPlayer()!!
        val room = RoomManager.getRoomByPlayer(player.uniqueId)

        if (room == null) {
            Message.send(player, "no-room-owned")
            return
        }

        if (!room.isOwner(player.uniqueId)) {
            Message.send(player, "not-room-owner")
            return
        }

        val lifecycle = RoomsPvP.getInstance().getRoomLifecycleManager()
        lifecycle.unclaimRoom(room, player)
    }

    @Subcommand("info")
    fun info(actor: BukkitCommandActor) {
        if (!actor.isPlayer) {
            Message.send(actor.sender(), "player-only")
            return
        }

        val player = actor.asPlayer()!!
        val room = RoomManager.getRoomByPlayer(player.uniqueId)

        if (room == null) {
            Message.send(player, "no-room-owned")
            return
        }

        player.sendMessage(Message.parse("<prefix><aqua>═══════════════════════"))
        player.sendMessage(Message.parse("<gold>Room: <yellow>${room.name}"))
        player.sendMessage(Message.parse("<gold>Display: ${room.displayName}"))
        player.sendMessage(Message.parse("<gold>Level: <yellow>${room.currentLevel}/5"))
        player.sendMessage(Message.parse("<gold>Status: <yellow>${room.status}"))
        player.sendMessage(Message.parse("<gold>Crops: <yellow>${room.crops.size}"))
        player.sendMessage(Message.parse("<gold>Ores: <yellow>${room.ores.size}"))

        room.claimedAt?.let {
            player.sendMessage(Message.parse("<gold>Claimed: <yellow>${it}"))
        }

        player.sendMessage(Message.parse("<prefix><aqua>═══════════════════════"))
    }
}