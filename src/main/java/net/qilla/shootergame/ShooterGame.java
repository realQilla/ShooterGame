package net.qilla.shootergame;

import org.bukkit.plugin.java.JavaPlugin;

public final class ShooterGame extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManagement.getInstance().onEnable();
    }

    @Override
    public void onDisable() {
        PluginManagement.getInstance().onDisable();
    }

    public static ShooterGame getInstance() {
        return getPlugin(ShooterGame.class);
    }
}

