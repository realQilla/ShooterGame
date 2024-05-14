package net.qilla.zombieshooter.GunSystem.GunSkeleton;

import net.qilla.zombieshooter.Cooldown.GunCooldown;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunRegistry;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunPDC;
import net.qilla.zombieshooter.GunSystem.GunUtils.CheckValid;
import net.qilla.zombieshooter.GunSystem.GunUtils.GetFromGun;
import net.qilla.zombieshooter.Utils.SoundModel;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class GunReload extends GunCore {

    private static final GunReload instance = new GunReload();
    private static BukkitRunnable currentReload;

    public void reloadMain(@NotNull Player player) {
        ItemStack gunItem = player.getInventory().getItemInMainHand();
        PersistentDataContainer dataContainer = gunItem.getItemMeta().getPersistentDataContainer();
        GunBase gunType = GunRegistry.getInstance().getGun(GetFromGun.typeID(dataContainer));
        String uniqueID = dataContainer.get(GunPDC.GUN_UUID.getKey(), PersistentDataType.STRING);

        if(canReload(player, dataContainer)) return;

        final int magazineBulletsFill = getBulletsNeeded(gunType, dataContainer); // Returns the smaller of the two, the remaining capacity, or the capacity minus the magazine
        final int amountPerStage = getAmountPerStage(gunType); // Divides the magazine by the amount of stages set for the gun

        /**
         * Check if a reload is even possible by checking the current capacity
         */

        if(dataContainer.get(GunPDC.GUN_CAPACITY.getKey(), PersistentDataType.INTEGER) <= 0) {
            SoundModel reloadSound = gunType.getCosmeticMod().emptyCapacity();
            player.playSound(player.getLocation(), reloadSound.getSound(), reloadSound.getVolume(), reloadSound.getPitch());
            return;
        }

        /**
         * Only start a reload if there are bullets missing in the magazine
         */

        if (magazineBulletsFill > 0) {
            setReloading(gunItem, true);
            currentReload = new ReloadRunnable(player, gunType, uniqueID, magazineBulletsFill, amountPerStage);
            currentReload.runTaskTimer(ZombieShooter.getInstance(), gunType.getAmmunitionMod().ticksPerStage(), gunType.getAmmunitionMod().ticksPerStage());
        }
    }

    private class ReloadRunnable extends BukkitRunnable {
        private final Player player;
        private final GunBase gunType;
        private final String gunUniqueID;
        private int magazineBulletsFill;
        private final int amountPerStage;

        public ReloadRunnable(Player player, GunBase gunType, String gunUniqueID, int magazineBulletsFill, int amountPerStage) {
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
            PersistentDataContainer dataContainer = gunItem.getItemMeta().getPersistentDataContainer();

            SoundModel reloadMagazine = gunType.getCosmeticMod().reloadMagazine();
            player.playSound(player.getLocation(), reloadMagazine.getSound(), reloadMagazine.getVolume(), reloadMagazine.getPitch());

            int amountInStage = Math.min(magazineBulletsFill, amountPerStage);
            magazineBulletsFill -= amountInStage;

            int currentMagazine = dataContainer.get(GunPDC.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER);
            int currentCapacity = dataContainer.get(GunPDC.GUN_CAPACITY.getKey(), PersistentDataType.INTEGER);

            updateMagazineDisplay(gunItem, gunType, amountInStage);
            updateAmmunitionMeta(gunItem, amountInStage, currentMagazine, currentCapacity);
            GunDisplay.getDisplayMap(player).setCurrentMagazine(currentMagazine + amountInStage);
            GunDisplay.getDisplayMap(player).setCurrentCapacity(currentCapacity - amountInStage);

            if(magazineBulletsFill <= 0) {
                SoundModel reloadMagazineEnd = gunType.getCosmeticMod().reloadMagazineEnd();
                player.playSound(player.getLocation(), reloadMagazineEnd.getSound(), reloadMagazineEnd.getVolume(), reloadMagazineEnd.getPitch());

                setReloading(gunItem, false);
                GunCooldown.getInstance().genericCooldown(player, GunCooldown.ActionCooldown.RECENT_RELOAD, true);
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
        return GunCooldown.getInstance().nonRemovingCooldown(player, GunCooldown.ActionCooldown.ACTION_PREVENTS_RELOAD, false) ||
                GunCooldown.getInstance().genericCooldown(player, GunCooldown.ActionCooldown.RECENT_RELOAD, true) ||
                Boolean.TRUE.equals(dataContainer.get(GunPDC.GUN_RELOAD_STATUS.getKey(), PersistentDataType.BOOLEAN));
    }

    private int getBulletsNeeded(GunBase gunType, PersistentDataContainer dataContainer) {
        return Math.min(dataContainer.get(GunPDC.GUN_CAPACITY.getKey(), PersistentDataType.INTEGER), gunType.getAmmunitionMod().gunMagazine() - dataContainer.get(GunPDC.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER));
    }

    private int getAmountPerStage(GunBase gunType) {
        return gunType.getAmmunitionMod().gunMagazine() / gunType.getAmmunitionMod().reloadStages();
    }

    private void setReloading(ItemStack gunItem, boolean status) {
        gunItem.editMeta(ItemMeta.class, itemMeta -> {
            itemMeta.getPersistentDataContainer().set(GunPDC.GUN_RELOAD_STATUS.getKey(), PersistentDataType.BOOLEAN, status);
            gunItem.setItemMeta(itemMeta);
        });
    }

    public void cancelReload() {
        if(currentReload != null) {
            currentReload.cancel();
            currentReload = null;
        }
    }

    public static GunReload getInstance() {
        return instance;
    }
}