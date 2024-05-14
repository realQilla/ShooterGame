package net.qilla.zombieshooter.GunSystem.GunCreation;

import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.NamespacedKey;

public enum GunPDC {

    GUN_TYPE(new NamespacedKey(ZombieShooter.getInstance(), "gun_type")),
    GUN_TIER(new NamespacedKey(ZombieShooter.getInstance(), "gun_tier")),
    GUN_UUID(new NamespacedKey(ZombieShooter.getInstance(), "gun_uuid")),
    GUN_CAPACITY(new NamespacedKey(ZombieShooter.getInstance(), "gun_capacity")),
    GUN_MAGAZINE(new NamespacedKey(ZombieShooter.getInstance(), "gun_magazine")),
    GUN_FIRE_MODE(new NamespacedKey(ZombieShooter.getInstance(), "gun_fire_mode")),
    GUN_RELOAD_STATUS(new NamespacedKey(ZombieShooter.getInstance(), "gun_reload_status"));

    final NamespacedKey key;

    GunPDC(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
