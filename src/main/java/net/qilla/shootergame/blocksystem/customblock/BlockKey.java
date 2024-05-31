package net.qilla.shootergame.blocksystem.customblock;

import net.qilla.shootergame.ShooterGame;
import org.bukkit.NamespacedKey;

public final class BlockKey {

    private static final ShooterGame plugin = ShooterGame.getInstance();

    public static final NamespacedKey lockedBlock = new NamespacedKey(plugin, "block_locked");
    public static final NamespacedKey blockAmount = new NamespacedKey(plugin, "block_amount");
}
