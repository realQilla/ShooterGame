package net.qilla.shootergame.statsystem;

import net.qilla.shootergame.ShooterGame;
import org.bukkit.NamespacedKey;

public enum EntityKey {
    MODIFIED_ENTITY(new NamespacedKey(ShooterGame.getInstance(), "modified_entity"));

    final NamespacedKey namespacedKey;

    EntityKey(NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
    }
}
