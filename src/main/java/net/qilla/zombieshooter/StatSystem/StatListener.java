package net.qilla.zombieshooter.StatSystem;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.qilla.zombieshooter.CorpseSystem.CorpseDisplay;
import net.qilla.zombieshooter.StatSystem.HealthCalculation.DamageCalc;
import net.qilla.zombieshooter.StatSystem.StatManagement.StatManager;
import net.qilla.zombieshooter.StatSystem.TagDisplay.HealthDisplay;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.lang.reflect.InvocationTargetException;

public final class StatListener implements Listener {

    @EventHandler
    private void onDamage(final EntityDamageEvent event) {
        long damage = Math.round(event.getDamage());
        event.setDamage(0);

        LivingEntity attacker;
        if(event.getDamageSource().getCausingEntity() == null || !(event.getDamageSource().getCausingEntity() instanceof LivingEntity livingEntity)) {
            attacker = null;
        } else {
            attacker = livingEntity;
        }
        if(event.getEntity() instanceof Player player) {
            new DamageCalc(player, attacker, damage).damageMain();
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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    private void onPlayerDeath(PlayerDeathEvent event) throws NoSuchFieldException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Player player = event.getPlayer();
        event.setKeepInventory(true);
        event.getDrops().clear();
        event.setDeathSound(Sound.ENTITY_PLAYER_BURP);

        new CorpseDisplay().body(event.getPlayer());

        ServerPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        Packet<ClientGamePacketListener> packet = new ClientboundGameEventPacket(ClientboundGameEventPacket.IMMEDIATE_RESPAWN, 11);
        nmsPlayer.connection.sendPacket(packet);
    }
}
