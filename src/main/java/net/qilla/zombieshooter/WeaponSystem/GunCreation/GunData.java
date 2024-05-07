package net.qilla.zombieshooter.WeaponSystem.GunCreation;

import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.NamespacedKey;

public enum GunData {

    TYPE_GUN(new NamespacedKey(ZombieShooter.getInstance(), "type_gun")),
    TIER_GUN(new NamespacedKey(ZombieShooter.getInstance(), "tier_gun")),
    GUN_AMMUNITION_CAPACITY(new NamespacedKey(ZombieShooter.getInstance(), "gun_ammunition_capacity")),
    GUN_MAGAZINE(new NamespacedKey(ZombieShooter.getInstance(), "gun_magazine")),
    GUN_FIRE_MODE(new NamespacedKey(ZombieShooter.getInstance(), "gun_fire_mode")),
    GUN_IS_RELOADING(new NamespacedKey(ZombieShooter.getInstance(), "gun_is_reloading"));

    final NamespacedKey key;

    GunData(NamespacedKey dataKey) {
        this.key = dataKey;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
