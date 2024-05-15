package net.qilla.zombieshooter.GunSystem.GunCreation.GunType;

import net.qilla.zombieshooter.GunSystem.GunCreation.Mod.AmmunitionMod;
import net.qilla.zombieshooter.GunSystem.GunCreation.Mod.CosmeticMod;
import net.qilla.zombieshooter.GunSystem.GunCreation.Mod.FireMod;
import net.qilla.zombieshooter.GunSystem.GunCreation.Mod.GunID;
import org.bukkit.Material;

import java.util.List;

public abstract class GunBase {

    private final Material gunMaterial;
    private final FireMod[] fireMod;
    private final AmmunitionMod ammunitionMod;
    private final CosmeticMod cosmeticMod;
    private final GunID gunID;
    private final String gunName;
    private final List<String> gunLore;

    GunBase(Material gunMaterial, FireMod[] fireMod, AmmunitionMod ammunitionMod, CosmeticMod cosmeticMod, GunID gunID, String gunName, List<String> gunLore) {
        this.gunMaterial = gunMaterial;
        this.fireMod = fireMod;
        this.ammunitionMod = ammunitionMod;
        this.cosmeticMod = cosmeticMod;
        this.gunID = gunID;
        this.gunName = gunName;
        this.gunLore = gunLore;
    }

    public Material getGunMaterial() {
        return gunMaterial;
    }

    public FireMod[] getFireMod() {
        return fireMod;
    }

    public AmmunitionMod getAmmunitionMod() {
        return ammunitionMod;
    }

    public CosmeticMod getCosmeticMod() {
        return cosmeticMod;
    }

    public GunID getTypeID() {
        return gunID;
    }

    public String getGunName() {
        return gunName;
    }

    public List<String> getGunLore() {
        return gunLore;
    }
}
