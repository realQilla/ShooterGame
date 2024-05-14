package net.qilla.zombieshooter;

import net.qilla.zombieshooter.Command.GetGun;
import net.qilla.zombieshooter.CorpseSystem.CorpseListener;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunRegistry;
import net.qilla.zombieshooter.GunSystem.GunSkeleton.GunDisplay;
import net.qilla.zombieshooter.GunSystem.WeaponListener;
import net.qilla.zombieshooter.StatSystem.ActionBar.StatDisplay;
import net.qilla.zombieshooter.StatSystem.HealthListener;
import net.qilla.zombieshooter.PlayerMechanics.MechanicListener;
import net.qilla.zombieshooter.StatSystem.StatManagement.StatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZombieShooter extends JavaPlugin {

    @Override
    public void onEnable() {
        GunRegistry.getInstance().registerAll();
        getServer().getPluginManager().registerEvents(new WeaponListener(), this);
        getServer().getPluginManager().registerEvents(new MechanicListener(), this);
        getServer().getPluginManager().registerEvents(new CorpseListener(), this);
        getServer().getPluginManager().registerEvents(new HealthListener(), this);
        getCommand("GetGun").setExecutor(new GetGun());
        for(Player player : Bukkit.getOnlinePlayers()) {
            new StatManager(player);
            //new GunDisplay(player).displayLoop();
        }
    }

    @Override
    public void onDisable() {

    }

    public static ZombieShooter getInstance() {
        return getPlugin(ZombieShooter.class);
    }
}
