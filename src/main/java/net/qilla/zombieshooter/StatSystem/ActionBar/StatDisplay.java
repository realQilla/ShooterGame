package net.qilla.zombieshooter.StatSystem.ActionBar;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.zombieshooter.StatSystem.StatManagement.StatManager;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class StatDisplay {

    private static final Map<UUID, StatDisplay> statDisplay = new HashMap<>();

    private BukkitTask displayTask;
    private final StatManager statManager;
    private final Player player;
    private long playerHealth;
    private long playerMaxHealth;
    private long playerDefense;


    public StatDisplay(Player player) {
        statManager = StatManager.getStatManager(player.getUniqueId());
        this.player = player;
        statDisplay.put(player.getUniqueId(), this);

        displayLoop();
    }

    private void displayLoop() {
        displayTask = new BukkitRunnable() {
            @Override
            public void run() {
                updateStats();
                player.sendActionBar(MiniMessage.miniMessage().deserialize("<red>♥ " + playerHealth + "/" + playerMaxHealth + "</red>   <gray>" + playerDefense + " \uD83D\uDEE1</gray>"));
            }
        }.runTaskTimer(ZombieShooter.getInstance(), 0, 20);
    }

    public void updateDisplay() {
        if(displayTask != null) {
            displayTask.cancel();
            displayTask = null;
        }
        displayLoop();
    }

    private void updateStats() {
        this.playerHealth = statManager.getHealth();
        this.playerMaxHealth = statManager.getStats().getMaxHealth();
        this.playerDefense = statManager.getStats().getDefense();
    }

    public static StatDisplay getStatDisplay(UUID playerUUID) {
        return statDisplay.get(playerUUID);
    }

    public void remove() {
        statDisplay.remove(player.getUniqueId());
        if(displayTask != null) {
            displayTask.cancel();
            displayTask = null;
        }
    }
}
