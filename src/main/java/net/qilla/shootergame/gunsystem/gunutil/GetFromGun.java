package net.qilla.shootergame.gunsystem.gunutil;

import net.qilla.shootergame.gunsystem.guncreation.GunPDC;
import net.qilla.shootergame.gunsystem.guncreation.gunmod.GunID;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GetFromGun {

    public static GunID typeID(PersistentDataContainer dataContainer) {
        return new GunID(dataContainer.get(GunPDC.GUN_TYPE.getKey(), PersistentDataType.STRING), dataContainer.get(GunPDC.GUN_TIER.getKey(), PersistentDataType.STRING));
    }
}
