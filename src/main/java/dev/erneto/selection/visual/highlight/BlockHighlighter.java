package dev.erneto.selection.visual.highlight;

import dev.erneto.RoomsPvP;
import dev.erneto.selection.visual.FakeEntity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.EntityUtil;

import java.util.*;

public abstract class BlockHighlighter {

    protected final RoomsPvP plugin;

    private final Map<UUID, List<FakeEntity>> entityMap;

    public BlockHighlighter(@NotNull RoomsPvP plugin) {
        this.plugin = plugin;
        this.entityMap = new HashMap<>();
    }

    public void clear() {
        this.plugin.getServer().getOnlinePlayers().forEach(this::removeVisuals);
        this.entityMap.clear();
    }

    @NotNull
    private List<FakeEntity> getEntityMap(@NotNull UUID playerId) {
        return this.entityMap.computeIfAbsent(playerId, k -> new ArrayList<>());
    }

    protected int nextEntityId() {
        return EntityUtil.nextEntityId();
    }

    public void removeVisuals(@NotNull Player player) {
        List<FakeEntity> entities = this.entityMap.remove(player.getUniqueId());
        if (entities == null) return;

        this.destroyEntity(player, new ArrayList<>(entities));
    }

    public void addVisualBlock(@NotNull Player player, @NotNull Location location, @NotNull BlockData blockData, @NotNull ChatColor color, float size) {
        List<FakeEntity> entities = this.getEntityMap(player.getUniqueId());

        float offset = 1f - size;
        float shift = 0.5f * offset;

        location.setX(location.getBlockX() + shift);
        location.setY(location.getBlockY() + shift);
        location.setZ(location.getBlockZ() + shift);

        int entityID = this.nextEntityId();

        FakeEntity entity = this.spawnVisualBlock(entityID, player, location, blockData, color, size);
        entities.add(entity);
    }

    @NotNull
    protected abstract FakeEntity spawnVisualBlock(int entityID, @NotNull Player player, @NotNull Location location, @NotNull BlockData blockData, @NotNull ChatColor color, float size);

    protected abstract void destroyEntity(@NotNull Player player, @NotNull List<FakeEntity> idList);
}
