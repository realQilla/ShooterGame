package net.qilla.shootergame.util;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ItemManagement {

    public static void giveItem(@NotNull Player player, List<ItemStack> itemList) {
        for (ItemStack item : itemList) {
            if (player.getInventory().firstEmpty() == -1) {
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
            } else {
                player.getInventory().addItem(item);
            }
        }
    }

    public static void giveItem(@NotNull Player player, ItemStack item) {
        if (player.getInventory().firstEmpty() == -1) {
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
        } else {
            player.getInventory().addItem(item);
        }
    }

    public static boolean subtractKey(@NotNull Player player, @NotNull ItemStack item, @NotNull NamespacedKey dataKey) {
        if(!item.hasItemMeta()) return false;
        if(!item.getItemMeta().getPersistentDataContainer().has(dataKey)) return false;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

        meta.getPersistentDataContainer().set(dataKey, PersistentDataType.INTEGER, dataContainer.get(dataKey, PersistentDataType.INTEGER) - 1);
        item.setItemMeta(meta);
        player.getInventory().setItemInMainHand(item);
        return true;
    }

    public static boolean updateKey(@NotNull Player player, @NotNull ItemStack item, @NotNull NamespacedKey dataKey, int newValue) {
        if(!item.hasItemMeta()) return false;
        if(!item.getItemMeta().getPersistentDataContainer().has(dataKey)) return false;

        ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().set(dataKey, PersistentDataType.INTEGER, newValue);
        item.setItemMeta(meta);
        player.getInventory().setItemInMainHand(item);
        return true;
    }
}
