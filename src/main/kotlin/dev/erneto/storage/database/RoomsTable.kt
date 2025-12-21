package dev.erneto.storage.database

import org.jetbrains.exposed.sql.Table

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
    val maxPlayers = integer("max_players").default(4)
    val level = integer("level").default(1)

    override val primaryKey = PrimaryKey(id)
}