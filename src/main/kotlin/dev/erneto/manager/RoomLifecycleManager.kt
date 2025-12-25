package dev.erneto.manager

import dev.erneto.RoomsPvP
import dev.erneto.model.Room
import dev.erneto.model.RoomStatus
import dev.erneto.utils.Message
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.time.Duration
import java.time.Instant

class RoomLifecycleManager(private val plugin: RoomsPvP) {

    private val resetQueue = mutableSetOf<String>()

    init {
        startAutoSaveTask()
        startInactivityCheck()
    }

    fun claimRoom(room: Room, player: Player): Boolean {
        if (room.status != RoomStatus.AVAILABLE) {
            Message.send(player, "room-not-available")
            return false
        }

        if (!room.claim(player.uniqueId)) {
            return false
        }

        teleportToSpawn(player, room)
        Message.send(player, "room-claimed")
        return true
    }

    fun unclaimRoom(room: Room, player: Player) {
        if (!room.isOwner(player.uniqueId)) {
            Message.send(player, "not-room-owner")
            return
        }

        Message.send(player, "room-unclaimed")
        teleportToLobby(player)
        scheduleReset(room)
    }

    fun teleportToSpawn(player: Player, room: Room) {
        player.teleport(room.spawnPoint)
        Message.send(player, "teleported-to-room")
    }

    fun teleportToLobby(player: Player) {
        val lobbyLocation = plugin.server.worlds[0].spawnLocation
        player.teleport(lobbyLocation)
    }

    fun handlePlayerDisconnect(player: Player) {
        val room = RoomManager.getAllRooms().find { it.isOwner(player.uniqueId) } ?: return

        room.saveBlockStates()
        scheduleReset(room)
    }

    fun handleNucleusDestroyed(room: Room) {
        room.owner?.let { ownerId ->
            Bukkit.getPlayer(ownerId)?.let { owner ->
                Message.send(owner, "nucleus-destroyed")
                teleportToLobby(owner)
            }
        }

        if (room.currentLevel > 1) {
            room.downgradeLevel()
            scheduleReset(room)
        } else {
            room.unclaim()
            scheduleReset(room)
        }
    }

    fun scheduleReset(room: Room) {
        if (resetQueue.contains(room.name)) return

        room.status = RoomStatus.RESETTING
        resetQueue.add(room.name)

        object : BukkitRunnable() {
            override fun run() {
                resetRoom(room)
                resetQueue.remove(room.name)
            }
        }.runTaskLater(plugin, 100L)
    }

    private fun resetRoom(room: Room) {
        room.restoreBlockStates()
        room.unclaim()
        room.status = RoomStatus.AVAILABLE

        plugin.logger.info("Room '${room.name}' has been reset and is now available")
    }

    private fun startAutoSaveTask() {
        object : BukkitRunnable() {
            override fun run() {
                RoomManager.getAllRooms()
                    .filter { it.status == RoomStatus.OCCUPIED }
                    .forEach { room ->
                        room.saveBlockStates()
                        RoomManager.saveRoomState(room)
                    }
            }
        }.runTaskTimerAsynchronously(plugin, 6000L, 6000L)
    }

    private fun startInactivityCheck() {
        object : BukkitRunnable() {
            override fun run() {
                val now = Instant.now()

                RoomManager.getAllRooms()
                    .filter { it.status == RoomStatus.OCCUPIED }
                    .forEach { room ->
                        room.lastActivity?.let { lastActivity ->
                            val inactiveTime = Duration.between(lastActivity, now)

                            if (inactiveTime.toMinutes() >= 30) {
                                room.owner?.let { ownerId ->
                                    Bukkit.getPlayer(ownerId)?.let { player ->
                                        Message.send(player, "room-inactive-unclaim")
                                    }
                                }
                                scheduleReset(room)
                            }
                        }
                    }
            }
        }.runTaskTimer(plugin, 12000L, 12000L)
    }
}