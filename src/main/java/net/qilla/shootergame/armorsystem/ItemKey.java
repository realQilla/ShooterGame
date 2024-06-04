package net.qilla.shootergame.armorsystem;

import net.qilla.shootergame.ShooterGame;
import org.bukkit.NamespacedKey;

public enum ItemKey {

    SET_ARMOR(new NamespacedKey(ShooterGame.getInstance(), "type_set")),
    TYPE_ARMOR(new NamespacedKey(ShooterGame.getInstance(), "type_armor")),

    TYPE_WEAPON(new NamespacedKey(ShooterGame.getInstance(), "type_weapon")),

    ITEM_STAT_DEFENSE(new NamespacedKey(ShooterGame.getInstance(), "item_stat_defense")),
    ITEM_STAT_MAX_HEALTH(new NamespacedKey(ShooterGame.getInstance(), "item_stat_max_health")),
    ITEM_STAT_REGENERATION(new NamespacedKey(ShooterGame.getInstance(), "item_stat_regeneration")),
    ITEM_STAT_DAMAGE(new NamespacedKey(ShooterGame.getInstance(), "item_stat_damage"));

    private final NamespacedKey key;

    ItemKey(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
