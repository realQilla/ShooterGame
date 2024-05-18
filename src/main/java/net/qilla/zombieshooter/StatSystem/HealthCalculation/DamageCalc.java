package net.qilla.zombieshooter.StatSystem.HealthCalculation;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.qilla.zombieshooter.StatSystem.BaseValues;
import net.qilla.zombieshooter.StatSystem.DamageIndicator.Indicator;
import net.qilla.zombieshooter.StatSystem.StatManagement.StatManager;
import net.qilla.zombieshooter.StatSystem.StatUtil.Formula;
import net.qilla.zombieshooter.StatSystem.StatUtil.UpdatePlayer;
import net.qilla.zombieshooter.StatSystem.TagDisplay.HealthDisplay;
import org.bukkit.craftbukkit.entity.CraftPlayer;
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
        new Indicator(sourceReceiver.getLocation(), Indicator.IndicatorType.DAMAGE, damageAmount).mainIndicator();
        new HealthDisplay(sourceReceiver, new HealthDifference(currentHealth, damageAmount), HealthDisplay.DisplayType.DAMAGE).updateHealthDisplay();
    }

    private long getNeededDamage(long damageAmount) {
        if ((currentHealth - damageAmount) <= 0) {
            killPlayer();
            return currentHealth;
        }
        return damageAmount;
    }

    private void killPlayer() {
        sourceReceiver.setHealth(0);
        ServerPlayer nmsPlayer = ((CraftPlayer)sourceReceiver).getHandle();
        Packet<ClientGamePacketListener> packet = new ClientboundGameEventPacket(ClientboundGameEventPacket.IMMEDIATE_RESPAWN, 11);

        nmsPlayer.connection.sendPacket(packet);
    }
}
