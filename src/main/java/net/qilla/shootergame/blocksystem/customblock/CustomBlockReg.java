package net.qilla.shootergame.blocksystem.customblock;

import net.qilla.shootergame.ShooterGame;
import net.qilla.shootergame.blocksystem.blockdb.MineableData;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class CustomBlockReg {

    private final ShooterGame plugin;

    private final Map<Short, CustomBlock> customBlockRegistry = new HashMap<>();

    public CustomBlockReg(ShooterGame plugin) {
        this.plugin = plugin;
        registerAll();
    }

    private void registerAll() {
        for (CustomBlock customBlock : CustomBlock.values()) registerBlock(customBlock);
    }

    private void registerBlock(@NotNull final CustomBlock customBlock) {
        customBlockRegistry.put(customBlock.id(), customBlock);
    }

    @NotNull
    public Map<Short, CustomBlock> getRegistry() {
        return customBlockRegistry;
    }

    @NotNull
    public CustomBlock getFromRegistryID(final short blockID) {
        return customBlockRegistry.get(blockID);
    }

    @Nullable
    public CustomBlock getFromRegistryLoc(final Location blockLoc) {
        MineableData mineableData =  plugin.getBlockMapper().getMineableData(blockLoc);
        if(mineableData == null) return null;
        return customBlockRegistry.get(mineableData.blockID());
    }
}