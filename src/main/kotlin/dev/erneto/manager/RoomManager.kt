package dev.erneto.manager

import dev.erneto.RoomsPvP
import dev.erneto.model.Room
import dev.erneto.storage.StorageManager
import org.bukkit.Location
import java.util.concurrent.ConcurrentHashMap

object RoomManager {

    private val rooms = ConcurrentHashMap<String, Room>()

    fun loadRooms() {
        val plugin = RoomsPvP.getInstance()
        rooms.clear()

        val loadedRooms = StorageManager.loadRooms()

        loadedRooms.forEach { room ->
            rooms[room.name] = room
            plugin.logger.info("Room '${room.name}' loaded - Crops: ${room.crops.size}, Ores: ${room.ores.size}")
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

    fun getRoomByLocation(location: Location): Room? {
        return rooms.values.find { it.isLocationInBounds(location) }
    }

    fun roomExists(name: String): Boolean = rooms.containsKey(name)

    fun reloadRooms() {
        rooms.clear()
        loadRooms()
    }
}