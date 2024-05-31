package net.qilla.shootergame.blocksystem.customblock;

import net.qilla.shootergame.blocksystem.customblock.miningsystem.MiningInstance;
import net.qilla.shootergame.blocksystem.customblock.miningsystem.MiningID;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MiningReg {

    private final Map<MiningID, MiningInstance> mineInstanceMap = new HashMap<>();
    private final Collection<Integer> blockLockMap = new HashSet<>();
    private final Map<Integer, Integer> nodeResources = new HashMap<>();

    /**
     * Pull the instance of the currently mined block.
     * @param miningID The identifier for the mined block.
     * @return A MiningInstance or null if one doesn't exist.
     */

    @Nullable
    public MiningInstance getMiningInstance(@NotNull MiningID miningID) {
        return mineInstanceMap.get(miningID);
    }

    /**
     * Check if there is currently a mining instance using the block identifier.
     * @param miningID The identifier for the mined block.
     * @return True if an instance currently exists.
     */

    public boolean isMining(@NotNull MiningID miningID) {
        return mineInstanceMap.containsKey(miningID);
    }

    /**
     * Create a new mining instance for a specified block identifier.
     * @param miningID The identifier for the mined block.
     * @param miningInstance A new Mining Instance to use.
     */

    public void addMining(@NotNull MiningID miningID, @NotNull MiningInstance miningInstance) {
        mineInstanceMap.put(miningID, miningInstance);
    }

    /**
     * Removes an instance from the mining map.
     * @param miningID The identifier for the mined block.
     */

    public void removeMining(@NotNull MiningID miningID) {
        mineInstanceMap.remove(miningID);
    }

    /**
     * Checks if a block is currently marked as locked.
     * @param block A block to check.
     * @return True if the block is currently marked as locked.
     */

    public boolean isBlockLocked(Block block) {
        return blockLockMap.contains(block.hashCode());
    }

    /**
     * Marks a block as locked to prevent future mining.
     * @param block A block to lock.
     */

    public void lockBlock(@NotNull Block block) {
        blockLockMap.add(block.hashCode());
    }

    /**
     * Unlocks a block to allow future mining.
     * @param block A block to unlock.
     */

    public void unlockBlock(@NotNull Block block) {
        blockLockMap.remove(block.hashCode());
    }

    /**
     * Checks if a node is already registered and has an amount value.
     * @param block A block to verify.
     * @return Returns true if the block exists in the map.
     */

    public boolean isNodeMapped(@NotNull Block block) {
        return nodeResources.containsKey(block.hashCode());
    }

    /**
     * Sets or creates a node with a set amount.
     * @param block A block to set an amount for.
     * @param amount An amount of ores to set the node to.
     */

    public void setNode(@NotNull Block block, int amount) {
        nodeResources.put(block.hashCode(), amount);
    }

    /**
     * Subtracts a nodes amount, if it reaches zero, removes it from  the map.
     * @param block A block to subtract.
     */

    public int subtractNode(@NotNull Block block) {
        final int nodeAmount = nodeResources.get(block.hashCode()) - 1;
        if(nodeAmount <= 0) nodeResources.remove(block.hashCode());
        else setNode(block, nodeAmount);
        return nodeAmount;
    }
}
