package dev.erneto.room

import dev.erneto.manager.RoomManager
import dev.erneto.model.Room
import org.bukkit.entity.Player
import java.util.UUID

class RoomSetupManager {

    private val sessions = mutableMapOf<UUID, RoomSetupSession>()

    fun createSession(player: Player, roomName: String) {
        sessions[player.uniqueId] = RoomSetupSession(roomName)
    }

    fun getSession(player: Player): RoomSetupSession? {
        return sessions[player.uniqueId]
    }

    fun removeSession(player: Player) {
        sessions.remove(player.uniqueId)
    }

    fun saveRoom(session: RoomSetupSession) {
        val spawnPoint = session.spawnPoint ?: session.corner1!!.clone().add(0.5, 1.0, 0.5)

        val room = Room(
            name = session.roomName,
            displayName = "<gradient:#ff6b6b:#4ecdc4>${session.roomName}",
            corner1 = session.corner1!!,
            corner2 = session.corner2!!,
            nucleusLocations = session.nucleusLocations,
            spawnPoint = spawnPoint
        )

        room.scanBlocks()
        RoomManager.addRoom(room)
    }
}