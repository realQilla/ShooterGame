package net.qilla.shootergame.gunsystem.guncreation;

import net.qilla.shootergame.gunsystem.guncreation.guntype.AssaultRifle;
import net.qilla.shootergame.gunsystem.guncreation.guntype.FlameThrower;
import net.qilla.shootergame.gunsystem.guncreation.guntype.GunBase;
import net.qilla.shootergame.gunsystem.guncreation.guntype.StandardPistol;
import net.qilla.shootergame.gunsystem.guncreation.gunmod.GunID;

import java.util.HashMap;
import java.util.Map;

public class GunRegistry {

    private static final Map<GunID, GunBase> gunRegistry = new HashMap<>();

    private GunRegistry() {
    }

    private static void register(GunID gunID, GunBase gunBase) {
        gunRegistry.put(gunID, gunBase);
    }

    public static GunBase getGun(GunID gunID) {
        return gunRegistry.get(gunID);
    }

    public static Map<GunID, GunBase> getGunRegistry() {
        return gunRegistry;
    }

    public static void registerAll() {
        register(StandardPistol.I().getTypeID(), StandardPistol.I());
        register(StandardPistol.II().getTypeID(), StandardPistol.II());
        register(StandardPistol.III().getTypeID(), StandardPistol.III());
        register(AssaultRifle.I().getTypeID(), AssaultRifle.I());
        register(AssaultRifle.II().getTypeID(), AssaultRifle.II());
        register(FlameThrower.I().getTypeID(), FlameThrower.I());
    }
}
