package net.qilla.zombieshooter.BlockSystem.BlockDatabase;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.zombieshooter.ZombieShooter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BlockDBListener implements Listener {

    private final BlockMapper blockMapper = BlockMapper.getInstance();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onWorldSave(final WorldSaveEvent event) {
        blockMapper.sendWorldToDB(event.getWorld());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onChunkLoad(final ChunkLoadEvent event) {
        blockMapper.mapChunkFromDB(event.getWorld(), new ChunkCoord(event.getChunk().getX(), event.getChunk().getZ()));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onChunkUnload(final ChunkUnloadEvent event) {
        blockMapper.unMapChunkSendToDB(event.getWorld(), new ChunkCoord(event.getChunk().getX(), event.getChunk().getZ()));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onServerLoad(final ServerLoadEvent event) {
        Bukkit.getWorlds().forEach(world -> {
            for(Chunk chunk : world.getLoadedChunks()) {
                blockMapper.mapChunkFromDB(world, new ChunkCoord(chunk.getX(), chunk.getZ()));
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onBlockPlace(final BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!player.getInventory().getItemInMainHand().hasItemMeta()) return;
        PersistentDataContainer pdc = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
        if (!pdc.has(new NamespacedKey(ZombieShooter.getInstance(), "permanent_block"))) return;
        final boolean isPermanent = Boolean.TRUE.equals(pdc.get(new NamespacedKey(ZombieShooter.getInstance(), "permanent_block"), PersistentDataType.BOOLEAN));
        final short blockID = pdc.get(new NamespacedKey(ZombieShooter.getInstance(), "block_id"), PersistentDataType.SHORT);
        blockMapper.addBlock(event.getBlock().getLocation(), blockID, isPermanent);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onBlockBreak(final BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();
        MineableData mineableData = blockMapper.getMineableData(location);
        if (mineableData == null) return;
        if (mineableData.isPermanent()) {
            if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE || !player.isSneaking()) {
                event.setCancelled(true);
            } else {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You removed a block labeled as permanent.</red>"));
                blockMapper.removeBlock(location);
            }
            return;
        }
        blockMapper.removeBlock(location);
    }
}