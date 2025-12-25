package dev.erneto.room

import org.bukkit.Location

data class RoomSetupSession(
    val roomName: String,
    var corner1: Location? = null,
    var corner2: Location? = null,
    var spawnPoint: Location? = null,
    val nucleusLocations: MutableMap<Int, Location> = mutableMapOf()
) {
    fun isComplete(): Boolean {
        return corner1 != null && corner2 != null && hasAllNucleusPositions()
    }

    fun hasAllNucleusPositions(): Boolean {
        return (1..5).all { nucleusLocations.containsKey(it) }
    }

    fun setNucleusForLevel(level: Int, location: Location) {
        nucleusLocations[level] = location
    }

    fun setNucleusForAllLevels(location: Location) {
        for (i in 1..5) {
            nucleusLocations[i] = location
        }
    }
}