package net.qilla.zombieshooter.GunSystem.GunCreation.GunType;

import net.qilla.zombieshooter.GunSystem.GunCreation.Mod.AmmunitionMod;
import net.qilla.zombieshooter.GunSystem.GunCreation.Mod.CosmeticMod;
import net.qilla.zombieshooter.GunSystem.GunCreation.Mod.FireMod;
import net.qilla.zombieshooter.GunSystem.GunCreation.Mod.GunID;
import net.qilla.zombieshooter.Utils.SoundModel;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.List;

public class StandardPistol extends GunBase {

    private StandardPistol(Material gunMaterial, FireMod[] fireMod, AmmunitionMod ammunitionMod, CosmeticMod cosmeticMod, GunID gunID, String gunName, List<String> gunLore) {
        super(gunMaterial, fireMod, ammunitionMod, cosmeticMod, gunID, gunName, gunLore);
    }

    public static StandardPistol I() {
        return new StandardPistol(Material.WOODEN_HOE,
                new FireMod[]{new FireMod("SINGLE", 6, 0, 1, 4.5f, 0.35f, 0.0f, 24)},
                new AmmunitionMod(96, 8, 2, 20),
                new CosmeticMod(new SoundModel(0.5f, 1.25f, org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_BLAST), Particle.CRIT),
                new GunID("standard_pistol", "tier_1"),
                "<!italic><yellow>Standard Pistol</yellow>",
                List.of("<!italic><gray>Single fire</gray>",
                        "",
                        "<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>3.3/s</white>",
                        "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>3.0 ♥</white>",
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
                new FireMod[]{new FireMod("SINGLE", 5, 0, 1, 6.0f, 0.45f, 0.0f, 32)},
                new AmmunitionMod(128, 8, 2, 20),
                new CosmeticMod(new SoundModel(0.5f, 1.5f, Sound.ENTITY_FIREWORK_ROCKET_BLAST), Particle.ENCHANTED_HIT),
                new GunID("standard_pistol", "tier_2"),
                "<!italic><blue>Standard Pistol</blue>",
                List.of("<!italic><gray>Single fire</gray>",
                        "",
                        "<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>2.8/s</white>",
                        "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>5.0 ♥</white>",
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
                new FireMod[]{new FireMod("BURST", 10, 3, 2, 4.0f, 0.3f, 0.0f, 32)},
                new AmmunitionMod(192, 16, 4, 20),
                new CosmeticMod(new SoundModel(0.5f, 2.0f, Sound.ENTITY_FIREWORK_ROCKET_BLAST), Particle.ENCHANTED_HIT),
                new GunID("standard_pistol", "tier_3"),
                "<!italic><dark_purple>Standard Pistol</dark_purple>",
                List.of("<!italic><gray>Burst fire</gray>",
                        "",
                        "<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>4/s</white>",
                        "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>4 ♥</white>",
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