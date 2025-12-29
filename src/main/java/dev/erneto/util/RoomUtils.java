package dev.erneto.util;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.BOLD;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.DARK_GRAY;
import static su.nightexpress.nightcore.util.text.tag.Tags.LIGHT_GRAY;
import static su.nightexpress.nightcore.util.text.tag.Tags.LIGHT_YELLOW;

public class RoomUtils {
    @NotNull
    public static NightItem getDefaultSelectionItem() {
        return new NightItem(Material.BLAZE_ROD)
                .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Selection Wand")))
                .setLore(Lists.newList(
                        DARK_GRAY.wrap("(Drop to exit selection mode)"),
                        "",
                        LIGHT_YELLOW.wrap("[▶] ") + LIGHT_GRAY.wrap("Left-Click to " + LIGHT_YELLOW.wrap("set 1st") + " point."),
                        LIGHT_YELLOW.wrap("[▶] ") + LIGHT_GRAY.wrap("Right-Click to " + LIGHT_YELLOW.wrap("set 2nd") + " point.")
                ));
    }
}
