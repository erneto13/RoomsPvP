package dev.erneto.commands

import dev.erneto.utils.Message
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Named
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission

@Command("room")
@CommandPermission("rooms.admin")
class RoomSetupCommand {

    @Subcommand("setup")
    fun setup(actor: BukkitCommandActor, @Named("room") roomName: String) {
        if (!actor.isPlayer) {
            Message.send(actor.sender(), "player-only")
            return
        }

        val player = actor.asPlayer()!!

        Message.send(player, "room-setup-start")
    }
}