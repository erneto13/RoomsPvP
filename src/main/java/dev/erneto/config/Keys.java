package dev.erneto.config;

import dev.erneto.RoomsPvP;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class Keys {
    public static NamespacedKey roomWand;

    public static void load(@NotNull RoomsPvP plugin) {
        roomWand = new NamespacedKey(plugin, "room_wand");
    }

    public static void clear() {
        roomWand = null;
    }

}
