package net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.StandardPistol;

import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunType.StandardPistolBase;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.AmmunitionMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.CosmeticMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.FireMod;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.Mod.UniqueID;
import net.qilla.zombieshooter.WeaponSystem.SoundModel;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.List;

public class StandardPistolT1 extends StandardPistolBase {

    public StandardPistolT1() {
        super(Material.WOODEN_HOE,
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
}
