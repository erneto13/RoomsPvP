package dev.erneto.commands

import dev.erneto.utils.Message
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Subcommand

@Command("room")
class RoomCommand {
    @Subcommand("create")
    fun createRoom() {
        Message.success("Room created successfully.")
    }
}