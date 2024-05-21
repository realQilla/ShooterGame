package net.qilla.zombieshooter.BlockSystem.CustomBlock;

import net.qilla.zombieshooter.BlockSystem.BlockDatabase.NodeMapping;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CustomBlockRegistry {

    private static final Map<Short, CustomBlock> customBlockRegistry = new HashMap<>();

    public static void registerAll() {
        for (CustomBlock customBlock : CustomBlock.values()) {
            registerBlock(customBlock);
        }
    }

    public static void registerBlock(@NotNull final CustomBlock customBlock) {
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
        Short blockID = NodeMapping.getInstance().getBlockID(blockLoc);
        if(blockID == null) return null;
        return getFromRegistryID(blockID);
    }
}