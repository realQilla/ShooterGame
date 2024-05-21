package net.qilla.zombieshooter;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.zombieshooter.ArmorSystem.ArmorRegistry;
import net.qilla.zombieshooter.BlockSystem.BlockDatabase.BlockMapper;
import net.qilla.zombieshooter.BlockSystem.BlockDatabase.BlockDBListener;
import net.qilla.zombieshooter.BlockSystem.CustomBlock.CustomBlockRegistry;
import net.qilla.zombieshooter.PacketListener.PacketGeneric;
import net.qilla.zombieshooter.Command.GetArmor;
import net.qilla.zombieshooter.Command.GetBlock;
import net.qilla.zombieshooter.Command.GetGun;
import net.qilla.zombieshooter.CorpseSystem.CorpseListener;
import net.qilla.zombieshooter.GunSystem.GunCreation.GunRegistry;
import net.qilla.zombieshooter.GunSystem.WeaponListener;
import net.qilla.zombieshooter.PlayerMechanics.MechanicListener;
import net.qilla.zombieshooter.StatSystem.StatListener;
import net.qilla.zombieshooter.StatSystem.StatManagement.StatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static org.bukkit.Bukkit.getPluginCommand;

public class PluginManagement implements Listener {

    private static final PluginManagement instance = new PluginManagement();
    private final ZombieShooter plugin = ZombieShooter.getInstance();
    private final BlockMapper blockMapper = BlockMapper.getInstance();
    private final PacketGeneric breakPacketListener = new PacketGeneric();;

    protected void onEnable() {
        GunRegistry.registerAll();
        ArmorRegistry.registerAll();
        CustomBlockRegistry.registerAll();

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new WeaponListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new MechanicListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new CorpseListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new StatListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockDBListener(), plugin);

        getPluginCommand("GetGun").setExecutor(new GetGun());
        getPluginCommand("GetArmor").setExecutor(new GetArmor());
        getPluginCommand("GetBlock").setExecutor(new GetBlock());
    }

    protected void onDisable() {
        Bukkit.getWorlds().forEach(BlockMapper.getInstance()::sendWorldToDB);
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.kick(MiniMessage.miniMessage().deserialize("<red>Server is reloading</red>"));
        });
    }

    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();

        breakPacketListener.addListener(player);
        new StatManager(player);
        //new GunDisplay(player).displayLoop();
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        Player player = event.getPlayer();

        breakPacketListener.removeListener(player);
        StatManager.getStatManager(player.getUniqueId()).clear();
        //GunDisplay.getDisplayMap(player).remove();
    }

    protected static PluginManagement getInstance() {
        return instance;
    }
}
