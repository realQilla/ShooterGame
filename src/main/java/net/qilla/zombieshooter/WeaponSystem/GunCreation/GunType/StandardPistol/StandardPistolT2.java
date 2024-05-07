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

public class StandardPistolT2 extends StandardPistolBase {

    public StandardPistolT2() {
        super(Material.WOODEN_HOE,
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
}
