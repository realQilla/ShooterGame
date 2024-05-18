package net.qilla.zombieshooter.StatSystem;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.mojang.datafixers.types.Type;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.qilla.zombieshooter.StatSystem.HealthCalculation.DamageCalc;
import net.qilla.zombieshooter.StatSystem.StatManagement.StatManager;
import net.qilla.zombieshooter.StatSystem.TagDisplay.HealthDisplay;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class StatListener implements Listener {

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
    private void onEntityRegainHealth(final EntityRegainHealthEvent event) {
        event.setAmount(0);
    }

    @EventHandler
    private void onSpawn(final EntitySpawnEvent event) {
        if (event.getEntity() instanceof LivingEntity livingEntity)
            new HealthDisplay(livingEntity).initialDisplaySetup();
    }

    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();

        new StatManager(player);
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        Player player = event.getPlayer();

        StatManager.getStatManager(player.getUniqueId()).clear();
    }

    @EventHandler
    private void onPlayerRespawn(final PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        StatManager.getStatManager(player.getUniqueId()).resetHealth();
    }

    @EventHandler
    private void onPlayerArmorChange(final PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();

        StatManager.getStatManager(player.getUniqueId()).updateArmor();
    }

    @EventHandler
    private void onPlayerItemDamage(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }
}
