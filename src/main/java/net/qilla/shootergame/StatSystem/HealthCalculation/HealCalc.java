package net.qilla.shootergame.StatSystem.HealthCalculation;

import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.qilla.shootergame.StatSystem.DamageIndicator.Indicator;
import net.qilla.shootergame.StatSystem.StatManagement.StatManager;
import net.qilla.shootergame.StatSystem.StatUtil.Formula;
import net.qilla.shootergame.StatSystem.StatUtil.UpdatePlayer;
import net.qilla.shootergame.StatSystem.TagDisplay.HealthDisplay;
import org.bukkit.craftbukkit.entity.CraftPlayer;
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