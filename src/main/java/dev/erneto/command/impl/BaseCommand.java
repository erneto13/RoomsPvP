package dev.erneto.command.impl;

import dev.erneto.RoomsPvP;
import dev.erneto.selection.SelectionType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.builder.HubNodeBuilder;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;

public class BaseCommand {

    public static void load(@NotNull RoomsPvP plugin, @NotNull HubNodeBuilder node) {

        node.branch(Commands.literal("reload")
                .description("recarga la config del pl")
                .permission("rooms.command.reload")
                .executes((ctx, args) -> {
                    plugin.doReload(ctx.getSender());
                    return true;
                })
        );

        node.branch(Commands.literal("wand")
                .playerOnly()
                .description("pues pa darteuna varita magica")
                .permission("rooms.command.wand")
                .withArguments(CommandArguments.forSelectionType(plugin))
                .executes((context, arguments) -> getWand(plugin, context, arguments))
        );
    }

    private static boolean getWand(@NotNull RoomsPvP plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        SelectionType type = arguments.get(CommandArguments.TYPE, SelectionType.class);

        plugin.getSelectionManager().startSelection(player, type);
        return true;
    }
}
