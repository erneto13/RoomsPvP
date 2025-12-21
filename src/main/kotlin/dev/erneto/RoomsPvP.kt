package dev.erneto

import dev.erneto.commands.RoomCommand
import dev.erneto.commands.RoomSetupCommand
import revxrsal.commands.Lamp
import revxrsal.commands.bukkit.BukkitLamp
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.zapper.ZapperJavaPlugin

class RoomsPvP : ZapperJavaPlugin() {

    companion object {
        private lateinit var instance: RoomsPvP
        fun getInstance(): RoomsPvP = instance
    }

    override fun onEnable() {
        instance = this
        registerCommands()
        logger.info("RoomsPvP has been enabled. Developed by erneto13")
    }

    override fun onDisable() {
        logger.info("RoomsPvP has been disabled. Developed by erneto13")
    }

    private fun registerCommands() {
        val lamp: Lamp<BukkitCommandActor> = BukkitLamp.builder(this).build()

        lamp.register(
            RoomCommand(),
            RoomSetupCommand(),
        )

        logger.info("Commands registered")
    }
}
