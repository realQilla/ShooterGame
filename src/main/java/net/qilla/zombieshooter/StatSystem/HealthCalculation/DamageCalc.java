package net.qilla.zombieshooter.StatSystem.HealthCalculation;

import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.qilla.zombieshooter.StatSystem.DamageIndicator.Indicator;
import net.qilla.zombieshooter.StatSystem.StatManagement.StatManager;
import net.qilla.zombieshooter.StatSystem.TagDisplay.HealthDisplay;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public final class DamageCalc {

    private final long baseDamage = 1;

    private final StatManager statManager;
    private LivingEntity sourceAttacker;
    private final Player sourceReceiver;
    private final long damageAmount;
    private final long maxHealth;
    private final long defence;
    private final long currentHealth;

    public DamageCalc(final Player sourceReceiver, final LivingEntity sourceAttacker, long damageAmount) {
        this.statManager = StatManager.getStatManager(sourceReceiver.getUniqueId());
        this.sourceAttacker = sourceAttacker;
        this.sourceReceiver = sourceReceiver;
        this.maxHealth = statManager.getStats().getMaxHealth();
        this.defence = statManager.getStats().getDefence();
        this.currentHealth = statManager.getHealth();
        this.damageAmount = getNeededDamage(damageAmount);
    }

    public void damageMain() {
        statManager.modifyHealth(damageAmount, StatManager.Type.REMOVE);
        setHealthBar();
        new Indicator(sourceReceiver.getLocation(), Indicator.IndicatorType.DAMAGE, damageAmount).mainIndicator();
        new HealthDisplay(sourceReceiver, new HealthDifference(currentHealth, damageAmount), HealthDisplay.DisplayType.DAMAGE).updateHealthDisplay();
    }

    private long getNeededDamage(long damageAmount) {
        if((currentHealth - damageAmount) <= 0) {
            sourceReceiver.setHealth(0);
            return currentHealth;
        } else {
            return damageAmount;
        }
    }

    private void setHealthBar() {
        CraftPlayer craftPlayer = (CraftPlayer) sourceReceiver;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();

        long flattenedHealth = (long) Math.ceil((double) (currentHealth - damageAmount) / maxHealth * 20);

        nmsPlayer.connection.sendPacket(new ClientboundSetHealthPacket(flattenedHealth, sourceReceiver.getFoodLevel(), sourceReceiver.getSaturation()));
    }
}
