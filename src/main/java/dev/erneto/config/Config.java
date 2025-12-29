package dev.erneto.config;

import dev.erneto.util.RoomUtils;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import static su.nightexpress.nightcore.util.Placeholders.URL_WIKI_ITEMS;

public class Config {

    public static final ConfigValue<NightItem> ITEMS_WAND_ITEM = ConfigValue.create("Items.WandItem",
            RoomUtils.getDefaultSelectionItem(),
            "Item used to select dungeon cuboids.",
            URL_WIKI_ITEMS
    );
}
