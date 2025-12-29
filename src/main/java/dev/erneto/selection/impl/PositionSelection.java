package dev.erneto.selection.impl;

import dev.erneto.config.Lang;
import dev.erneto.selection.SelectionType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

import java.util.LinkedHashSet;

public class PositionSelection extends Selection {

    private final LinkedHashSet<BlockPos> positions;

    public PositionSelection() {
        super(SelectionType.POSITION);
        this.positions = new LinkedHashSet<>();
    }

    @Override
    public void clear() {
        this.positions.clear();
    }

    @Override
    public boolean isIncompleted() {
        return this.positions.isEmpty();
    }

    @Override
    public void onSelect(@NotNull Player player, @NotNull BlockPos pos, @NotNull Action action) {
        if (action == Action.RIGHT_CLICK_BLOCK) {
            this.positions.remove(pos);
            Lang.SELECTION_INFO_POSITION_REMOVE.message().send(player);
        } else {
            this.positions.add(pos);
            Lang.SELECTION_INFO_POSITION_ADD.message().send(player);
        }
    }

    @NotNull
    public LinkedHashSet<BlockPos> getPositions() {
        return this.positions;
    }
}
