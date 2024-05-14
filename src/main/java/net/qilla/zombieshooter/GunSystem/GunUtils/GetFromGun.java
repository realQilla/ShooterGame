package net.qilla.zombieshooter.GunSystem.GunUtils;

import net.qilla.zombieshooter.GunSystem.GunCreation.GunPDC;
import net.qilla.zombieshooter.GunSystem.GunCreation.Mod.GunID;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GetFromGun {

    public static GunID typeID(PersistentDataContainer dataContainer) {
        return new GunID(dataContainer.get(GunPDC.GUN_TYPE.getKey(), PersistentDataType.STRING), dataContainer.get(GunPDC.GUN_TIER.getKey(), PersistentDataType.STRING));
    }
}
