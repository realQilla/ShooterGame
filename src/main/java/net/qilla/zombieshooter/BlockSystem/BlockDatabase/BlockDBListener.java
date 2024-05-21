package net.qilla.zombieshooter.BlockSystem.BlockDatabase;

import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BlockDBListener implements Listener {

    private final NodeMapping nodeMapping = NodeMapping.getInstance();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onWorldSave(final WorldSaveEvent event) {
        nodeMapping.sendWorldToDB(event.getWorld());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onChunkLoad(final ChunkLoadEvent event) {
        nodeMapping.mapChunkFromDB(event.getWorld(), new ChunkCoord(event.getChunk().getX(), event.getChunk().getZ()));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onChunkUnload(final ChunkUnloadEvent event) {
        nodeMapping.unMapChunkSendToDB(event.getWorld(), new ChunkCoord(event.getChunk().getX(), event.getChunk().getZ()));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onBlockPlace(final BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(!player.getInventory().getItemInMainHand().hasItemMeta()) return;
        PersistentDataContainer pdc = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
        if(!pdc.has(new NamespacedKey(ZombieShooter.getInstance(), "block_id"), PersistentDataType.SHORT)) return;
        final Short blockID = pdc.get(new NamespacedKey(ZombieShooter.getInstance(), "block_id"), PersistentDataType.SHORT);
        if(blockID == null) return;
        nodeMapping.addBlock(event.getBlock().getLocation(), blockID);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onBlockBreak(final BlockBreakEvent event) {
        //nodeMapping.removeBreakable(event.getBlock().getLocation());
    }
}