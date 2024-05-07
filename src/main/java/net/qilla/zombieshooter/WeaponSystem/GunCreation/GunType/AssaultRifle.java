package net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType;

import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.AmmunitionMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.CosmeticMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.FireMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.UniqueID;
import net.qilla.zombieshooter.WeaponSystem.SoundModel;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.List;

public class AssaultRifle extends GunBase {


    private AssaultRifle(Material gunMaterial, FireMod[] fireMod, AmmunitionMod ammunitionMod, CosmeticMod cosmeticMod, UniqueID uniqueID, String gunName, List<String> gunLore) {
        super(gunMaterial, fireMod, ammunitionMod, cosmeticMod, uniqueID, gunName, gunLore);
    }

    public static AssaultRifle I() {
        return new AssaultRifle(Material.IRON_HOE,
                new FireMod[]{new FireMod("BURST",10, 2, 3, 3.0f, 0.30f, 0.0f, 64), new FireMod("SINGLE",5, 0, 1, 4.0f, 0.25f, 0.0f, 64)},
                new AmmunitionMod(192, 30, 6, 10),
                new CosmeticMod(new SoundModel(1.0f, 1.50f, Sound.ENTITY_FIREWORK_ROCKET_BLAST), Particle.ENCHANTED_HIT),
                new UniqueID("assault_rifle", "tier_1"),
                "<!italic><blue>Assault Rifle</blue>",
                List.of("<!italic><gray>Burst & Single fire</gray>",
                        "",
                        "<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>6.0/s</white>",
                        "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>3.0 ♥</white>",
                        "<!italic><gold>\uD83C\uDFF9</gold> <gray>Range:</gray> <white>64/b</white>",
                        "",
                        "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK F</bold></yellow> <gray>to reload your weapon</gray>",
                        "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK LEFT</bold></yellow> <gray>to toggle the fire mode</gray>",
                        "",
                        "<!italic><gray>Standard rifle with the power to show,</gray>",
                        "<!italic><gray>can be toggled from single to burst fire.</gray>",
                        "",
                        "<!italic><blue><bold>STANDARD WEAPON</bold></blue>")
        );
    }

    public static AssaultRifle II() {
        return new AssaultRifle(Material.IRON_HOE,
                new FireMod[]{new FireMod("AUTO", 4, 2, 2, 3.0f, 0.30f, 0.0f, 64)},
                new AmmunitionMod(192, 30, 6, 10),
                new CosmeticMod(new SoundModel(1.0f, 1.50f, Sound.ENTITY_FIREWORK_ROCKET_BLAST), Particle.ENCHANTED_HIT),
                new UniqueID("assault_rifle", "tier_2"),
                "<!italic><blue>Assault Rifle</blue>",
                List.of("<!italic><gray>Automatic fire</gray>",
                        "",
                        "<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>10/s</white>",
                        "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>3.0 ♥</white>",
                        "<!italic><gold>\uD83C\uDFF9</gold> <gray>Range:</gray> <white>64/b</white>",
                        "",
                        "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK F</bold></yellow> <gray>to reload your weapon</gray>",
                        "","<!italic><gray>Standard rifle to overwhelm any enemy</gray>",
                        "<!italic><gray>with a barrage of bullets.</gray>",
                        "",
                        "<!italic><blue><bold>STANDARD WEAPON</bold></blue>")
        );
    }
}
