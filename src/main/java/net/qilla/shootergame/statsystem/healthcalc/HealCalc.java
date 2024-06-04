package net.qilla.shootergame.statsystem.healthcalc;

import net.qilla.shootergame.statsystem.damageindicator.Indicator;
import net.qilla.shootergame.statsystem.statmanagement.StatManager;
import org.bukkit.entity.Player;

import static net.qilla.shootergame.statsystem.stat.StatType.*;

public final class HealCalc {

    private final StatManager statManager;
    private final Player sourceReceiver;
    private final long amount;
    private final long maxHealth;
    private final long priorHealth;

    public HealCalc(final StatManager statManager, final long amount) {
        this.statManager = statManager;
        this.sourceReceiver = statManager.getPlayer();
        this.maxHealth = statManager.getStatRegistry().getStat(MAX_HEALTH).getValue();
        this.priorHealth = statManager.getStatRegistry().getStat(HEALTH).getValue();
        this.amount = calcHealing(amount);
    }

    public void healMain() {
        statManager.addStat(statManager.getStatRegistry().getStat(HEALTH), amount);
        new Indicator(sourceReceiver.getEyeLocation(), Indicator.IndicatorType.HEAL, amount).mainIndicator();
        statManager.updateClientHealth(priorHealth + amount);
        //new HealthDisplay(sourceReceiver, new HealthDifference(currentHealth, amount), HealthDisplay.DisplayType.HEAL).updateHealthDisplay();
    }

    private long calcHealing(long amount) {
        if((priorHealth + amount) > maxHealth) return maxHealth - priorHealth;
        else return amount;
    }
}