package dev.erneto.visual

import dev.erneto.RoomsPvP
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class CuboidVisualizer(private val plugin: RoomsPvP) {

    private val activeVisualizations = mutableMapOf<String, BukkitTask>()

    fun startVisualization(playerName: String, corner1: Location, corner2: Location) {
        stopVisualization(playerName)

        val task = object : BukkitRunnable() {
            override fun run() {
                if (corner1.world == null || corner2.world == null) {
                    cancel()
                    activeVisualizations.remove(playerName)
                    return
                }
                drawCuboid(corner1, corner2)
            }
        }

        val scheduledTask = task.runTaskTimer(plugin, 0L, 10L)
        activeVisualizations[playerName] = scheduledTask
    }

    fun stopVisualization(playerName: String) {
        activeVisualizations[playerName]?.cancel()
        activeVisualizations.remove(playerName)
    }

    fun hasActiveVisualization(playerName: String): Boolean {
        return activeVisualizations.containsKey(playerName)
    }

    fun stopAllVisualizations() {
        activeVisualizations.values.forEach { it.cancel() }
        activeVisualizations.clear()
    }

    private fun drawCuboid(loc1: Location, loc2: Location) {
        val world = loc1.world ?: return

        val minX = minOf(loc1.x, loc2.x)
        val minY = minOf(loc1.y, loc2.y)
        val minZ = minOf(loc1.z, loc2.z)
        val maxX = maxOf(loc1.x, loc2.x) + 1
        val maxY = maxOf(loc1.y, loc2.y) + 1
        val maxZ = maxOf(loc1.z, loc2.z) + 1

        val particleSpacing = 0.5

        //bordes inferiores
        drawLine(world, minX, minY, minZ, maxX, minY, minZ, particleSpacing)
        drawLine(world, minX, minY, maxZ, maxX, minY, maxZ, particleSpacing)
        drawLine(world, minX, minY, minZ, minX, minY, maxZ, particleSpacing)
        drawLine(world, maxX, minY, minZ, maxX, minY, maxZ, particleSpacing)

        //bordes superiores
        drawLine(world, minX, maxY, minZ, maxX, maxY, minZ, particleSpacing)
        drawLine(world, minX, maxY, maxZ, maxX, maxY, maxZ, particleSpacing)
        drawLine(world, minX, maxY, minZ, minX, maxY, maxZ, particleSpacing)
        drawLine(world, maxX, maxY, minZ, maxX, maxY, maxZ, particleSpacing)

        //bordes verticales
        drawLine(world, minX, minY, minZ, minX, maxY, minZ, particleSpacing)
        drawLine(world, maxX, minY, minZ, maxX, maxY, minZ, particleSpacing)
        drawLine(world, minX, minY, maxZ, minX, maxY, maxZ, particleSpacing)
        drawLine(world, maxX, minY, maxZ, maxX, maxY, maxZ, particleSpacing)

        //vertices con particulas mas grandes
        val vertices = listOf(
            Location(world, minX, minY, minZ),
            Location(world, maxX, minY, minZ),
            Location(world, minX, minY, maxZ),
            Location(world, maxX, minY, maxZ),
            Location(world, minX, maxY, minZ),
            Location(world, maxX, maxY, minZ),
            Location(world, minX, maxY, maxZ),
            Location(world, maxX, maxY, maxZ)
        )

        vertices.forEach { vertex ->
            world.spawnParticle(
                Particle.DUST,
                vertex,
                5,
                0.0, 0.0, 0.0,
                Particle.DustOptions(Color.YELLOW, 2.0f)
            )
        }
    }

    private fun drawLine(
        world: World,
        x1: Double, y1: Double, z1: Double,
        x2: Double, y2: Double, z2: Double,
        spacing: Double
    ) {
        val dx = x2 - x1
        val dy = y2 - y1
        val dz = z2 - z1
        val distance = kotlin.math.sqrt(dx * dx + dy * dy + dz * dz)
        val points = (distance / spacing).toInt().coerceAtLeast(1)

        for (i in 0..points) {
            val t = i.toDouble() / points
            val x = x1 + dx * t
            val y = y1 + dy * t
            val z = z1 + dz * t

            world.spawnParticle(
                Particle.DUST,
                Location(world, x, y, z),
                1,
                0.0, 0.0, 0.0,
                Particle.DustOptions(Color.AQUA, 1.0f)
            )
        }
    }
}