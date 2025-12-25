package dev.erneto.listener

import dev.erneto.manager.RoomLifecycleManager
import dev.erneto.manager.RoomManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerQuitEvent

class RoomListener(private val lifecycleManager: RoomLifecycleManager) : Listener {

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        lifecycleManager.handlePlayerDisconnect(event.player)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val room = RoomManager.getRoomByLocation(event.block.location) ?: return

        if (room.owner == null) {
            event.isCancelled = true
            return
        }

        if (!room.isOwner(event.player.uniqueId)) {
            event.isCancelled = true
            return
        }

        room.updateActivity()

        room.getCurrentNucleus()?.let { nucleus ->
            if (event.block.location.distance(nucleus) < 1.0) {
                lifecycleManager.handleNucleusDestroyed(room)
            }
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val room = RoomManager.getRoomByLocation(event.block.location) ?: return

        if (room.owner == null) {
            event.isCancelled = true
            return
        }

        if (!room.isOwner(event.player.uniqueId)) {
            event.isCancelled = true
            return
        }

        room.updateActivity()
    }
}