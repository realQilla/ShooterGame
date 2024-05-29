package net.qilla.shootergame.statsystem.healthcalc;

import net.qilla.shootergame.statsystem.damageindicator.Indicator;
import net.qilla.shootergame.statsystem.statmanagement.StatManager;
import net.qilla.shootergame.statsystem.tagdisplay.HealthDisplay;
import org.bukkit.entity.Player;

public final class HealCalc {

    private final StatManager statManager;
    private final Player sourceReceiver;
    private final long healAmount;
    private final long maxHealth;
    private final long currentHealth;

    public HealCalc(final Player sourceReceiver, long healAmount) {
        this.statManager = StatManager.getStatManager(sourceReceiver.getUniqueId());
        this.sourceReceiver = sourceReceiver;
        this.maxHealth = statManager.getStats().getMaxHealth();
        this.currentHealth = statManager.getHealth();
        this.healAmount = getNeededHeal(healAmount);
    }

    public void healMain() {
        statManager.addHealth(healAmount);
        new Indicator(sourceReceiver.getLocation(), Indicator.IndicatorType.HEAL, healAmount).mainIndicator();
        new HealthDisplay(sourceReceiver, new HealthDifference(currentHealth, healAmount), HealthDisplay.DisplayType.HEAL).updateHealthDisplay();
    }

    private long getNeededHeal(long healAmount) {
        if((currentHealth + healAmount) > maxHealth) {
            return maxHealth - currentHealth;
        } else {
            return healAmount;
        }
    }
}