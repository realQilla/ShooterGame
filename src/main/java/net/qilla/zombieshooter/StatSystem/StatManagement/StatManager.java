package net.qilla.zombieshooter.StatSystem.StatManagement;

import net.qilla.zombieshooter.ArmorSystem.ArmorPDC;
import net.qilla.zombieshooter.StatSystem.ActionBar.StatDisplay;
import net.qilla.zombieshooter.StatSystem.BaseValues;
import net.qilla.zombieshooter.StatSystem.HealthCalculation.HealCalc;
import net.qilla.zombieshooter.StatSystem.StatUtil.UpdatePlayer;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatManager extends StatCore {

    private static final Map<UUID, StatManager> statManager = new HashMap<>();

    private BukkitTask timedStat;
    private final Player player;
    private final UUID playerUUID;

    public StatManager(Player player) {
        super(player.getUniqueId());
        this.player = player;
        this.playerUUID = player.getUniqueId();
        statManager.put(playerUUID, this);

        super.setHealth(calcTotalMaxHealth());
        super.setStats(new StatModel(calcTotalMaxHealth(), calcTotalDefense(), calcTotalRegeneration()));
        new StatDisplay(player);
        setAttributes();
        automatedTasks();
    }

    public static StatManager getStatManager(UUID playerUUID) {
        return statManager.get(playerUUID);
    }

    public void addHealth(long amount) {
        super.setHealth(super.getHealth() + amount);
        UpdatePlayer.healthBarDisplay(player, (super.getHealth() + amount), super.getStats().getMaxHealth());
        updateDisplay();
    }

    public void removeHealth(long amount) {
        super.setHealth(super.getHealth() - amount);
        UpdatePlayer.healthBarDisplay(player, (super.getHealth() + amount), super.getStats().getMaxHealth());
        updateDisplay();
    }

    public void resetHealth() {
        super.setHealth(BaseValues.BASE_HEALTH.getValue());
        UpdatePlayer.healthBarDisplay(player, BaseValues.BASE_HEALTH.getValue(), super.getStats().getMaxHealth());
        updateDisplay();
    }

    public void clear() {
        super.clearStats();
        statManager.remove(playerUUID);
        StatDisplay.getStatDisplay(player.getUniqueId()).remove();
        if (timedStat == null || timedStat.isCancelled()) return;
        timedStat.cancel();
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
        if (getHealth() > calcTotalMaxHealth()) setHealth(calcTotalMaxHealth());
        super.setStats(new StatModel(calcTotalMaxHealth(), calcTotalDefense(), calcTotalRegeneration()));
        UpdatePlayer.healthBarDisplay(player, (super.getHealth()), super.getStats().getMaxHealth());
    }

    private void updateDisplay() {
        StatDisplay.getStatDisplay(playerUUID).updateDisplay();
    }

    /**
     * Calculate the all variables for that stat
     */

    private long calcTotalMaxHealth() {
        return BaseValues.BASE_HEALTH.getValue() + getArmorPDC(ArmorPDC.ITEM_STAT_MAX_HEALTH) + getHeldPDC(ArmorPDC.ITEM_STAT_MAX_HEALTH);
    }

    private long calcTotalDefense() {
        return BaseValues.BASE_DEFENSE.getValue() + getArmorPDC(ArmorPDC.ITEM_STAT_DEFENSE) + getHeldPDC(ArmorPDC.ITEM_STAT_DEFENSE);
    }

    private long calcTotalRegeneration() {
        return getArmorPDC(ArmorPDC.ITEM_STAT_REGENERATION) + getHeldPDC(ArmorPDC.ITEM_STAT_REGENERATION);
    }

    /**
     * Get PDC's from different places
     */

    private long getArmorPDC(ArmorPDC armorPDC) {
        long totalValue = 0;
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(armorPDC.getKey(), PersistentDataType.LONG)) {
                totalValue += item.getItemMeta().getPersistentDataContainer().get(armorPDC.getKey(), PersistentDataType.LONG);
            }
        }
        return totalValue;
    }

    private long getHeldPDC(ArmorPDC armorPDC) {
        long totalValue = 0;
        if (player.getInventory().getItemInMainHand().hasItemMeta() && player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(armorPDC.getKey(), PersistentDataType.LONG)) {
            //totalValue += player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(armorPDC.getKey(), PersistentDataType.LONG);
        }
        return totalValue;
    }

    private void setAttributes() {
        //player.registerAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED);
        player.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED).setBaseValue(0);
    }

    /**
     * Automated timed tasks
     */

    private void automatedTasks() {
        timedStat = Bukkit.getScheduler().runTaskTimer(ZombieShooter.getInstance(), () -> {
            setRegeneration();
        }, 0, 40);
    }

    private void setRegeneration() {
        if (getHealth() >= getStats().getMaxHealth() || getHealth() <= 0) return;
        final long regeneration = (long) Math.ceil((double) getStats().getMaxHealth() / 100);
        new HealCalc(player, getStats().getRegeneration() + regeneration).healMain();
    }
}
