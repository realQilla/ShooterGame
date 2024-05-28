package net.qilla.shootergame.GunSystem.GunCreation.GunType;

import net.qilla.shootergame.GunSystem.GunCreation.Mod.AmmunitionMod;
import net.qilla.shootergame.GunSystem.GunCreation.Mod.CosmeticMod;
import net.qilla.shootergame.GunSystem.GunCreation.Mod.FireMod;
import net.qilla.shootergame.GunSystem.GunCreation.Mod.GunID;
import net.qilla.shootergame.Utils.SoundModel;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.List;

public class FlameThrower extends GunBase {


    private FlameThrower(Material gunMaterial, FireMod[] fireMod, AmmunitionMod ammunitionMod, CosmeticMod cosmeticMod, GunID gunID, String gunName, List<String> gunLore) {
        super(gunMaterial, fireMod, ammunitionMod, cosmeticMod, gunID, gunName, gunLore);
    }

    public static FlameThrower I() {
        return new FlameThrower(Material.GOLDEN_HOE,
                new FireMod[]{new FireMod("Automatic",4, 2, 2, 1f, 0.30f, 0.0f, 32)},
                new AmmunitionMod(320, 48, 6, 10),
                new CosmeticMod(new SoundModel(1.0f, 1.0f, Sound.BLOCK_LAVA_POP), Particle.FLAME),
                new GunID("flame_thrower", GunID.Tier.I.getTier()),
                "<!italic><blue>Flame Thrower</blue>",
                List.of("<!italic><gray>Automatic fire</gray>",
                        "",
                        "<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>10.0/s</white>",
                        "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>1 ♥</white>",
                        "<!italic><gold>\uD83C\uDFF9</gold> <gray>Range:</gray> <white>32/b</white>",
                        "",
                        "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK F</bold></yellow> <gray>to reload your weapon</gray>",
                        "",
                        "<!italic><gray>Light flamethrower to toast</gray>",
                        "<!italic><gray>your enemies.</gray>",
                        "",
                        "<!italic><blue><bold>STANDARD WEAPON</bold></blue>")
        );
    }
}
