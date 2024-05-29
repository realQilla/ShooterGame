package net.qilla.shootergame.blocksystem.customblock;

import net.qilla.shootergame.blocksystem.blockdb.BlockMapper;
import net.qilla.shootergame.blocksystem.blockdb.MineableData;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CustomBlockRegistry {

    private static final Map<Short, CustomBlock> customBlockRegistry = new HashMap<>();

    public static void registerAll() {
        customBlockRegistry.clear();
        for (CustomBlock customBlock : CustomBlock.values()) registerBlock(customBlock);
    }

    private static void registerBlock(@NotNull final CustomBlock customBlock) {
        customBlockRegistry.put(customBlock.id(), customBlock);
    }

    @NotNull
    public static Map<Short, CustomBlock> getRegistry() {
        return customBlockRegistry;
    }

    @NotNull
    public static CustomBlock getFromRegistryID(final short blockID) {
        return customBlockRegistry.get(blockID);
    }

    @Nullable
    public static CustomBlock getFromRegistryLoc(final Location blockLoc) {
        MineableData mineableData =  BlockMapper.getInstance().getMineableData(blockLoc);
        if(mineableData == null) return null;
        return customBlockRegistry.get(mineableData.blockID());
    }
}