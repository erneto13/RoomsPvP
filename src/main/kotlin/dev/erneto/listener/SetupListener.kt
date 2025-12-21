package dev.erneto.listener

import dev.erneto.RoomsPvP
import dev.erneto.room.RoomSetupManager
import dev.erneto.utils.SelectionTool
import dev.erneto.utils.Message
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot

class SetupListener(private val setupManager: RoomSetupManager) : Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val session = setupManager.getSession(player) ?: return

        if (event.hand != EquipmentSlot.HAND) return

        val item = player.inventory.itemInMainHand
        if (!SelectionTool.isSelectionAxe(item)) return

        event.isCancelled = true

        when (event.action) {
            Action.LEFT_CLICK_BLOCK -> {
                val block = event.clickedBlock ?: return
                session.corner1 = block.location
                Message.send(player, "setup-corner1-set")

                if (session.corner1 != null && session.corner2 != null) {
                    val visualizer = RoomsPvP.getInstance().getCuboidVisualizer()
                    visualizer.startVisualization(
                        player.name,
                        session.corner1!!,
                        session.corner2!!
                    )
                    Message.send(player, "setup-area-ready")
                }
            }

            Action.RIGHT_CLICK_BLOCK -> {
                val block = event.clickedBlock ?: return
                session.corner2 = block.location
                Message.send(player, "setup-corner2-set")

                if (session.corner1 != null && session.corner2 != null) {
                    val visualizer = RoomsPvP.getInstance().getCuboidVisualizer()
                    visualizer.startVisualization(
                        player.name,
                        session.corner1!!,
                        session.corner2!!
                    )
                    Message.send(player, "setup-area-ready")
                }
            }

            else -> {}
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val visualizer = RoomsPvP.getInstance().getCuboidVisualizer()
        visualizer.stopVisualization(event.player.name)
        setupManager.removeSession(event.player)
    }
}