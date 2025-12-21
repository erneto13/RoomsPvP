package dev.erneto

import dev.dejvokep.boostedyaml.YamlDocument
import dev.erneto.commands.RoomCommand
import dev.erneto.commands.RoomSetupCommand
import dev.erneto.listener.SetupListener
import dev.erneto.manager.RoomManager
import dev.erneto.room.RoomSetupManager
import dev.erneto.storage.StorageManager
import dev.erneto.storage.file.FileManager
import dev.erneto.visual.CuboidVisualizer
import revxrsal.commands.Lamp
import revxrsal.commands.bukkit.BukkitLamp
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.zapper.ZapperJavaPlugin

class RoomsPvP : ZapperJavaPlugin() {

    companion object {
        private lateinit var instance: RoomsPvP
        fun getInstance(): RoomsPvP = instance
    }

    private lateinit var cuboidVisualizer: CuboidVisualizer
    private lateinit var setupManager: RoomSetupManager

    override fun onEnable() {
        instance = this
        setupManager = RoomSetupManager()

        initFileManager()
        initStorage()
        RoomManager.loadRooms()

        initVisualizer()
        registerCommands()
        registerListeners()

        logger.info("RoomsPvP has been enabled. Developed by erneto13")
    }

    override fun onDisable() {
        cuboidVisualizer.stopAllVisualizations()
        logger.info("RoomsPvP has been disabled. Developed by erneto13")
    }

    private fun registerCommands() {
        val lamp: Lamp<BukkitCommandActor> = BukkitLamp.builder(this).build()

        lamp.register(
            RoomCommand(),
            RoomSetupCommand(setupManager),
        )

        logger.info("Commands registered")
    }

    private fun registerListeners() {
        server.pluginManager.registerEvents(SetupListener(setupManager), this)
        logger.info("Listeners registered")
    }

    private fun initFileManager() {
        FileManager.initialize()
        logger.info("FileManager initialized")
    }

    private fun initStorage() {
        StorageManager.initialize()
        logger.info("StorageManager initialized")
    }

    private fun initVisualizer() {
        cuboidVisualizer = CuboidVisualizer(this)
        logger.info("CuboidVisualizer initialized")
    }

    fun getCuboidVisualizer(): CuboidVisualizer = cuboidVisualizer

    fun getEngineConfig(): YamlDocument {
        return FileManager.get("engine")
            ?: throw IllegalStateException("Engine config not loaded")
    }
}