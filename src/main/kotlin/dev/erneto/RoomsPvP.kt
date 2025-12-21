package dev.erneto

import revxrsal.zapper.ZapperJavaPlugin

class RoomsPvP : ZapperJavaPlugin() {

    companion object {
        private lateinit var instance: RoomsPvP
        fun getInstance(): RoomsPvP = instance
    }

    override fun onEnable() {
        logger.info("RoomsPvP has been enabled. Developed by erneto13")
    }

    override fun onDisable() {
        logger.info("RoomsPvP has been disabled. Developed by erneto13")
    }
}
