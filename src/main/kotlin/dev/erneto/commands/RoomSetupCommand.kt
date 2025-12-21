package dev.erneto.commands

import dev.erneto.RoomsPvP
import dev.erneto.room.RoomSetupManager
import dev.erneto.room.RoomValidator
import dev.erneto.utils.SelectionTool
import dev.erneto.utils.Message
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Named
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission

@Command("room")
@CommandPermission("rooms.admin")
class RoomSetupCommand(private val setupManager: RoomSetupManager) {

    @Subcommand("setup")
    fun setup(actor: BukkitCommandActor, @Named("room") roomName: String) {
        if (!actor.isPlayer) {
            Message.send(actor.sender(), "player-only")
            return
        }

        val player = actor.asPlayer()!!

        if (!RoomValidator.isValidRoomName(roomName)) {
            Message.send(player, "invalid-room-name")
            return
        }

        setupManager.createSession(player, roomName)

        val setupTool = SelectionTool.createSelectionAxe()
        player.inventory.addItem(setupTool)

        Message.send(player, "room-setup-start")
    }

    @Subcommand("setup corepos")
    fun setupCorePos(actor: BukkitCommandActor, @Named("level") levelStr: String) {
        if (!actor.isPlayer) {
            Message.send(actor.sender(), "player-only")
            return
        }

        val player = actor.asPlayer()!!
        val session = setupManager.getSession(player)

        if (session == null) {
            Message.send(player, "no-setup-session")
            return
        }

        if (session.corner1 == null || session.corner2 == null) {
            Message.send(player, "area-not-defined")
            return
        }

        val playerLocation = player.location.clone().add(0.5, 0.0, 0.5)

        when (levelStr.lowercase()) {
            "all" -> {
                session.setNucleusForAllLevels(playerLocation)
                Message.send(player, "nucleus-set-all")
            }
            in "1".."5" -> {
                val level = levelStr.toInt()
                session.setNucleusForLevel(level, playerLocation)
                val msg = Message.getMsg("nucleus-set-level")
                    .replace("%level%", level.toString())
                player.sendMessage(Message.parse(msg, player))
            }
            else -> {
                Message.send(player, "invalid-level")
                return
            }
        }

        //verificar si el setup esta completo
        if (session.isComplete()) {
            Message.send(player, "setup-ready-to-save")
        }
    }

    @Subcommand("setup save")
    fun save(actor: BukkitCommandActor) {
        if (!actor.isPlayer) {
            Message.send(actor.sender(), "player-only")
            return
        }

        val player = actor.asPlayer()!!
        val session = setupManager.getSession(player)

        if (session == null) {
            Message.send(player, "no-setup-session")
            return
        }

        if (!RoomValidator.validateSession(session, player)) {
            return
        }

        //validar bounds
        val boundsError = RoomValidator.validateBounds(session)
        if (boundsError != null) {
            player.sendMessage(Message.parse("<red>Error: $boundsError"))
            return
        }

        //validar nucleos
        val nucleusError = RoomValidator.validateNucleusLocations(session)
        if (nucleusError != null) {
            player.sendMessage(Message.parse("<red>Error: $nucleusError"))
            return
        }

        //guardar
        setupManager.saveRoom(session)

        //limpiar sesion y remover hacha
        val visualizer = RoomsPvP.getInstance().getCuboidVisualizer()
        visualizer.stopVisualization(player.name)
        SelectionTool.removeSelectionAxe(player.inventory)
        setupManager.removeSession(player)

        Message.send(player, "setup-complete-saved")
    }

    @Subcommand("setup cancel")
    fun cancel(actor: BukkitCommandActor) {
        if (!actor.isPlayer) {
            Message.send(actor.sender(), "player-only")
            return
        }

        val player = actor.asPlayer()!!
        val session = setupManager.getSession(player)

        if (session == null) {
            Message.send(player, "no-setup-session")
            return
        }

        val visualizer = RoomsPvP.getInstance().getCuboidVisualizer()
        visualizer.stopVisualization(player.name)
        SelectionTool.removeSelectionAxe(player.inventory)
        setupManager.removeSession(player)

        Message.send(player, "setup-cancelled")
    }
}