package net.qilla.shootergame;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.shootergame.armorsystem.ArmorRegistry;
import net.qilla.shootergame.blocksystem.blockdb.BlockDBListener;
import net.qilla.shootergame.blocksystem.blockdb.BlockMapper;
import net.qilla.shootergame.blocksystem.blockdb.LoadedCustomBlockReg;
import net.qilla.shootergame.blocksystem.customblock.CustomBlockReg;
import net.qilla.shootergame.blocksystem.customblock.MiningReg;
import net.qilla.shootergame.command.GiveArmorCom;
import net.qilla.shootergame.command.GetBlockCom;
import net.qilla.shootergame.command.GiveGunCom;
import net.qilla.shootergame.command.RCBCom;
import net.qilla.shootergame.cooldown.CooldownRegistry;
import net.qilla.shootergame.database.Database;
import net.qilla.shootergame.gunsystem.WeaponListener;
import net.qilla.shootergame.gunsystem.guncreation.GunRegistry;
import net.qilla.shootergame.packetlistener.PacketGeneric;
import net.qilla.shootergame.playermechanics.InstantPickup;
import net.qilla.shootergame.statsystem.StatListener;
import net.qilla.shootergame.statsystem.statmanagement.StatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

import static org.bukkit.Bukkit.getPluginCommand;

public final class ShooterGame extends JavaPlugin implements Listener {

    private final LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
    private final PacketGeneric packetGeneric = new PacketGeneric(this);
    private Database database = null;

    private final CooldownRegistry cooldownRegistry = new CooldownRegistry();
    private final GunRegistry gunRegistry = new GunRegistry();
    private final ArmorRegistry armorRegistry = ArmorRegistry.getInstance();
    private final LoadedCustomBlockReg loadedCustomBlockReg = new LoadedCustomBlockReg();
    private final CustomBlockReg customBlockReg = new CustomBlockReg(this);
    private final MiningReg miningReg = new MiningReg();

    private final BlockMapper blockMapper = new BlockMapper(loadedCustomBlockReg);

    public void onEnable() {

        try {
            database = new Database();
        } catch(SQLException exception) {
            Bukkit.getLogger().severe("Failed to connect to database");
            exception.printStackTrace();
        }

        registerEvents();
        registerCommands();
    }

    public void onDisable() {
        blockMapper.sendGlobalToDB();
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.kick(MiniMessage.miniMessage().deserialize("<red>Server is reloading</red>"));
        });
    }

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(new WeaponListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InstantPickup(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new StatListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockDBListener(blockMapper), this);
    }

    private void registerCommands() {
        this.manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            new GiveArmorCom(this, commands).register();
            new RCBCom(this, commands).register();
        });

        getPluginCommand("GetGun").setExecutor(new GiveGunCom(this));
        getPluginCommand("GetBlock").setExecutor(new GetBlockCom());
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
        StatManager.getStatManager(player).hardRemove();
        //GunDisplay.getDisplayMap(player).remove();
    }

    public CooldownRegistry getCooldownRegistry() {
        return cooldownRegistry;
    }

    public GunRegistry getGunRegistry() {
        return gunRegistry;
    }

    public ArmorRegistry getArmorRegistry() {
        return armorRegistry;
    }

    public LoadedCustomBlockReg getLoadedCustomBlockReg() {
        return loadedCustomBlockReg;
    }

    public BlockMapper getBlockMapper() {
        return blockMapper;
    }

    public CustomBlockReg getCustomBlockRegistry() {
        return customBlockReg;
    }

    public MiningReg getMiningReg() {
        return miningReg;
    }

    public static ShooterGame getInstance() {
        return getPlugin(ShooterGame.class);
    }
}