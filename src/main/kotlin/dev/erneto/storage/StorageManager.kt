package dev.erneto.storage

import dev.erneto.RoomsPvP
import dev.erneto.model.Room
import dev.erneto.storage.database.NucleusTable
import dev.erneto.storage.database.RoomsTable
import org.bukkit.Bukkit
import org.bukkit.Location
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object StorageManager {

    private lateinit var database: Database

    fun initialize() {
        val plugin = RoomsPvP.getInstance()
        val engineConfig = plugin.getEngineConfig()

        val storageType = engineConfig.getString("storage.type", "H2")?.uppercase() ?: "H2"

        database = when (storageType) {
            "MYSQL" -> {
                val host = engineConfig.getString("storage.mysql.host", "localhost")!!
                val port = engineConfig.getInt("storage.mysql.port", 3306)
                val dbName = engineConfig.getString("storage.mysql.database", "roomspvp")!!
                val user = engineConfig.getString("storage.mysql.username", "root")!!
                val pass = engineConfig.getString("storage.mysql.password", "password")!!

                Database.connect(
                    url = "jdbc:mysql://$host:$port/$dbName",
                    driver = "com.mysql.cj.jdbc.Driver",
                    user = user,
                    password = pass
                )
            }
            else -> {
                val dbFile = engineConfig.getString("storage.h2.file", "database")!!
                val dbPath = File(plugin.dataFolder, "$dbFile.db").absolutePath

                Database.connect(
                    url = "jdbc:h2:$dbPath",
                    driver = "org.h2.Driver"
                )
            }
        }

        createTables()
        plugin.logger.info("Storage initialized using $storageType")
    }

    private fun createTables() {
        transaction(database) {
            SchemaUtils.create(RoomsTable)
            SchemaUtils.create(NucleusTable)
        }
    }

    fun saveRoom(room: Room) {
        transaction(database) {
            val existingId = RoomsTable.selectAll().where { RoomsTable.roomId eq room.name }.singleOrNull()?.get(RoomsTable.id)

            val roomDbId = if (existingId != null) {
                RoomsTable.update({ RoomsTable.id eq existingId }) {
                    it[displayName] = room.displayName
                    it[worldName] = room.corner1.world?.name ?: "world"
                    it[corner1X] = room.corner1.blockX
                    it[corner1Y] = room.corner1.blockY
                    it[corner1Z] = room.corner1.blockZ
                    it[corner2X] = room.corner2.blockX
                    it[corner2Y] = room.corner2.blockY
                    it[corner2Z] = room.corner2.blockZ
                    it[maxPlayers] = room.maxPlayers
                    it[level] = room.level
                }
                existingId
            } else {
                RoomsTable.insert {
                    it[roomId] = room.name
                    it[displayName] = room.displayName
                    it[worldName] = room.corner1.world?.name ?: "world"
                    it[corner1X] = room.corner1.blockX
                    it[corner1Y] = room.corner1.blockY
                    it[corner1Z] = room.corner1.blockZ
                    it[corner2X] = room.corner2.blockX
                    it[corner2Y] = room.corner2.blockY
                    it[corner2Z] = room.corner2.blockZ
                    it[maxPlayers] = room.maxPlayers
                    it[level] = room.level
                } get RoomsTable.id
            }

            NucleusTable.deleteWhere { NucleusTable.roomDbId eq roomDbId }

            room.nucleusLocations.forEach { (lvl, loc) ->
                NucleusTable.insert {
                    it[NucleusTable.roomDbId] = roomDbId
                    it[nucleusLevel] = lvl
                    it[worldName] = loc.world?.name ?: "world"
                    it[x] = loc.x
                    it[y] = loc.y
                    it[z] = loc.z
                }
            }
        }
    }

    fun loadRooms(): List<Room> {
        return transaction(database) {
            val rooms = mutableListOf<Room>()

            RoomsTable.selectAll().forEach { roomRow ->
                val roomDbId = roomRow[RoomsTable.id]
                val roomId = roomRow[RoomsTable.roomId]
                val displayName = roomRow[RoomsTable.displayName]
                val worldName = roomRow[RoomsTable.worldName]
                val world = Bukkit.getWorld(worldName)

                if (world == null) {
                    RoomsPvP.getInstance().logger.warning("World '$worldName' not found for room '$roomId'")
                    return@forEach
                }

                val corner1 = Location(
                    world,
                    roomRow[RoomsTable.corner1X].toDouble(),
                    roomRow[RoomsTable.corner1Y].toDouble(),
                    roomRow[RoomsTable.corner1Z].toDouble()
                )

                val corner2 = Location(
                    world,
                    roomRow[RoomsTable.corner2X].toDouble(),
                    roomRow[RoomsTable.corner2Y].toDouble(),
                    roomRow[RoomsTable.corner2Z].toDouble()
                )

                val nucleusLocations = mutableMapOf<Int, Location>()
                NucleusTable.selectAll().where { NucleusTable.roomDbId eq roomDbId }.forEach { nucleusRow ->
                    val level = nucleusRow[NucleusTable.nucleusLevel]
                    val nucleusWorld = Bukkit.getWorld(nucleusRow[NucleusTable.worldName])

                    if (nucleusWorld != null) {
                        nucleusLocations[level] = Location(
                            nucleusWorld,
                            nucleusRow[NucleusTable.x],
                            nucleusRow[NucleusTable.y],
                            nucleusRow[NucleusTable.z]
                        )
                    }
                }

                val room = Room(
                    name = roomId,
                    displayName = displayName,
                    corner1 = corner1,
                    corner2 = corner2,
                    nucleusLocations = nucleusLocations,
                    maxPlayers = roomRow[RoomsTable.maxPlayers],
                    level = roomRow[RoomsTable.level]
                )

                room.scanBlocks()
                rooms.add(room)
            }

            rooms
        }
    }

    fun deleteRoom(roomId: String): Boolean {
        return transaction(database) {
            val roomDbId = RoomsTable.selectAll().where { RoomsTable.roomId eq roomId }
                .singleOrNull()?.get(RoomsTable.id) ?: return@transaction false

            NucleusTable.deleteWhere { NucleusTable.roomDbId eq roomDbId }
            RoomsTable.deleteWhere { RoomsTable.roomId eq roomId } > 0
        }
    }
}