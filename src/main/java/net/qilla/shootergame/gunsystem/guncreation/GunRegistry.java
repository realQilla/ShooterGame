package net.qilla.shootergame.gunsystem.guncreation;

import net.qilla.shootergame.gunsystem.guncreation.guntype.AssaultRifle;
import net.qilla.shootergame.gunsystem.guncreation.guntype.FlameThrower;
import net.qilla.shootergame.gunsystem.guncreation.guntype.GunBase;
import net.qilla.shootergame.gunsystem.guncreation.guntype.StandardPistol;
import net.qilla.shootergame.gunsystem.guncreation.gunmod.GunID;

import java.util.HashMap;
import java.util.Map;

public class GunRegistry {

    private final Map<GunID, GunBase> gunRegistry = new HashMap<>();

    public GunRegistry() {
        registerAll();
    }

    private void register(GunID gunID, GunBase gunBase) {
        gunRegistry.put(gunID, gunBase);
    }

    public GunBase getGun(GunID gunID) {
        return gunRegistry.get(gunID);
    }

    public Map<GunID, GunBase> getRegistry() {
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
}
