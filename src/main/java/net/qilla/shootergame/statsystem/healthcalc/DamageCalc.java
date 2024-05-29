package net.qilla.shootergame.statsystem.healthcalc;

import net.qilla.shootergame.statsystem.statmanagement.StatManager;
import net.qilla.shootergame.statsystem.statutil.Formula;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public final class DamageCalc {

    private final StatManager statManager;
    private LivingEntity sourceAttacker;
    private final Player sourceReceiver;
    private final long damageAmount;
    private final long maxHealth;
    private final long defense;
    private final long currentHealth;

    public DamageCalc(final Player sourceReceiver, final LivingEntity sourceAttacker, long damageAmount) {
        this.statManager = StatManager.getStatManager(sourceReceiver.getUniqueId());
        this.sourceAttacker = sourceAttacker;
        this.sourceReceiver = sourceReceiver;
        this.maxHealth = statManager.getStats().getMaxHealth();
        this.defense = statManager.getStats().getDefense();
        this.currentHealth = statManager.getHealth();
        this.damageAmount = getNeededDamage(Math.max(0, Formula.defenseCalc(damageAmount, defense)));
    }

    public void damageMain() {
        statManager.removeHealth(damageAmount);
    }

    private long getNeededDamage(long damageAmount) {
        if ((currentHealth - damageAmount) <= 0) {
            statManager.killPlayer();
            return currentHealth;
        }
        return damageAmount;
    }
}
