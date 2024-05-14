package net.qilla.zombieshooter.StatSystem;

import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.NamespacedKey;

public enum EntityKey {
    MODIFIED_ENTITY(new NamespacedKey(ZombieShooter.getInstance(), "modified_entity"));

    final NamespacedKey namespacedKey;

    EntityKey(NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
    }
}
