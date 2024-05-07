package net.qilla.zombieshooter.WeaponSystem;

import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunData;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunRegistry;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.UniqueID;
import net.qilla.zombieshooter.WeaponSystem.Systems.GunChangeMode;
import net.qilla.zombieshooter.WeaponSystem.Systems.GunFire;
import net.qilla.zombieshooter.WeaponSystem.Systems.GunReload;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemListener implements Listener {


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (event.getHand() != EquipmentSlot.HAND) return;
        if (item == null || !item.hasItemMeta()) return;

        PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();

        if(!dataContainer.has(GunData.TYPE_GUN.getKey()) && dataContainer.has(GunData.TIER_GUN.getKey())) return;

        if(event.getAction().isRightClick()) {
            event.setCancelled(true);
            UniqueID uniqueID = new UniqueID(dataContainer.get(GunData.TYPE_GUN.getKey(), PersistentDataType.STRING), dataContainer.get(GunData.TIER_GUN.getKey(), PersistentDataType.STRING));
            new GunFire().onShoot(event, GunRegistry.getGun(uniqueID));
        }

        if(event.getAction().isLeftClick()) {
            event.setCancelled(true);
            UniqueID uniqueID = new UniqueID(dataContainer.get(GunData.TYPE_GUN.getKey(), PersistentDataType.STRING), dataContainer.get(GunData.TIER_GUN.getKey(), PersistentDataType.STRING));
            new GunChangeMode().rotate(player, item, GunRegistry.getGun(uniqueID));
        }
    }

    @EventHandler
    public void onPlayerSwapHand(PlayerSwapHandItemsEvent event) {
        ItemStack offhandItem = event.getOffHandItem();

        if (!offhandItem.hasItemMeta()) return;

        PersistentDataContainer dataContainer = offhandItem.getItemMeta().getPersistentDataContainer();

        if(dataContainer.has(GunData.TYPE_GUN.getKey()) && dataContainer.has(GunData.TIER_GUN.getKey())) {
            event.setCancelled(true);
            UniqueID uniqueID = new UniqueID(dataContainer.get(GunData.TYPE_GUN.getKey(), PersistentDataType.STRING), dataContainer.get(GunData.TIER_GUN.getKey(), PersistentDataType.STRING));
            new GunReload().generalReload(event, GunRegistry.getGun(uniqueID));
        }
    }

    @EventHandler
    public void changeWeapon(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        int curSlot = event.getNewSlot();
        int prevSlot = event.getNewSlot();
        ItemStack curItem = player.getInventory().getItem(curSlot);
        ItemStack prevItem = player.getInventory().getItem(prevSlot);

        if (prevItem != null && prevItem.hasItemMeta() && prevItem.getItemMeta().getPersistentDataContainer().has(GunData.GUN_IS_RELOADING.getKey())) {
            if (prevItem.getItemMeta().getPersistentDataContainer().get(GunData.GUN_IS_RELOADING.getKey(), PersistentDataType.BOOLEAN)) {
                prevItem.editMeta(ItemMeta.class, meta -> {
                    meta.getPersistentDataContainer().set(GunData.GUN_IS_RELOADING.getKey(), PersistentDataType.BOOLEAN, false);
                    prevItem.setItemMeta(meta);
                });
            }
        }

        if (curItem == null || !curItem.hasItemMeta() || !curItem.getItemMeta().getPersistentDataContainer().has(GunData.GUN_AMMUNITION_CAPACITY.getKey())) {
            setAmmoDisplay(player, 0);
            return;
        }

        setAmmoDisplay(player, curItem.getItemMeta().getPersistentDataContainer().get(GunData.GUN_AMMUNITION_CAPACITY.getKey(), PersistentDataType.INTEGER));
    }

    private void setAmmoDisplay(Player player, int ammo) {
        if (ammo < 0) ammo = 0;
        player.setLevel(ammo);
        player.setExp(0);
    }

    @EventHandler
    public void playerDamageWeapon(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(GunData.TYPE_GUN.getKey())) {
            event.setCancelled(true);
        }
    }
}
