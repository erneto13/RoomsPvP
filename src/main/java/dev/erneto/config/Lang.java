package dev.erneto.config;

import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;

import static dev.erneto.Placeholders.GENERIC_VALUE;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class Lang implements LangContainer {

    public static final MessageLocale SELECTION_INFO_CUBOID = LangEntry.builder("Selection.Info.Cuboid").chatMessage(
            GRAY.wrap("Set " + SOFT_YELLOW.wrap("#" + GENERIC_VALUE) + " position.")
    );

    public static final MessageLocale ERROR_COMMAND_INVALID_SELECTION_ARGUMENT = LangEntry.builder("Command.Syntax.InvalidSelectionType").chatMessage(
            GRAY.wrap(SOFT_RED.wrap("nan") + " is not a valid type!"));


    public static final MessageLocale SELECTION_INFO_POSITION_ADD = LangEntry.builder("Selection.Info.PositionAdd").chatMessage(
            GRAY.wrap("Added block position.")
    );

    public static final MessageLocale SELECTION_INFO_POSITION_REMOVE = LangEntry.builder("Selection.Info.PositionRemove").chatMessage(
            GRAY.wrap("Removed block position.")
    );

    public static final MessageLocale SETUP_SELECTION_ACTIVATED = LangEntry.builder("Setup.Selection.Activated").chatMessage(
            GRAY.wrap("Selection mode " + GREEN.wrap("activated") + "."));
}
