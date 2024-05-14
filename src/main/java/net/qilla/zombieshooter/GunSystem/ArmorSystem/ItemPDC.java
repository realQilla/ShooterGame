package net.qilla.zombieshooter.GunSystem.ArmorSystem;

import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.NamespacedKey;

public enum ItemPDC {

    ITEM_STAT_DEFENCE(new NamespacedKey(ZombieShooter.getInstance(), "item_stat_defence")),
    ITEM_STAT_MAX_HEALTH(new NamespacedKey(ZombieShooter.getInstance(), "item_stat_max_health")),
    ITEM_STAT_REGENERATION(new NamespacedKey(ZombieShooter.getInstance(), "item_stat_regeneration"));

    private final NamespacedKey key;

    ItemPDC(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
