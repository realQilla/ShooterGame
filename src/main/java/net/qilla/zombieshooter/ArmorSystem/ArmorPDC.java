package net.qilla.zombieshooter.ArmorSystem;

import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.NamespacedKey;

public enum ArmorPDC {

    ARMOR_SET(new NamespacedKey(ZombieShooter.getInstance(), "armor_type")),

    ITEM_STAT_DEFENSE(new NamespacedKey(ZombieShooter.getInstance(), "item_stat_defense")),
    ITEM_STAT_MAX_HEALTH(new NamespacedKey(ZombieShooter.getInstance(), "item_stat_max_health")),
    ITEM_STAT_REGENERATION(new NamespacedKey(ZombieShooter.getInstance(), "item_stat_regeneration"));

    private final NamespacedKey key;

    ArmorPDC(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
