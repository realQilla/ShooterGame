package net.qilla.shootergame;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.shootergame.ArmorSystem.ArmorRegistry;
import net.qilla.shootergame.BlockSystem.BlockDatabase.BlockMapper;
import net.qilla.shootergame.BlockSystem.BlockDatabase.BlockDBListener;
import net.qilla.shootergame.BlockSystem.CustomBlock.CustomBlockRegistry;
import net.qilla.shootergame.Database.Database;
import net.qilla.shootergame.PacketListener.PacketGeneric;
import net.qilla.shootergame.Command.GetArmor;
import net.qilla.shootergame.Command.GetBlock;
import net.qilla.shootergame.Command.GetGun;
import net.qilla.shootergame.GunSystem.GunCreation.GunRegistry;
import net.qilla.shootergame.GunSystem.WeaponListener;
import net.qilla.shootergame.PlayerMechanics.InstantPickup;
import net.qilla.shootergame.StatSystem.StatListener;
import net.qilla.shootergame.StatSystem.StatManagement.StatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

import static org.bukkit.Bukkit.getPluginCommand;

public class PluginManagement implements Listener {

    private static final PluginManagement instance = new PluginManagement();
    private final ShooterGame plugin = ShooterGame.getInstance();
    private final PacketGeneric packetGeneric = new PacketGeneric();
    private static Database database = null;

    protected void onEnable() {
        try {
            database = new Database();
        } catch(SQLException exception) {
            System.out.println("Unable to connect to the database.");
            exception.printStackTrace();
        }

        GunRegistry.registerAll();
        ArmorRegistry.registerAll();
        CustomBlockRegistry.registerAll();

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new WeaponListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new InstantPickup(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new StatListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockDBListener(), plugin);

        getPluginCommand("GetGun").setExecutor(new GetGun());
        getPluginCommand("GetArmor").setExecutor(new GetArmor());
        getPluginCommand("GetBlock").setExecutor(new GetBlock());
    }

    protected void onDisable() {
        BlockMapper.getInstance().sendGlobalToDB();
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.kick(MiniMessage.miniMessage().deserialize("<red>Server is reloading</red>"));
        });
    }

    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();

        packetGeneric.addListener(player);
        new StatManager(player);
        //new GunDisplay(player).displayLoop();
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        Player player = event.getPlayer();

        packetGeneric.removeListener(player);
        StatManager.getStatManager(player.getUniqueId()).clear();
        //GunDisplay.getDisplayMap(player).remove();
    }

    public Database getDatabase() {
        return database;
    }

    protected static PluginManagement getInstance() {
        return instance;
    }
}