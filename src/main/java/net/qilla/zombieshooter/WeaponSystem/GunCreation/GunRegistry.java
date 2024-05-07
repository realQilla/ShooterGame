package net.qilla.zombieshooter.WeaponSystem.GunCreation;

import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.AssaultRifle;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.StandardPistol;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.UniqueID;

import java.util.HashMap;
import java.util.Map;

public class GunRegistry {

    private static final Map<UniqueID, GunBase> gunRegistry = new HashMap<>();

    private GunRegistry() {
    }

    private static void register(UniqueID uniqueID, GunBase gunBase) {
        gunRegistry.put(uniqueID, gunBase);
    }

    public static GunBase getGun(UniqueID uniqueID) {
        return gunRegistry.get(uniqueID);
    }

    public static Map<UniqueID, GunBase> getGunRegistry() {
        return gunRegistry;
    }

    public static void registerAll() {
        register(StandardPistol.I().getUniqueID(), StandardPistol.I());
        register(StandardPistol.II().getUniqueID(), StandardPistol.II());
        register(StandardPistol.III().getUniqueID(), StandardPistol.III());
        register(AssaultRifle.I().getUniqueID(), AssaultRifle.I());
        register(AssaultRifle.II().getUniqueID(), AssaultRifle.II());
    }
}
