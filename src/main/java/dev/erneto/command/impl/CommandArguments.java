package dev.erneto.command.impl;

import dev.erneto.RoomsPvP;
import dev.erneto.config.Lang;
import dev.erneto.selection.SelectionType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.builder.ArgumentNodeBuilder;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.Enums;

import java.util.ArrayList;
import java.util.Optional;

public class CommandArguments {

    public static final String PLAYER = "player";
    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String DUNGEON = "dungeon";
    public static final String KIT = "kit";
    public static final String STAGE = "stage";
    public static final String LEVEL = "level";
    public static final String AMOUNT = "amount";
    public static final String SPOT = "spot";
    public static final String REWARD = "reward";
    public static final String LOOT_CHEST = "lootchest";
    public static final String WEIGHT = "weight";
    public static final String STATE = "state";

    @NotNull
    public static ArgumentNodeBuilder<SelectionType> forSelectionType(@NotNull RoomsPvP plugin) {
        return Commands.argument(TYPE, (context, string) -> Enums.parse(string, SelectionType.class)
                        .orElseThrow(() -> CommandSyntaxException.custom(Lang.ERROR_COMMAND_INVALID_SELECTION_ARGUMENT))
                )
                .localized(CoreLang.COMMAND_ARGUMENT_NAME_TYPE)
                .suggestions((reader, context) -> Enums.getNames(SelectionType.class));
    }
}
