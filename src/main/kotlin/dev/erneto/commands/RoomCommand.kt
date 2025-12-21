package dev.erneto.commands

import dev.erneto.ui.RoomMenu
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.actor.BukkitCommandActor

@Command("room")
class RoomCommand {

    @Command("room")
    fun roomMenu(actor: BukkitCommandActor) {
        if (actor.isPlayer) {
            RoomMenu.open(actor.asPlayer()!!)
        }
    }

    @Subcommand("create")
    fun createRoom() {
        //todo: implement create room logic
    }
}