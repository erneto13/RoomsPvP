package dev.erneto.model

import org.bukkit.Location
import org.bukkit.Material

data class Room(
    val name: String,
    val displayName: String,
    val corner1: Location,
    val corner2: Location,
    val nucleusLocations: Map<Int, Location>,
    val maxPlayers: Int,
    val level: Int = 1
) {
    val crops: MutableSet<BlockData> = mutableSetOf()
    val ores: MutableSet<BlockData> = mutableSetOf()

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
                    if (isFarmable(block.type)) {
                        crops.add(BlockData(block.location.clone(), block.type))
                    }
                    if (isMineable(block.type)) {
                        ores.add(BlockData(block.location.clone(), block.type))
                    }
                }
            }
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
        val originalMaterial: Material
    )
}