package dev.erneto.storage.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object NucleusTable : Table("nucleus") {
    val id = integer("id").autoIncrement()
    val roomDbId = integer("room_db_id").references(RoomsTable.id, onDelete = ReferenceOption.CASCADE)
    val nucleusLevel = integer("nucleus_level")
    val worldName = varchar("world_name", 64)
    val x = double("x")
    val y = double("y")
    val z = double("z")

    override val primaryKey = PrimaryKey(id)
}