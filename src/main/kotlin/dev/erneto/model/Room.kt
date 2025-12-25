package dev.erneto.model

import org.bukkit.Location
import org.bukkit.Material
import java.time.Instant
import java.util.UUID

data class Room(
    val name: String,
    val displayName: String,
    val corner1: Location,
    val corner2: Location,
    val nucleusLocations: Map<Int, Location>,
    val spawnPoint: Location,
    var currentLevel: Int = 1,
    var status: RoomStatus = RoomStatus.AVAILABLE,
    var owner: UUID? = null,
    var claimedAt: Instant? = null,
    var lastActivity: Instant? = null
) {
    val blockStates: MutableMap<Location, BlockState> = mutableMapOf()
    val crops: MutableSet<BlockData> = mutableSetOf()
    val ores: MutableSet<BlockData> = mutableSetOf()

    fun claim(player: UUID): Boolean {
        if (status != RoomStatus.AVAILABLE) return false

        owner = player
        status = RoomStatus.OCCUPIED
        claimedAt = Instant.now()
        lastActivity = Instant.now()
        return true
    }

    fun unclaim() {
        owner = null
        status = RoomStatus.RESETTING
        claimedAt = null
    }

    fun isOwner(player: UUID): Boolean = owner == player

    fun updateActivity() {
        lastActivity = Instant.now()
    }

    fun scanBlocks() {
        val world = corner1.world ?: return

        val minX = minOf(corner1.blockX, corner2.blockX)
        val maxX = maxOf(corner1.blockX, corner2.blockX)
        val minY = minOf(corner1.blockY, corner2.blockY)
        val maxY = maxOf(corner1.blockY, corner2.blockY)
        val minZ = minOf(corner1.blockZ, corner2.blockZ)
        val maxZ = maxOf(corner1.blockZ, corner2.blockZ)

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val block = world.getBlockAt(x, y, z)
                    val location = block.location.clone()

                    if (isFarmable(block.type)) {
                        val blockData = BlockData(location, block.type, block.blockData)
                        crops.add(blockData)
                        blockStates[location] = BlockState(block.type, block.blockData, BlockType.CROP)
                    }

                    if (isMineable(block.type)) {
                        val blockData = BlockData(location, block.type, block.blockData)
                        ores.add(blockData)
                        blockStates[location] = BlockState(block.type, block.blockData, BlockType.ORE)
                    }
                }
            }
        }
    }

    fun saveBlockStates() {
        val world = corner1.world ?: return

        blockStates.clear()

        val minX = minOf(corner1.blockX, corner2.blockX)
        val maxX = maxOf(corner1.blockX, corner2.blockX)
        val minY = minOf(corner1.blockY, corner2.blockY)
        val maxY = maxOf(corner1.blockY, corner2.blockY)
        val minZ = minOf(corner1.blockZ, corner2.blockZ)
        val maxZ = maxOf(corner1.blockZ, corner2.blockZ)

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val block = world.getBlockAt(x, y, z)
                    val location = block.location.clone()

                    val blockType = when {
                        isFarmable(block.type) -> BlockType.CROP
                        isMineable(block.type) -> BlockType.ORE
                        block.type != Material.AIR -> BlockType.OTHER
                        else -> null
                    }

                    blockType?.let {
                        blockStates[location] = BlockState(
                            material = block.type,
                            data = block.blockData,
                            type = it,
                            lastUpdate = Instant.now()
                        )
                    }
                }
            }
        }
    }

    fun restoreBlockStates() {
        val world = corner1.world ?: return

        blockStates.forEach { (location, state) ->
            val block = world.getBlockAt(location)
            block.type = state.material
            block.blockData = state.data
        }
    }

    fun isLocationInBounds(location: Location): Boolean {
        val minX = minOf(corner1.blockX, corner2.blockX)
        val maxX = maxOf(corner1.blockX, corner2.blockX)
        val minY = minOf(corner1.blockY, corner2.blockY)
        val maxY = maxOf(corner1.blockY, corner2.blockY)
        val minZ = minOf(corner1.blockZ, corner2.blockZ)
        val maxZ = maxOf(corner1.blockZ, corner2.blockZ)

        return location.blockX in minX..maxX &&
                location.blockY in minY..maxY &&
                location.blockZ in minZ..maxZ
    }

    fun getCurrentNucleus(): Location? {
        return nucleusLocations[currentLevel]
    }

    fun upgradeLevel(): Boolean {
        if (currentLevel >= 5) return false
        currentLevel++
        lastActivity = Instant.now()
        return true
    }

    fun downgradeLevel(): Boolean {
        if (currentLevel <= 1) return false
        currentLevel--
        lastActivity = Instant.now()
        return true
    }

    private fun isFarmable(material: Material): Boolean {
        return material in listOf(
            Material.WHEAT,
            Material.CARROTS,
            Material.POTATOES,
            Material.BEETROOTS,
            Material.NETHER_WART,
            Material.SWEET_BERRY_BUSH,
            Material.MELON,
            Material.PUMPKIN
        )
    }

    private fun isMineable(material: Material): Boolean {
        return material in listOf(
            Material.COAL_ORE,
            Material.IRON_ORE,
            Material.GOLD_ORE,
            Material.DIAMOND_ORE,
            Material.EMERALD_ORE,
            Material.LAPIS_ORE,
            Material.REDSTONE_ORE,
            Material.DEEPSLATE_COAL_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.DEEPSLATE_REDSTONE_ORE
        )
    }

    data class BlockData(
        val location: Location,
        val originalMaterial: Material,
        val originalData: org.bukkit.block.data.BlockData
    )

    data class BlockState(
        val material: Material,
        val data: org.bukkit.block.data.BlockData,
        val type: BlockType,
        val lastUpdate: Instant = Instant.now()
    )
}