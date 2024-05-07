package net.qilla.zombieshooter.WeaponSystem.Systems;

import io.papermc.paper.entity.TeleportFlag;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

public class GunRecoil {

    public void verticalRecoil(Player player, boolean randomized) {
        Location location = player.getLocation();
        if(randomized) {
            Random random = new Random();
            location.setYaw(player.getYaw() + (random.nextInt(2) - 1));
            location.setPitch(player.getPitch() + random.nextInt(2) * -1);
        }
        player.teleport(location, TeleportFlag.Relative.PITCH, TeleportFlag.Relative.YAW, TeleportFlag.Relative.X, TeleportFlag.Relative.Y, TeleportFlag.Relative.Z);
    }
}
