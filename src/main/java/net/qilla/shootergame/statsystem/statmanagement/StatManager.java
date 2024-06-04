package net.qilla.shootergame.statsystem.statmanagement;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.qilla.shootergame.statsystem.actionbar.StatDisplay;
import net.qilla.shootergame.statsystem.healthcalc.HealCalc;
import net.qilla.shootergame.statsystem.stat.*;
import net.qilla.shootergame.statsystem.statutil.Formula;
import net.qilla.shootergame.ShooterGame;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.qilla.shootergame.statsystem.stat.StatType.*;

public class StatManager {

    private static final Map<UUID, StatManager> statManagerRegistry = new HashMap<>();

    private final Player player;

    private final StatContainer statContainer;
    private final StatDisplay statDisplay;

    private StatContainer activeArmor;
    private StatContainer activeHeld;

    private BukkitTask regenerationTask;

    public StatManager(@NotNull Player player) {
        statManagerRegistry.put(player.getUniqueId(), this);

        this.player = player;

        this.statContainer = new StatContainer().registerAll();
        setAttributes();
        healthRegeneration();
        this.statDisplay = new StatDisplay(this);

        this.activeArmor = null;
        this.activeHeld = null;
    }

    public static StatManager getStatManager(@NotNull final Player player) {
        return statManagerRegistry.get(player.getUniqueId());
    }

    public StatContainer getStatRegistry() {
        return this.statContainer;
    }

    public Player getPlayer() {
        return this.player;
    }

    public StatDisplay getStatDisplay() {
        return this.statDisplay;
    }



    public void addStat(final StatBase stat, final long amount) {
        this.statContainer.addValue(stat.getType(), amount);
        this.statDisplay.forceUpdate();
    }

    public void subtractStat(final StatBase stat, final long amount) {
        this.statContainer.subtractValue(stat.getType(), amount);
        this.statDisplay.forceUpdate();
    }

    public void setStat(final StatBase stat, final long amount) {
        this.statContainer.setValue(stat.getType(), amount);
        this.statDisplay.forceUpdate();
    }



    public void kill() {
        this.player.setHealth(0);
    }

    public void resetStats() {
        this.statContainer.resetStats();
        this.statDisplay.forceUpdate();
        updateClientHealth(statContainer.getStat(MAX_HEALTH).getValue());
    }

    public void hardRemove() {
        statManagerRegistry.remove(player.getUniqueId());
        this.statDisplay.remove();
        if (regenerationTask == null || regenerationTask.isCancelled()) return;
        this.regenerationTask.cancel();
    }



    public void updateArmor() {
        this.activeArmor = new StatCalculation().activeArmor(player);
        calcAll();
    }

    public void calcAll() {
        this.statContainer.resetStats();
        statContainer.getContainer().forEach((k, v) -> System.out.println("BEFORE: " + k + " " + v.getValue()));
        this.activeArmor.getContainer().forEach((k, v) -> this.statContainer.addValue(k, v.getValue()));
        statContainer.getContainer().forEach((k, v) -> System.out.println("AFTER: " + k + " " + v.getValue()));
        this.statDisplay.forceUpdate();
        updateClientHealth(this.statContainer.getStat(HEALTH).getValue());
    }

    private void setAttributes() {
        this.player.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED).setBaseValue(0);
    }

    private void healthRegeneration() {
        this.regenerationTask = Bukkit.getScheduler().runTaskTimer(ShooterGame.getInstance(), () -> setRegeneration(), 0, 40);
    }

    private void setRegeneration() {
        final long health = this.statContainer.getStat(HEALTH).getValue();
        final long maxHealth = this.statContainer.getStat(MAX_HEALTH).getValue();
        final long regeneration = this.statContainer.getStat(REGENERATION).getValue();

        if (health >= maxHealth|| health <= 0) return;
        final long bonusRegeneration = (long) Math.floor((double) maxHealth / 100);

        new HealCalc(this, regeneration + bonusRegeneration).healMain();
    }

    public void updateClientHealth(final long health) {
        final ServerPlayer nmsPlayer = ((CraftPlayer) this.player).getHandle();
        final Packet<ClientGamePacketListener> packet = new ClientboundSetHealthPacket(Formula.healthBar(health, statContainer.getStat(HEALTH).getValue()),
                this.player.getFoodLevel(),
                this.player.getSaturation());

        nmsPlayer.connection.sendPacket(packet);
    }
}