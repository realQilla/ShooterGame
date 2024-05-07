package net.qilla.zombieshooter.WeaponSystem.GunCreation;

import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.AssaultRifle.AssaultRifleT1;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.AssaultRifle.AssaultRifleT2;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.StandardPistol.StandardPistolT1;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.StandardPistol.StandardPistolT2;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.StandardPistol.StandardPistolT3;
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
        register(new StandardPistolT1().getUniqueID(), new StandardPistolT1());
        register(new StandardPistolT2().getUniqueID(), new StandardPistolT2());
        register(new StandardPistolT3().getUniqueID(), new StandardPistolT3());
        register(new AssaultRifleT1().getUniqueID(), new AssaultRifleT1());
        register(new AssaultRifleT2().getUniqueID(), new AssaultRifleT2());
    }
}
