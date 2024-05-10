package net.qilla.zombieshooter.GunSystem.GunUtils;

import net.qilla.zombieshooter.GunSystem.GunCreation.GunData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class CheckValid {

    /**
     * Check the validity of the item existing and its meta.
     * @param item The item to check
     * @return Returns true if valid
     */

    public static boolean hasValidMeta(ItemStack item) {
        return item != null && item.hasItemMeta();
    }

    /**
     * Check the validity of the gun itself, checks its required PDC's.
     * @param dataContainer The items container to check
     * @return Returns true if valid
     */

    public static boolean isValidGun(PersistentDataContainer dataContainer) {
        return dataContainer.has(GunData.GUN_TYPE.getKey()) && dataContainer.has(GunData.GUN_TIER.getKey());
    }

    /**
     * Check the validity of the item existing, has metadata, and is a valid gun item.
     * @param item The item to check
     * @return Returns true if valid
     */

    public static boolean isValidBoth(ItemStack item) {
        if(!hasValidMeta(item)) return false;
        return isValidGun(item.getItemMeta().getPersistentDataContainer());
    }

    public static boolean isSameGun(PersistentDataContainer dataContainer, String gunUniqueID) {
        return Objects.equals(dataContainer.get(GunData.GUN_UUID.getKey(), PersistentDataType.STRING), gunUniqueID);
    }

    public static boolean hasUniqueIDPDC(PersistentDataContainer dataContainer) {
        return dataContainer.has(GunData.GUN_UUID.getKey(), PersistentDataType.STRING);
    }

    public static boolean hasMagazinePDC(PersistentDataContainer dataContainer) {
        return dataContainer.has(GunData.GUN_MAGAZINE.getKey(), PersistentDataType.INTEGER);
    }

    public static boolean hasCapacityPDC(PersistentDataContainer dataContainer) {
        return dataContainer.has(GunData.GUN_CAPACITY.getKey(), PersistentDataType.INTEGER);
    }

    public static boolean hasFireModePDC(PersistentDataContainer dataContainer) {
        return dataContainer.has(GunData.GUN_FIRE_MODE.getKey(), PersistentDataType.INTEGER);
    }

    public static boolean hasReloadPDC(PersistentDataContainer dataContainer) {
        return dataContainer.has(GunData.GUN_RELOAD_STATUS.getKey(), PersistentDataType.BOOLEAN);
    }
}
