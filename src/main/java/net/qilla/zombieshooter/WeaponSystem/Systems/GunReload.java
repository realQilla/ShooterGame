package net.qilla.zombieshooter.WeaponSystem.Systems;

import net.qilla.zombieshooter.Cooldown.GunCooldown;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunData;
import net.qilla.zombieshooter.WeaponSystem.SoundModel;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class GunReload {

    public void generalReload(PlayerSwapHandItemsEvent event, GunBase gunType) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        ItemStack gunItem = player.getInventory().getItemInMainHand();
        PersistentDataContainer dataContainer = gunItem.getItemMeta().getPersistentDataContainer();

        if (dataContainer.get(GunData.GUN_IS_RELOADING.getKey(), PersistentDataType.BOOLEAN)) return;
        if(GunCooldown.getInstance().nonRemovingCooldown(player, GunCooldown.ActionCooldown.ACTION_PREVENTS_RELOAD, false)) return;

        /**
         * Simple grabbing of the gun's default required information
         */

        int currentCapacity = dataContainer.get(GunData.GUN_AMMUNITION_CAPACITY.getKey(), PersistentDataType.INTEGER);
        final int totalMagazineCapacity = gunType.getAmmunitionMod().gunMagazine();
        final int setReloadStages = gunType.getAmmunitionMod().reloadStages();

        /**
         * Divide the reload amount into stages and
         * subtract the gun capacity by the magazine size
         */

        final int amountPerStage = (int) Math.ceil((double) totalMagazineCapacity / setReloadStages);
        int bulletUntilFull = Math.min(currentCapacity,gunType.getAmmunitionMod().gunMagazine() - dataContainer.get(GunData.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER));

        if (bulletUntilFull > 0) {
            setReloadingStatus(gunItem, true);

            new BukkitRunnable() {
                int remainingBullets = bulletUntilFull;

                @Override
                public void run() {
                    ItemStack updatedGun = player.getInventory().getItemInMainHand();
                    PersistentDataContainer updatedDataContainer = updatedGun.getItemMeta().getPersistentDataContainer();
                    if (checkReloading(updatedGun)) {
                        GunCooldown.getInstance().nonRemovingCooldown(player, GunCooldown.ActionCooldown.RECENT_RELOAD, true);
                        cancel();
                        return;
                    }

                    int updatedMagazine = updatedDataContainer.get(GunData.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER);
                    int updatedCapacity = updatedDataContainer.get(GunData.GUN_AMMUNITION_CAPACITY.getKey(), PersistentDataType.INTEGER);

                    int amountToAdd = Math.min(remainingBullets, amountPerStage);
                    remainingBullets -= amountToAdd;

                    updateAmmunitionMeta(updatedGun, updatedMagazine, updatedCapacity, amountToAdd);
                    updateCapacityDisplay(player, (updatedCapacity - amountToAdd));
                    updateMagazineDisplay(updatedGun, gunType, updatedMagazine, amountToAdd);

                    SoundModel reloadMagSound = gunType.getCosmeticMod().reloadMagazine();
                    player.playSound(player.getLocation(), reloadMagSound.getSound(), reloadMagSound.getVolume(), reloadMagSound.getPitch());

                    if(remainingBullets <= 0) {
                        setReloadingStatus(updatedGun, false);
                        GunCooldown.getInstance().genericCooldown(player, GunCooldown.ActionCooldown.RECENT_RELOAD, true);

                        SoundModel reloadMagEndSound = gunType.getCosmeticMod().reloadMagazineEnd();
                        player.playSound(player.getLocation(), reloadMagEndSound.getSound(), reloadMagEndSound.getVolume(), reloadMagEndSound.getPitch());
                        cancel();
                    }
                }
            }.runTaskTimer(ZombieShooter.getInstance(), 5, gunType.getAmmunitionMod().ticksPerStage());
        }
    }
    private void setReloadingStatus(ItemStack gunItem, boolean status) {
        gunItem.editMeta(ItemMeta.class, meta -> {
            meta.getPersistentDataContainer().set(GunData.GUN_IS_RELOADING.getKey(), PersistentDataType.BOOLEAN, status);
            gunItem.setItemMeta(meta);
        });
    }

    private boolean checkReloading(ItemStack gunItem) {
        return Boolean.FALSE.equals(gunItem.getItemMeta().getPersistentDataContainer().get(GunData.GUN_IS_RELOADING.getKey(), PersistentDataType.BOOLEAN));
    }

    private void updateAmmunitionMeta(ItemStack gunItem, int currentMagazine, int currentCapacity, int bulletsReloaded) {
        gunItem.editMeta(ItemMeta.class, meta -> {
            meta.getPersistentDataContainer().set(GunData.GUN_AMMUNITION_CAPACITY.getKey(), PersistentDataType.INTEGER, currentCapacity - bulletsReloaded);
            meta.getPersistentDataContainer().set(GunData.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER, currentMagazine + bulletsReloaded);
            gunItem.setItemMeta(meta);
        });
    }

    private void updateMagazineDisplay(ItemStack gunItem, GunBase gunType, int currentMagazine, int bulletsReloaded) {
        Damageable damageable = (Damageable) gunItem.getItemMeta();
        damageable.setDamage((int) Math.ceil((double) gunType.getAmmunitionMod().gunMagazine() - (currentMagazine + bulletsReloaded)) * (gunItem.getType().getMaxDurability() / gunType.getAmmunitionMod().gunMagazine()));
        gunItem.setItemMeta(damageable);
    }

    private void updateCapacityDisplay(Player player, int ammoAmount) {
        player.setLevel(ammoAmount);
        player.setExp(0);
    }
}