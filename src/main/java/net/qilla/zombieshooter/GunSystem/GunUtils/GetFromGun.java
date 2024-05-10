package net.qilla.zombieshooter.GunSystem.GunUtils;

import net.qilla.zombieshooter.GunSystem.GunCreation.GunData;
import net.qilla.zombieshooter.GunSystem.GunCreation.Mod.GunID;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GetFromGun {

    public static GunID typeID(PersistentDataContainer dataContainer) {
        return new GunID(dataContainer.get(GunData.GUN_TYPE.getKey(), PersistentDataType.STRING), dataContainer.get(GunData.GUN_TIER.getKey(), PersistentDataType.STRING));
    }
}
