package net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType;

import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.AmmunitionMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.CosmeticMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.FireMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.UniqueID;
import org.bukkit.Material;

import java.util.List;

public abstract class GunBase {

    final Material gunMaterial;
    final FireMod[] fireMod;
    final AmmunitionMod ammunitionMod;
    final CosmeticMod cosmeticMod;
    final UniqueID uniqueID;
    final String gunName;
    final List<String> gunLore;

    GunBase(Material gunMaterial, FireMod[] fireMod, AmmunitionMod ammunitionMod, CosmeticMod cosmeticMod, UniqueID uniqueID, String gunName, List<String> gunLore) {
        this.gunMaterial = gunMaterial;
        this.fireMod = fireMod;
        this.ammunitionMod = ammunitionMod;
        this.cosmeticMod = cosmeticMod;
        this.uniqueID = uniqueID;
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

    public UniqueID getUniqueID() {
        return uniqueID;
    }

    public String getGunName() {
        return gunName;
    }

    public List<String> getGunLore() {
        return gunLore;
    }
}
