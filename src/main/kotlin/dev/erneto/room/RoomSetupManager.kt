package dev.erneto.room

import dev.erneto.RoomsPvP
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
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
        val plugin = RoomsPvP.getInstance()
        val roomsFolder = File(plugin.dataFolder, "rooms")
        roomsFolder.mkdirs()

        val roomFile = File(roomsFolder, "${session.roomName}.yml")
        val config = YamlConfiguration()

        config.set("display-name", "<gradient:#ff6b6b:#4ecdc4>${session.roomName}")
        config.set("max-players", 4)

        val corner1 = session.corner1!!
        config.set("corner1.x", corner1.blockX)
        config.set("corner1.y", corner1.blockY)
        config.set("corner1.z", corner1.blockZ)
        config.set("corner1.world", corner1.world?.name)

        val corner2 = session.corner2!!
        config.set("corner2.x", corner2.blockX)
        config.set("corner2.y", corner2.blockY)
        config.set("corner2.z", corner2.blockZ)
        config.set("corner2.world", corner2.world?.name)

        for (level in 1..5) {
            val nucleus = session.nucleusLocations[level]!!
            config.set("nucleus.level-$level.x", nucleus.x)
            config.set("nucleus.level-$level.y", nucleus.y)
            config.set("nucleus.level-$level.z", nucleus.z)
            config.set("nucleus.level-$level.world", nucleus.world?.name)
        }

        config.save(roomFile)
        plugin.logger.info("Room '${session.roomName}' saved successfully")
    }
}