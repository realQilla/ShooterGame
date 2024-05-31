package net.qilla.shootergame.gunsystem.gunskeleton;

import net.qilla.shootergame.cooldown.GunCooldown;
import net.qilla.shootergame.gunsystem.guncreation.guntype.GunBase;
import net.qilla.shootergame.gunsystem.guncreation.GunPDC;
import net.qilla.shootergame.gunsystem.gunutil.CheckValid;
import net.qilla.shootergame.gunsystem.gunutil.GetFromGun;
import net.qilla.shootergame.util.SoundModel;
import net.qilla.shootergame.ShooterGame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class GunReload {

    private static BukkitRunnable currentReload;

    private final Player player;
    private final GunBase gunBase;
    private final ItemStack gunItem;

    private final String gunUniqueID;
    private PersistentDataContainer dataContainer;
    private int gunCapacity;
    private int gunMagazine;
    private int magazineBulletsRemaining;
    private int amountPerStage;

    public GunReload(@NotNull final Player player, @NotNull GunBase gunBase, @NotNull final ItemStack gunItem) {
        this.player = player;
        this.gunBase = gunBase;
        this.gunItem = gunItem;

        this.dataContainer = this.gunItem.getItemMeta().getPersistentDataContainer();
        this.gunUniqueID = this.dataContainer.get(GunPDC.GUN_UUID.getKey(), PersistentDataType.STRING);
        this.gunCapacity = dataContainer.get(GunPDC.GUN_CAPACITY.getKey(), PersistentDataType.INTEGER);
        this.gunMagazine = dataContainer.get(GunPDC.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER);
        this.magazineBulletsRemaining = getBulletsNeeded();
        this.amountPerStage = getAmountPerStage();
    }

    public void reloadMain() {
        if(canReload(player, dataContainer)) return;

        /**
         * Check if a reload is even possible by checking the current capacity
         */

        if(gunCapacity <= 0) {
            final SoundModel reloadSound = gunBase.getCosmeticMod().emptyCapacity();
            this.player.playSound(this.player.getLocation(), reloadSound.getSound(), reloadSound.getVolume(), reloadSound.getPitch());
            return;
        }

        /**
         * Only start a reload if there are bullets missing in the magazine
         */

        if (magazineBulletsRemaining > 0) {
            setReloading(true);
            currentReload = new ReloadRunnable(this.player, this.gunBase, this.gunUniqueID, magazineBulletsRemaining, amountPerStage);
            currentReload.runTaskTimer(ShooterGame.getInstance(), gunBase.getAmmunitionMod().ticksPerStage(), gunBase.getAmmunitionMod().ticksPerStage());
        }
    }

    private class ReloadRunnable extends BukkitRunnable {
        private final Player player;
        private final GunBase gunType;
        private final String gunUniqueID;
        private int magazineBulletsFill;
        private final int amountPerStage;

        public ReloadRunnable(@NotNull final Player player, @NotNull final GunBase gunType, @NotNull final String gunUniqueID, final int magazineBulletsFill, final int amountPerStage) {
            this.player = player;
            this.gunType = gunType;
            this.gunUniqueID = gunUniqueID;
            this.magazineBulletsFill = magazineBulletsFill;
            this.amountPerStage = amountPerStage;
        }

        @Override
        public void run() {
            ItemStack gunItem = player.getInventory().getItemInMainHand();

            if(!CheckValid.isValidBoth(gunItem) || !CheckValid.isSameGun(gunItem.getItemMeta().getPersistentDataContainer(), gunUniqueID)) {
                cancel();
                return;
            }
            final PersistentDataContainer dataContainer = gunItem.getItemMeta().getPersistentDataContainer();

            SoundModel reloadMagazine = gunType.getCosmeticMod().reloadMagazine();
            player.playSound(player.getLocation(), reloadMagazine.getSound(), reloadMagazine.getVolume(), reloadMagazine.getPitch());

            int amountInStage = Math.min(magazineBulletsFill, amountPerStage);
            magazineBulletsFill -= amountInStage;

            final int currentMagazine = dataContainer.get(GunPDC.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER);
            final int currentCapacity = dataContainer.get(GunPDC.GUN_CAPACITY.getKey(), PersistentDataType.INTEGER);

            updateMagazineDisplay(gunItem, gunType, amountInStage);
            updateAmmunitionMeta(gunItem, amountInStage, currentMagazine, currentCapacity);
            //GunDisplay.getDisplayMap(player).setCurrentMagazine(currentMagazine + amountInStage);
            //GunDisplay.getDisplayMap(player).setCurrentCapacity(currentCapacity - amountInStage);

            if(magazineBulletsFill <= 0) {
                final SoundModel reloadMagazineEnd = gunType.getCosmeticMod().reloadMagazineEnd();
                player.playSound(player.getLocation(), reloadMagazineEnd.getSound(), reloadMagazineEnd.getVolume(), reloadMagazineEnd.getPitch());

                setReloading(false);
                GunCooldown.normalCD(player, GunCooldown.ActionCooldown.RECENT_RELOAD, true);
                cancelReload();
            }
        }

        private void updateAmmunitionMeta(ItemStack gunItem, int amountInStage, int currentMagazine, int currentCapacity) {
            gunItem.editMeta(meta -> {
                PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

                dataContainer.set(GunPDC.GUN_CAPACITY.getKey(), PersistentDataType.INTEGER, currentCapacity - amountInStage);
                dataContainer.set(GunPDC.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER, currentMagazine + amountInStage);
            });
        }

        private void updateMagazineDisplay(ItemStack gunItem, GunBase gunType, int amountInStage) {
            gunItem.editMeta(meta -> {
                int currentMagazine = meta.getPersistentDataContainer().get(GunPDC.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER);
                ((Damageable) meta).setDamage((gunType.getAmmunitionMod().gunMagazine() - (currentMagazine + amountInStage)) * (gunItem.getType().getMaxDurability() / gunType.getAmmunitionMod().gunMagazine()));
            });
        }
    }
    private boolean canReload(Player player, PersistentDataContainer dataContainer) {
        return GunCooldown.overridableCD(player, GunCooldown.ActionCooldown.ACTION_PREVENTS_RELOAD, false) ||
                GunCooldown.normalCD(player, GunCooldown.ActionCooldown.RECENT_RELOAD, true) ||
                Boolean.TRUE.equals(dataContainer.get(GunPDC.GUN_RELOAD_STATUS.getKey(), PersistentDataType.BOOLEAN));
    }

    private int getBulletsNeeded() {
        return Math.min(gunCapacity, this.gunBase.getAmmunitionMod().gunMagazine() - gunMagazine);
    }

    private int getAmountPerStage() {
        return gunBase.getAmmunitionMod().gunMagazine() / gunBase.getAmmunitionMod().reloadStages();
    }

    private void setReloading(boolean status) {
        this.gunItem.editMeta(ItemMeta.class, itemMeta -> {
            itemMeta.getPersistentDataContainer().set(GunPDC.GUN_RELOAD_STATUS.getKey(), PersistentDataType.BOOLEAN, status);
            this.gunItem.setItemMeta(itemMeta);
        });
    }

    public void cancelReload() {
        if(currentReload != null) {
            currentReload.cancel();
            currentReload = null;
        }
    }
}