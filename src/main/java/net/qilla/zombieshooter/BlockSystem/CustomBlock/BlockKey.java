package net.qilla.zombieshooter.BlockSystem.CustomBlock;

import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.NamespacedKey;

public class BlockKey {

    private static final ZombieShooter plugin = ZombieShooter.getInstance();

    public static final NamespacedKey lockedBlock = new NamespacedKey(plugin, "block_locked");
    public static final NamespacedKey blockAmount = new NamespacedKey(plugin, "block_amount");
}
