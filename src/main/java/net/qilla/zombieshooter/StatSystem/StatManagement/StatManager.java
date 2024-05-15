package net.qilla.zombieshooter.StatSystem.StatManagement;

import net.qilla.zombieshooter.ArmorSystem.ArmorPDC;
import net.qilla.zombieshooter.StatSystem.ActionBar.StatDisplay;
import net.qilla.zombieshooter.StatSystem.HealthCalculation.HealCalc;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatManager extends StatCore {

    private static final Map<UUID, StatManager> statManager = new HashMap<>();

    private BukkitTask timedStat;

    private final long baseMaxHealth = 10;
    private final long baseDefence = 0;
    private final long baseRegeneration = 1;

    private final Player player;
    private final UUID playerUUID;

    public StatManager(Player player) {
        super(player.getUniqueId());
        this.player = player;
        this.playerUUID = player.getUniqueId();
        statManager.put(playerUUID, this);

        super.setStats(new StatModel(calcTotalMaxHealth(), calcTotalDefence(), calcTotalRegeneration()));
        super.setHealth(calcTotalMaxHealth());
        new StatDisplay(player);
        timedStat();
    }

    public static StatManager getStatManager(UUID playerUUID) {
        return statManager.get(playerUUID);
    }

    public void addHealth(long amount) {
        super.setHealth(super.getHealth() + amount);
        updateDisplay();
    }

    public void removeHealth(long amount) {
        super.setHealth(super.getHealth() - amount);
        updateDisplay();
    }

    public void resetHealth() {
        super.setHealth(calcTotalMaxHealth());
        updateDisplay();
    }

    public void clear() {
        super.clearStats();
        StatDisplay.getStatDisplay(player.getUniqueId()).remove();
        if(!timedStat.isCancelled()) timedStat.cancel();
    }

    public StatModel getStats() {
        return super.getStats();
    }

    public long getHealth() {
        return super.getHealth();
    }

    public void updateArmor() {
        safeStatUpdate();
        updateDisplay();
    }

    private void safeStatUpdate() {
        if(getHealth() > calcTotalMaxHealth()) setHealth(calcTotalMaxHealth());
        super.setStats(new StatModel(calcTotalMaxHealth(), calcTotalDefence(), calcTotalRegeneration()));
    }

    private void updateDisplay() {
        StatDisplay.getStatDisplay(playerUUID).updateDisplay();
    }

    /**
     * Calculate the all variables for that stat
     */

    private long calcTotalMaxHealth() {
        return baseMaxHealth + getArmorPDC(ArmorPDC.ITEM_STAT_MAX_HEALTH) + getHeldPDC(ArmorPDC.ITEM_STAT_MAX_HEALTH);
    }

    private long calcTotalDefence() {
        return baseDefence + getArmorPDC(ArmorPDC.ITEM_STAT_DEFENCE) + getHeldPDC(ArmorPDC.ITEM_STAT_DEFENCE);
    }

    private long calcTotalRegeneration() {
        return baseRegeneration + getArmorPDC(ArmorPDC.ITEM_STAT_REGENERATION) + getHeldPDC(ArmorPDC.ITEM_STAT_REGENERATION);
    }

    /**
     * Get PDC's from different places
     */

    private long getArmorPDC(ArmorPDC armorPDC) {
        long totalValue = 0;
        for(ItemStack item : player.getInventory().getArmorContents()) {
            if(item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(armorPDC.getKey(), PersistentDataType.LONG)) {
                totalValue += item.getItemMeta().getPersistentDataContainer().get(armorPDC.getKey(), PersistentDataType.LONG);
            }
        }
        return totalValue;
    }

    private long getHeldPDC(ArmorPDC armorPDC) {
        long totalValue = 0;
        if(player.getInventory().getItemInMainHand().hasItemMeta() && player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(armorPDC.getKey(), PersistentDataType.LONG)) {
            //totalValue += player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(armorPDC.getKey(), PersistentDataType.LONG);
        }
        return totalValue;
    }

    /**
     * Timed regeneration
     */

    private void timedStat() {
        timedStat = new BukkitRunnable() {
            @Override
            public void run() {
                if(getStats().getRegeneration() <= 0 || getHealth() >= getStats().getMaxHealth() || getHealth() <= 0) return;
                new HealCalc(player, getStats().getRegeneration()).healMain();
            }
        }.runTaskTimer(ZombieShooter.getInstance(), 0, 40);
    }

    public enum Type {
        REMOVE, ADD
    }
}
