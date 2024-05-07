package net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType;

import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.AmmunitionMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.CosmeticMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.FireMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.UniqueID;
import net.qilla.zombieshooter.WeaponSystem.SoundModel;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.List;

public class StandardPistol extends GunBase {

    private StandardPistol(Material gunMaterial, FireMod[] fireMod, AmmunitionMod ammunitionMod, CosmeticMod cosmeticMod, UniqueID uniqueID, String gunName, List<String> gunLore) {
        super(gunMaterial, fireMod, ammunitionMod, cosmeticMod, uniqueID, gunName, gunLore);
    }

    public static StandardPistol I() {
        return new StandardPistol(Material.WOODEN_HOE,
                new FireMod[]{new FireMod("SINGLE", 6, 0, 1, 4.5f, 0.35f, 0.0f, 24)},
                new AmmunitionMod(96, 8, 4, 10),
                new CosmeticMod(new SoundModel(1.0f, 1.25f, org.bukkit.Sound.ENTITY_IRON_GOLEM_REPAIR), Particle.CRIT),
                new UniqueID("standard_pistol", "tier_1"),
                "<!italic><yellow>Standard Pistol</yellow>",
                List.of("<!italic><gray>Single fire</gray>",
                        "",
                        "<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>3.3/s</white>",
                        "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>4.5 ♥</white>",
                        "<!italic><gold>\uD83C\uDFF9</gold> <gray>Range:</gray> <white>24/b</white>",
                        "",
                        "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK F</bold></yellow> <gray>to reload your weapon</gray>",
                        "",
                        "<!italic><gray>Standard weapon with below average stats.</gray>",
                        "",
                        "<!italic><white><bold>STANDARD WEAPON</white>")
        );
    }

    public static StandardPistol II() {
        return new StandardPistol(Material.WOODEN_HOE,
                new FireMod[]{new FireMod("SINGLE", 8, 0, 1, 6.0f, 0.45f, 0.0f, 32)},
                new AmmunitionMod(128, 8, 4, 10),
                new CosmeticMod(new SoundModel(1.0f, 1.25f, org.bukkit.Sound.ENTITY_IRON_GOLEM_REPAIR), Particle.ENCHANTED_HIT),
                new UniqueID("standard_pistol", "tier_2"),
                "<!italic><blue>Standard Pistol</blue>",
                List.of("<!italic><gray>Single fire</gray>",
                        "",
                        "<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>2.5/s</white>",
                        "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>6.0 ♥</white>",
                        "<!italic><gold>\uD83C\uDFF9</gold> <gray>Range:</gray> <white>32/b</white>",
                        "",
                        "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK F</bold></yellow> <gray>to reload your weapon</gray>",
                        "",
                        "<!italic><gray>Upgraded standard weapon with an overall better setup.</gray>",
                        "",
                        "<!italic><blue><bold>UPGRADED WEAPON</bold></blue>")
        );
    }

    public static StandardPistol III() {
        return new StandardPistol(Material.WOODEN_HOE,
                new FireMod[]{new FireMod("BURST", 10, 3, 2, 4.8f, 0.3f, 0.0f, 32)},
                new AmmunitionMod(192, 16, 6, 10),
                new CosmeticMod(new SoundModel(1.0f, 1.25f, org.bukkit.Sound.ENTITY_IRON_GOLEM_REPAIR), Particle.ENCHANTED_HIT),
                new UniqueID("standard_pistol", "tier_3"),
                "<!italic><dark_purple>Standard Pistol</dark_purple>",
                List.of("<!italic><gray>Burst fire</gray>",
                        "",
                        "<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>4/s</white>",
                        "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>4.8 ♥</white>",
                        "<!italic><gold>\uD83C\uDFF9</gold> <gray>Range:</gray> <white>32/b</white>",
                        "",
                        "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK F</bold></yellow> <gray>to reload your weapon</gray>",
                        "", "<!italic><gray>Revised standard pistol with dual burst allowing you</gray>",
                        "<!italic><gray>to blast an enemy with twice the power.</gray>",
                        "",
                        "<!italic><dark_purple><bold>REVISED WEAPON</bold></dark_purple>")
        );
    }
}