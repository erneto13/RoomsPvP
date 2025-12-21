package dev.erneto.utils

import dev.erneto.storage.file.FileManager
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object Message {
    private val miniMessage = MiniMessage.miniMessage()

    fun getMsg(path: String): String {
        return FileManager.get("lang")?.getString(path) ?: "Message not found: $path"
    }

    fun parse(msg : String, player: Player) : TextComponent {
        return miniMessage.deserialize(placeholder(msg, player)) as TextComponent
    }

    fun parse(msg : String) : TextComponent {
        return miniMessage.deserialize(msg) as TextComponent
    }

    fun send(player: Player, path: String) {
        player.sendMessage(parse(getMsg(path), player))
    }

    fun send(sender: CommandSender, path: String){
        sender.sendMessage(parse(getMsg(path)))
    }

    fun placeholder(msg: String, player: Player) : String {
        return PlaceholderAPI.setPlaceholders(player, msg)
    }

    fun title(title: String, subtitle: String = ""): Pair<Component, Component> {
        return parse(title) to parse(subtitle)
    }

    fun actionBar(message: String): Component {
        return parse(message)
    }
}
