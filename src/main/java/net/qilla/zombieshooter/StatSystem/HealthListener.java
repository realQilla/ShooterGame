package net.qilla.zombieshooter.StatSystem;

import net.qilla.zombieshooter.StatSystem.ActionBar.StatDisplay;
import net.qilla.zombieshooter.StatSystem.HealthCalculation.DamageCalc;
import net.qilla.zombieshooter.StatSystem.HealthCalculation.HealCalc;
import net.qilla.zombieshooter.StatSystem.StatManagement.StatManager;
import net.qilla.zombieshooter.StatSystem.TagDisplay.HealthDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class HealthListener implements Listener {

    @EventHandler
    private void onDamage(final EntityDamageEvent event) {
        LivingEntity attacker;
        if(event.getDamageSource().getCausingEntity() == null || !(event.getDamageSource().getCausingEntity() instanceof LivingEntity livingEntity)) {
            attacker = null;
        } else {
            attacker = livingEntity;
        }
        if(event.getEntity() instanceof Player player) {
            new DamageCalc(player, attacker, Math.round(event.getDamage())).damageMain();
            event.setDamage(0);
        }
    }

    @EventHandler
    private void onHeal(final EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        new HealCalc(player, Math.round(event.getAmount())).healMain();
    }

    @EventHandler
    private void onSpawn(final EntitySpawnEvent event) {
        if (event.getEntity() instanceof LivingEntity livingEntity)
            new HealthDisplay(livingEntity).initialDisplaySetup();
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        StatManager.getStatManager(player.getUniqueId()).clear();
        StatDisplay.getStatDisplay(player.getUniqueId()).remove();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        new StatManager(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        StatManager.getStatManager(player.getUniqueId()).resetHealth();
    }
}
