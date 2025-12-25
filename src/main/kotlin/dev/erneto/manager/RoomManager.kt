package dev.erneto.manager

import dev.erneto.RoomsPvP
import dev.erneto.model.Room
import dev.erneto.model.RoomStatus
import dev.erneto.storage.StorageManager
import org.bukkit.Location
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object RoomManager {

    private val rooms = ConcurrentHashMap<String, Room>()

    fun loadRooms() {
        val plugin = RoomsPvP.getInstance()
        rooms.clear()

        val loadedRooms = StorageManager.loadRooms()

        loadedRooms.forEach { room ->
            rooms[room.name] = room
            plugin.logger.info("Room '${room.name}' loaded - Status: ${room.status}, Level: ${room.currentLevel}")
        }

        plugin.logger.info("Loaded ${rooms.size} room(s)")
    }

    fun addRoom(room: Room) {
        rooms[room.name] = room
        StorageManager.saveRoom(room)
    }

    fun removeRoom(roomName: String): Boolean {
        return if (rooms.remove(roomName) != null) {
            StorageManager.deleteRoom(roomName)
            true
        } else {
            false
        }
    }

    fun getRoom(name: String): Room? = rooms[name]

    fun getAllRooms(): Collection<Room> = rooms.values

    fun getAvailableRooms(): List<Room> {
        return rooms.values.filter { it.status == RoomStatus.AVAILABLE }
    }

    fun getOccupiedRooms(): List<Room> {
        return rooms.values.filter { it.status == RoomStatus.OCCUPIED }
    }

    fun getRoomByPlayer(playerId: UUID): Room? {
        return rooms.values.find { it.isOwner(playerId) }
    }

    fun getRoomByLocation(location: Location): Room? {
        return rooms.values.find { it.isLocationInBounds(location) }
    }

    fun roomExists(name: String): Boolean = rooms.containsKey(name)

    fun reloadRooms() {
        rooms.clear()
        loadRooms()
    }

    fun saveRoomState(room: Room) {
        StorageManager.updateRoomState(room)
    }

    fun saveAllRoomStates() {
        rooms.values
            .filter { it.status == RoomStatus.OCCUPIED }
            .forEach { room ->
                room.saveBlockStates()
                saveRoomState(room)
            }
    }
}