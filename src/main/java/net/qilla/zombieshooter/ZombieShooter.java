package net.qilla.zombieshooter;

import net.qilla.zombieshooter.Command.GetGun;
import net.qilla.zombieshooter.WeaponSystem.GunCreation.GunRegistry;
import net.qilla.zombieshooter.WeaponSystem.ItemListener;
import net.qilla.zombieshooter.PlayerMechanics.MechanicListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZombieShooter extends JavaPlugin {

    @Override
    public void onEnable() {
        GunRegistry.registerAll();
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        getServer().getPluginManager().registerEvents(new MechanicListener(), this);
        getCommand("Gun").setExecutor(new GetGun());
    }

    @Override
    public void onDisable() {

    }

    public static ZombieShooter getInstance() {
        return getPlugin(ZombieShooter.class);
    }
}
