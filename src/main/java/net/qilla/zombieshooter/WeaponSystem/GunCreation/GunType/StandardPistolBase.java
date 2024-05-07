package net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType;

import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.AmmunitionMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.CosmeticMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.FireMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.UniqueID;
import org.bukkit.Material;

import java.util.List;

public abstract class StandardPistolBase extends GunBase {

    public StandardPistolBase(Material gunMaterial, FireMod[] fireMod, AmmunitionMod ammunitionMod, CosmeticMod cosmeticMod, UniqueID uniqueID, String gunName, List<String> gunLore) {
        super(gunMaterial, fireMod, ammunitionMod, cosmeticMod, uniqueID, gunName, gunLore);
    }
}