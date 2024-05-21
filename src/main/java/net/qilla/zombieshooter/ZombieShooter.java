package net.qilla.zombieshooter;

import org.bukkit.plugin.java.JavaPlugin;

public final class ZombieShooter extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManagement.getInstance().onEnable();
    }

    @Override
    public void onDisable() {
        PluginManagement.getInstance().onDisable();
    }

    public static ZombieShooter getInstance() {
        return getPlugin(ZombieShooter.class);
    }
}

