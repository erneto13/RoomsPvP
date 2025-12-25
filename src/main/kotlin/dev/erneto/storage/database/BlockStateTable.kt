package dev.erneto.storage.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object BlockStatesTable : Table("block_states") {
    val id = integer("id").autoIncrement()
    val roomDbId = integer("room_db_id").references(RoomsTable.id, onDelete = ReferenceOption.CASCADE)
    val worldName = varchar("world_name", 64)
    val x = integer("x")
    val y = integer("y")
    val z = integer("z")
    val material = varchar("material", 64)
    val blockData = text("block_data")
    val blockType = varchar("block_type", 32)
    val lastUpdate = timestamp("last_update")

    override val primaryKey = PrimaryKey(id)

    init {
        index(true, roomDbId, x, y, z)
    }
}