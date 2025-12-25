package dev.erneto.storage.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object RoomsTable : Table("rooms") {
    val id = integer("id").autoIncrement()
    val roomId = varchar("room_id", 32).uniqueIndex()
    val displayName = varchar("display_name", 128)
    val worldName = varchar("world_name", 64)
    val corner1X = integer("corner1_x")
    val corner1Y = integer("corner1_y")
    val corner1Z = integer("corner1_z")
    val corner2X = integer("corner2_x")
    val corner2Y = integer("corner2_y")
    val corner2Z = integer("corner2_z")
    val spawnX = double("spawn_x")
    val spawnY = double("spawn_y")
    val spawnZ = double("spawn_z")
    val spawnYaw = float("spawn_yaw")
    val spawnPitch = float("spawn_pitch")
    val currentLevel = integer("current_level").default(1)
    val status = varchar("status", 32).default("AVAILABLE")
    val owner = varchar("owner", 36).nullable()
    val claimedAt = timestamp("claimed_at").nullable()
    val lastActivity = timestamp("last_activity").nullable()

    override val primaryKey = PrimaryKey(id)
}