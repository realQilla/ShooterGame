package net.qilla.shootergame.gunsystem.guncreation;

import net.qilla.shootergame.ShooterGame;
import org.bukkit.NamespacedKey;

public enum GunPDC {

    GUN_TYPE(new NamespacedKey(ShooterGame.getInstance(), "gun_type")),
    GUN_TIER(new NamespacedKey(ShooterGame.getInstance(), "gun_tier")),
    GUN_UUID(new NamespacedKey(ShooterGame.getInstance(), "gun_uuid")),
    GUN_CAPACITY(new NamespacedKey(ShooterGame.getInstance(), "gun_capacity")),
    GUN_MAGAZINE(new NamespacedKey(ShooterGame.getInstance(), "gun_magazine")),
    GUN_FIRE_MODE(new NamespacedKey(ShooterGame.getInstance(), "gun_fire_mode")),
    GUN_RELOAD_STATUS(new NamespacedKey(ShooterGame.getInstance(), "gun_reload_status"));

    final NamespacedKey key;

    GunPDC(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
