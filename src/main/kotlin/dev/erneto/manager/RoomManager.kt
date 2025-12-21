package dev.erneto.manager

import dev.erneto.RoomsPvP
import dev.erneto.model.Room
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object RoomManager {

    private val rooms = ConcurrentHashMap<String, Room>()

    fun loadRooms() {
        val plugin = RoomsPvP.getInstance()
        val roomsFolder = File(plugin.dataFolder, "rooms")

        if (!roomsFolder.exists() || !roomsFolder.isDirectory) {
            plugin.logger.warning("Rooms folder does not exist")
            return
        }

        val roomFiles = roomsFolder.listFiles { _, name ->
            name.endsWith(".yml", ignoreCase = true)
        } ?: return

        if (roomFiles.isEmpty()) {
            plugin.logger.warning("No room files found")
            return
        }

        for (roomFile in roomFiles) {
            val config = YamlConfiguration.loadConfiguration(roomFile)
            val roomID = roomFile.nameWithoutExtension

            val displayName = config.getString("display-name") ?: roomID
            val maxPlayers = config.getInt("max-players", 4)

            val corner1 = Location(
                Bukkit.getWorld(config.getString("corner1.world")!!),
                config.getDouble("corner1.x"),
                config.getDouble("corner1.y"),
                config.getDouble("corner1.z")
            )

            val corner2 = Location(
                Bukkit.getWorld(config.getString("corner2.world")!!),
                config.getDouble("corner2.x"),
                config.getDouble("corner2.y"),
                config.getDouble("corner2.z")
            )

            val nucleusLocations = mutableMapOf<Int, Location>()
            for (level in 1..5) {
                val nucleus = Location(
                    Bukkit.getWorld(config.getString("nucleus.level-$level.world")!!),
                    config.getDouble("nucleus.level-$level.x"),
                    config.getDouble("nucleus.level-$level.y"),
                    config.getDouble("nucleus.level-$level.z")
                )
                nucleusLocations[level] = nucleus
            }

            val room = Room(
                roomID,
                displayName,
                corner1,
                corner2,
                nucleusLocations,
                maxPlayers
            )

            room.scanBlocks()
            rooms[roomID] = room

            plugin.logger.info("Room '$roomID' loaded - Crops: ${room.crops.size}, Ores: ${room.ores.size}")
        }
    }

    fun getRoom(name: String): Room? = rooms[name]

    fun getAllRooms(): Collection<Room> = rooms.values

    fun getRoomByLocation(location: Location): Room? {
        return rooms.values.find { it.isLocationInBounds(location) }
    }

    fun reloadRooms() {
        rooms.clear()
        loadRooms()
    }
}