package net.qilla.shootergame.gunsystem;

import net.qilla.shootergame.ShooterGame;
import net.qilla.shootergame.gunsystem.guncreation.GunPDC;
import net.qilla.shootergame.gunsystem.guncreation.guntype.GunBase;
import net.qilla.shootergame.gunsystem.gunutil.CheckValid;
import net.qilla.shootergame.gunsystem.gunskeleton.GunChangeMode;
import net.qilla.shootergame.gunsystem.gunskeleton.GunFire;
import net.qilla.shootergame.gunsystem.gunskeleton.GunReload;
import net.qilla.shootergame.gunsystem.gunutil.GetFromGun;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class WeaponListener implements Listener {

    private final ShooterGame plugin;

    public WeaponListener(ShooterGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (event.getHand() != EquipmentSlot.HAND || !CheckValid.isValidBoth(item)) return;

        event.setCancelled(true);

        final ItemStack gunItem = player.getInventory().getItemInMainHand();
        final GunBase gunBase = this.plugin.getGunRegistry().getGun(GetFromGun.typeID(gunItem.getItemMeta().getPersistentDataContainer()));

        if (event.getAction().isRightClick()) new GunFire(player, gunBase, gunItem).begin();
        if (event.getAction().isLeftClick()) new GunChangeMode(player, gunBase, gunItem).change();
    }

    @EventHandler
    public void onPlayerSwapHand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack offhandItem = event.getOffHandItem();

        if (!CheckValid.isValidBoth(offhandItem)) return;

        event.setCancelled(true);

        final ItemStack gunItem = player.getInventory().getItemInMainHand();
        final GunBase gunBase = this.plugin.getGunRegistry().getGun(GetFromGun.typeID(gunItem.getItemMeta().getPersistentDataContainer()));

        new GunReload(player, gunBase, gunItem).reloadMain();
    }

    @EventHandler
    public void changeWeapon(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack curItem = player.getInventory().getItem(event.getNewSlot());
        ItemStack prevItem = player.getInventory().getItem(event.getNewSlot());

        //GunReload.getInstance().cancelReload();
        if(CheckValid.isValidBoth(prevItem)) {
            if (prevItem.getItemMeta().getPersistentDataContainer().get(GunPDC.GUN_RELOAD_STATUS.getKey(), PersistentDataType.BOOLEAN)) {
                prevItem.editMeta(ItemMeta.class, meta -> {
                    meta.getPersistentDataContainer().set(GunPDC.GUN_RELOAD_STATUS.getKey(), PersistentDataType.BOOLEAN, false);
                    prevItem.setItemMeta(meta);
                });
            }
        }

        //GunDisplay.getDisplayMap(player).updateGunFields(curItem, true);
    }

    @EventHandler
    public void clickInventory(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();

        if (CheckValid.isValidBoth(clickedItem)) {
            if (clickedItem.getItemMeta().getPersistentDataContainer().get(GunPDC.GUN_RELOAD_STATUS.getKey(), PersistentDataType.BOOLEAN)) {
                clickedItem.editMeta(ItemMeta.class, meta -> {
                    meta.getPersistentDataContainer().set(GunPDC.GUN_RELOAD_STATUS.getKey(), PersistentDataType.BOOLEAN, false);
                    clickedItem.setItemMeta(meta);
                });
            }
        }
    }
}
