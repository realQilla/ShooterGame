package net.qilla.shootergame.statsystem.healthcalc;

import net.qilla.shootergame.statsystem.damageindicator.Indicator;
import net.qilla.shootergame.statsystem.statmanagement.StatManager;
import net.qilla.shootergame.statsystem.statutil.Formula;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import static net.qilla.shootergame.statsystem.stat.StatType.*;

public final class DamageCalc {

    private final StatManager statManager;
    private final LivingEntity sourceAttacker;
    private final Player sourceReceiver;
    private final long amount;
    private final long defense;
    private final long priorHealth;

    public DamageCalc(final Player sourceReceiver, final LivingEntity sourceAttacker, final long amount) {
        this.statManager = StatManager.getStatManager(sourceReceiver);
        this.sourceAttacker = sourceAttacker;
        this.sourceReceiver = sourceReceiver;
        this.defense = statManager.getStatRegistry().getStat(DEFENSE).getValue();
        this.priorHealth = statManager.getStatRegistry().getStat(HEALTH).getValue();
        this.amount = calcDamage(Math.max(0, Formula.defenseCalc(amount, defense)));
    }

    public void damageMain() {
        statManager.subtractStat(statManager.getStatRegistry().getStat(HEALTH), amount);
        new Indicator(sourceReceiver.getEyeLocation(), Indicator.IndicatorType.DAMAGE, amount).mainIndicator();
        statManager.updateClientHealth(priorHealth + amount);
        //new HealthDisplay(sourceReceiver, new HealthDifference(currentHealth, amount), HealthDisplay.DisplayType.HEAL).updateHealthDisplay();
    }

    private long calcDamage(long amount) {
        if (priorHealth - amount <= 0) {
            statManager.kill();
            return priorHealth;
        } else return amount;
    }
}
