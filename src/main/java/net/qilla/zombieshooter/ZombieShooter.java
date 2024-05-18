package net.qilla.zombieshooter;

import net.qilla.zombieshooter.BlockSystem.ChunkCaching;
import net.qilla.zombieshooter.Command.GetArmor;
import net.qilla.zombieshooter.Command.GetGun;
import net.qilla.zombieshooter.CorpseSystem.CorpseListener;
import net.qilla.zombieshooter.ArmorSystem.ArmorRegistry;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunRegistry;
import net.qilla.zombieshooter.GunSystem.WeaponListener;
import net.qilla.zombieshooter.StatSystem.StatListener;
import net.qilla.zombieshooter.PlayerMechanics.MechanicListener;
import net.qilla.zombieshooter.StatSystem.StatManagement.StatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZombieShooter extends JavaPlugin {

    @Override
    public void onEnable() {
        GunRegistry.getInstance().registerAll();
        ArmorRegistry.getInstance().registerAll();
        getServer().getPluginManager().registerEvents(new WeaponListener(), this);
        getServer().getPluginManager().registerEvents(new MechanicListener(), this);
        getServer().getPluginManager().registerEvents(new CorpseListener(), this);
        getServer().getPluginManager().registerEvents(new StatListener(), this);
        getServer().getPluginManager().registerEvents(new ChunkCaching(), this);
        getCommand("GetGun").setExecutor(new GetGun());
        getCommand("GetArmor").setExecutor(new GetArmor());
        for(Player player : Bukkit.getOnlinePlayers()) {
            new StatManager(player);
            //new GunDisplay(player).displayLoop();
        }
    }

    @Override
    public void onDisable() {
        //Disable logic
    }

    public static ZombieShooter getInstance() {
        return getPlugin(ZombieShooter.class);
    }
}

