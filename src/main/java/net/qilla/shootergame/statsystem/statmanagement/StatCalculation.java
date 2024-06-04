package net.qilla.shootergame.statsystem.statmanagement;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.shootergame.ShooterGame;
import net.qilla.shootergame.armorsystem.ItemKey;
import net.qilla.shootergame.armorsystem.armormodel.ArmorSet;
import net.qilla.shootergame.armorsystem.armormodel.ArmorType;
import net.qilla.shootergame.armorsystem.armortype.ArmorBase;
import net.qilla.shootergame.statsystem.stat.StatContainer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class StatCalculation {

    private final ShooterGame plugin = ShooterGame.getInstance();

    public StatContainer activeArmor(@NotNull final Player player) {
        final StatContainer statContainer = new StatContainer();
        for(ItemStack item : player.getInventory().getArmorContents()) {
            if(!isValidArmor(item)) continue;

            final ArmorBase armorBase = getArmorBase(item);
            Arrays.stream(armorBase.getStats()).forEach(stat -> statContainer.getContainer().compute(stat.getType(), (k, v) -> v == null ? stat : stat.setValue(stat.getValue() + v.getValue())));
        }
        return statContainer;
    }

    private ArmorBase getArmorBase(final ItemStack item) {
        final String set = item.getItemMeta().getPersistentDataContainer().get(ItemKey.SET_ARMOR.getKey(), PersistentDataType.STRING);
        final String type = item.getItemMeta().getPersistentDataContainer().get(ItemKey.TYPE_ARMOR.getKey(), PersistentDataType.STRING);
        return plugin.getArmorRegistry().getPiece(ArmorSet.valueOf(set), ArmorType.valueOf(type));
    }

    private boolean isValidArmor(final ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(ItemKey.SET_ARMOR.getKey(), PersistentDataType.STRING);
    }
}
