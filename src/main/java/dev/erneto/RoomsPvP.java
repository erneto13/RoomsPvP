package dev.erneto;

import dev.erneto.command.impl.BaseCommand;
import dev.erneto.config.Config;
import dev.erneto.config.Perms;
import dev.erneto.selection.SelectionManager;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.commands.command.NightCommand;
import su.nightexpress.nightcore.config.PluginDetails;

public final class RoomsPvP extends NightPlugin {

    private SelectionManager selectionManager;

    @Override
    @NotNull
    protected PluginDetails getDefaultDetails() {
        return PluginDetails.create("Rooms", new String[]{"rooms"})
                .setConfigClass(Config.class)
                .setPermissionsClass(Perms.class);
    }

    @Override
    public void enable() {

        this.initCommands();

        this.selectionManager = new SelectionManager(this);
        this.selectionManager.setup();

    }

    @Override
    public void disable() {
        if (this.selectionManager != null) this.selectionManager.shutdown();

    }

    public void initCommands() {
        this.rootCommand = NightCommand.forPlugin(this, builder -> {
            BaseCommand.load(this, builder);
        });
    }

    // Getters
    @NotNull
    public SelectionManager getSelectionManager() {
        return this.selectionManager;
    }
}
