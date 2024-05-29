package net.qilla.shootergame.blocksystem.blockdb;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.qilla.shootergame.ShooterGame;
import org.bukkit.*;
import org.bukkit.block.Block;
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
        Block block = event.getBlock();
        if(!player.getInventory().getItemInMainHand().hasItemMeta()) return;
        PersistentDataContainer pdc = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();

        if(!pdc.has(new NamespacedKey(ShooterGame.getInstance(), "permanent_block"))) return;
        final boolean isPermanent = Boolean.TRUE.equals(pdc.get(new NamespacedKey(ShooterGame.getInstance(), "permanent_block"), PersistentDataType.BOOLEAN));
        final short blockID = pdc.get(new NamespacedKey(ShooterGame.getInstance(), "block_id"), PersistentDataType.SHORT);
        blockMapper.addBlock(event.getBlock().getLocation(), blockID, isPermanent);

        MineableData mineableData = blockMapper.getMineableData(block.getLocation());
        if(mineableData != null)
            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Successfully placed permanent custom block. (Permanent: " + mineableData.isPermanent() + ") </green>"));
        else {
            event.setCancelled(true);
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Failed to place custom block.</red>"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onBlockBreak(final BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();
        MineableData mineableData = blockMapper.getMineableData(location);

        if(mineableData == null) return;
        if(mineableData.isPermanent()) {
            if(player.isOp() && player.getGameMode() == GameMode.CREATIVE) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Successfully remove a permanent block.</green>"));
                blockMapper.removeBlock(location);
            }
            else event.setCancelled(true);
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Successfully remove a non-permanent block.</green>"));
            blockMapper.removeBlock(location);
        }
    }
}