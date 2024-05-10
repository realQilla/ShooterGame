package net.qilla.zombieshooter.GunSystem.GunCreation;

import net.qilla.zombieshooter.GunSystem.GunCreation.GunType.AssaultRifle;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunType.FlameThrower;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunType.GunBase;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunType.StandardPistol;
import net.qilla.zombieshooter.GunSystem.GunCreation.Mod.GunID;

import java.util.HashMap;
import java.util.Map;

public class GunRegistry {

    private final static GunRegistry instance = new GunRegistry();
    private static final Map<GunID, GunBase> gunRegistry = new HashMap<>();

    private GunRegistry() {
    }

    private void register(GunID gunID, GunBase gunBase) {
        gunRegistry.put(gunID, gunBase);
    }

    public GunBase getGun(GunID gunID) {
        return gunRegistry.get(gunID);
    }

    public Map<GunID, GunBase> getGunRegistry() {
        return gunRegistry;
    }

    public void registerAll() {
        register(StandardPistol.I().getTypeID(), StandardPistol.I());
        register(StandardPistol.II().getTypeID(), StandardPistol.II());
        register(StandardPistol.III().getTypeID(), StandardPistol.III());
        register(AssaultRifle.I().getTypeID(), AssaultRifle.I());
        register(AssaultRifle.II().getTypeID(), AssaultRifle.II());
        register(FlameThrower.I().getTypeID(), FlameThrower.I());
    }

    public static GunRegistry getInstance() {
        return instance;
    }
}
