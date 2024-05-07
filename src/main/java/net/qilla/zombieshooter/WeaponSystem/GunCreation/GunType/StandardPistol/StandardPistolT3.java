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

public class StandardPistolT3 extends StandardPistolBase {

    public StandardPistolT3() {
        super(Material.WOODEN_HOE,
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
