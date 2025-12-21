package dev.erneto.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage

object Message {
    private val miniMessage = MiniMessage.miniMessage()

    private val PRIMARY = TextColor.fromHexString("#FF6B6B")
    private val SECONDARY = TextColor.fromHexString("#4ECDC4")
    private val SUCCESS = TextColor.fromHexString("#95E1D3")
    private val ERROR = TextColor.fromHexString("#F38181")
    private val INFO = TextColor.fromHexString("#A8E6CF")

    private const val PREFIX = "<gradient:#FF6B6B:#4ECDC4><bold>RoomsPvP</bold></gradient> <gray>»</gray> "

    fun parse(text: String): Component {
        return miniMessage.deserialize(text)
    }

    fun success(message: String): Component {
        return parse("$PREFIX<#95E1D3>$message")
    }

    fun error(message: String): Component {
        return parse("$PREFIX<#F38181>✘ $message")
    }

    fun info(message: String): Component {
        return parse("$PREFIX<#A8E6CF>$message")
    }

    fun warning(message: String): Component {
        return parse("$PREFIX<#FFE66D>⚠ $message")
    }

    fun raw(message: String): Component {
        return parse(message)
    }

    fun title(title: String, subtitle: String = ""): Pair<Component, Component> {
        return parse(title) to parse(subtitle)
    }

    fun actionBar(message: String): Component {
        return parse(message)
    }
}