package net.qilla.shootergame.armorsystem;

import net.qilla.shootergame.ShooterGame;
import org.bukkit.NamespacedKey;

public enum ArmorKey {

    ARMOR_SET(new NamespacedKey(ShooterGame.getInstance(), "armor_type")),

    ITEM_STAT_DEFENSE(new NamespacedKey(ShooterGame.getInstance(), "item_stat_defense")),
    ITEM_STAT_MAX_HEALTH(new NamespacedKey(ShooterGame.getInstance(), "item_stat_max_health")),
    ITEM_STAT_REGENERATION(new NamespacedKey(ShooterGame.getInstance(), "item_stat_regeneration"));

    private final NamespacedKey key;

    ArmorKey(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
