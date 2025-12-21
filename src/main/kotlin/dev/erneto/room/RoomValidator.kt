package dev.erneto.room

import dev.erneto.manager.RoomManager
import dev.erneto.utils.Message
import org.bukkit.entity.Player

object RoomValidator {

    fun validateSession(session: RoomSetupSession, player: Player): Boolean {
        if (session.isComplete()) {
            return true
        }

        Message.send(player, "setup-validation-failed")

        if (session.corner1 == null) {
            Message.send(player, "setup-missing-corner1")
        }
        if (session.corner2 == null) {
            Message.send(player, "setup-missing-corner2")
        }
        if (!session.hasAllNucleusPositions()) {
            Message.send(player, "setup-missing-nucleus")
        }

        return false
    }

    fun isValidRoomName(name: String?): Boolean {
        if (name.isNullOrBlank()) return false
        if (name.length > 32) return false
        if (!name.matches(Regex("[a-zA-Z0-9_-]+"))) return false
        if (RoomManager.roomExists(name)) return false
        return true
    }

    fun validateBounds(session: RoomSetupSession): String? {
        val corner1 = session.corner1 ?: return "Missing corner 1"
        val corner2 = session.corner2 ?: return "Missing corner 2"

        if (corner1.world != corner2.world) {
            return "Corners must be in the same world"
        }

        val dx = kotlin.math.abs(corner1.x - corner2.x)
        val dy = kotlin.math.abs(corner1.y - corner2.y)
        val dz = kotlin.math.abs(corner1.z - corner2.z)

        if (dx < 3 || dy < 3 || dz < 3) {
            return "Room is too small (minimum 3x3x3 blocks)"
        }

        if (dx > 100 || dy > 100 || dz > 100) {
            return "Room is too large (maximum 100x100x100 blocks)"
        }

        return null
    }

    fun validateNucleusLocations(session: RoomSetupSession): String? {
        val corner1 = session.corner1 ?: return "Corner 1 not set"
        val corner2 = session.corner2 ?: return "Corner 2 not set"

        val minX = minOf(corner1.x, corner2.x)
        val maxX = maxOf(corner1.x, corner2.x)
        val minY = minOf(corner1.y, corner2.y)
        val maxY = maxOf(corner1.y, corner2.y)
        val minZ = minOf(corner1.z, corner2.z)
        val maxZ = maxOf(corner1.z, corner2.z)

        for ((level, nucleus) in session.nucleusLocations) {
            if (nucleus.x !in minX..maxX ||
                nucleus.y !in minY..maxY ||
                nucleus.z !in minZ..maxZ) {
                return "Nucleus for level $level must be inside the room bounds"
            }
        }

        return null
    }
}