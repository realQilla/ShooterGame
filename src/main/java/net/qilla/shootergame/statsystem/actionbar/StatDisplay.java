package net.qilla.shootergame.statsystem.actionbar;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.shootergame.statsystem.statmanagement.StatManager;
import net.qilla.shootergame.ShooterGame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import static net.qilla.shootergame.statsystem.stat.StatType.*;

public class StatDisplay {

    private BukkitTask displayTask;
    private final StatManager statManager;
    private final Player player;
    private long playerHealth;
    private long playerMaxHealth;
    private long playerDefense;


    public StatDisplay(StatManager statManager) {
        this.statManager = statManager;
        this.player = statManager.getPlayer();

        displayLoop();
    }

    private void displayLoop() {
        remove();
        displayTask = Bukkit.getScheduler().runTaskTimer(ShooterGame.getInstance(), () -> {
            pullUpdatedStats();
            player.sendActionBar(MiniMessage.miniMessage().deserialize("<red>♥ " + playerHealth + "/" + playerMaxHealth + "</red>   <gray>" + playerDefense + " \uD83D\uDEE1</gray>"));
        }, 0, 30);
    }

    public void forceUpdate() {
        remove();
        displayLoop();
    }

    private void pullUpdatedStats() {
        this.playerHealth = statManager.getStatRegistry().getStat(HEALTH).getValue();
        this.playerMaxHealth = statManager.getStatRegistry().getStat(MAX_HEALTH).getValue();
        this.playerDefense = statManager.getStatRegistry().getStat(DEFENSE).getValue();
    }

    public void remove() {
        if(displayTask != null) {
            displayTask.cancel();
            displayTask = null;
        }
    }
}
