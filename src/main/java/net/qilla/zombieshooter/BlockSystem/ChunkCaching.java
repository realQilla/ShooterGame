package net.qilla.zombieshooter.BlockSystem;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.IOException;
import java.util.BitSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ChunkCaching implements Listener {

    private final ConcurrentMap<String, ConcurrentMap<ChunkCoord, BitSet>> globalChunkMap = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private void loadChunk(World world, ChunkCoord chunkCoord) {
        executorService.submit(() -> {
            try {
                ChunkDataHandler chunkHolder = ChunkDataHandler.getInstance(world);
                if (!chunkHolder.lookupChunk(chunkCoord)) return;
                ConcurrentMap<ChunkCoord, BitSet> chunksMap = globalChunkMap.computeIfAbsent(world.getName(), k -> new ConcurrentHashMap<>());
                chunksMap.putAll(chunkHolder.loadChunkData(chunkCoord));
                System.out.println("Chunk " + chunkCoord + " loaded. " + globalChunkMap.get(world.getName()).size() + " entries in " + world.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void unloadChunk(World world, ChunkCoord chunkCoord) {
        executorService.submit(() -> {
            if (!globalChunkMap.containsKey(world.getName())) return;
            globalChunkMap.get(world.getName()).computeIfPresent(chunkCoord, (key, bitSet) -> {
                try {
                    ChunkDataHandler.getInstance(world).saveChunkData(chunkCoord, bitSet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Chunk " + chunkCoord + " unloaded. " + globalChunkMap.get(world.getName()).size() + " entries in " + world.getName());
                return null;
            });
        });
    }

    private void saveChunks(World world, Map<ChunkCoord, BitSet> chunksMap) {
        chunksMap.forEach((chunkCoord, bitSet) -> {
            try {
                ChunkDataHandler.getInstance(world).saveChunkData(chunkCoord, bitSet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @EventHandler
    public void onPluginEnable(final PluginEnableEvent event) {
        Bukkit.getWorlds().forEach(world -> {
            for (Chunk chunk : world.getLoadedChunks()) {
                executorService.submit(() -> {
                    loadChunk(chunk.getWorld(), new ChunkCoord(chunk.getX(), chunk.getZ()));
                });
            }
        });
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Bukkit.getWorlds().forEach(world -> {
            Map<ChunkCoord, BitSet> chunksMap = globalChunkMap.get(world.getName());
            if (chunksMap == null) return;
            executorService.submit(() -> saveChunks(world, chunksMap));
            globalChunkMap.clear();

        });
    }


    @EventHandler
    private void onWorldSave(WorldSaveEvent event) {
        World world = event.getWorld();
        Map<ChunkCoord, BitSet> chunksMap = globalChunkMap.get(world.getName());
        if (chunksMap == null) return;
        executorService.submit(() -> saveChunks(world, chunksMap));
    }

    @EventHandler
    private void onChunkLoad(final ChunkLoadEvent event) throws IOException {
        loadChunk(event.getWorld(), new ChunkCoord(event.getChunk().getX(), event.getChunk().getZ()));
    }

    @EventHandler
    private void onChunkUnload(final ChunkUnloadEvent event) {
        unloadChunk(event.getWorld(), new ChunkCoord(event.getChunk().getX(), event.getChunk().getZ()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void playerInteract(final PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (event.isBlockInHand()) return;
        if (isBlockPlaced(event.getClickedBlock().getLocation())) {
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<gray>Clicked block was placed by a player</gray>"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onBlockPlace(final BlockPlaceEvent event) {
        updateBlock(event.getBlock().getLocation(), true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onBlockBreak(final BlockBreakEvent event) {
        updateBlock(event.getBlock().getLocation(), false);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onBlockExplode(final BlockExplodeEvent event) {
        updateBlock(event.getBlock().getLocation(), false);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onBlockFade(final BlockFadeEvent event) {
        updateBlock(event.getBlock().getLocation(), false);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onBlockBurn(final BlockBurnEvent event) {
        updateBlock(event.getBlock().getLocation(), false);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onBlockPhysics(final BlockPhysicsEvent event) {
        if (!event.getChangedType().isAir()) return;
        updateBlock(event.getBlock().getLocation(), false);
    }

    private void updateBlock(final Location loc, final boolean place) {
        int x = loc.getBlockX();
        int y = loc.getBlockY() + 64;
        int z = loc.getBlockZ();
        World world = loc.getWorld();
        ConcurrentMap<ChunkCoord, BitSet> chunksMap = globalChunkMap.computeIfAbsent(world.getName(), k -> new ConcurrentHashMap<>());
        ChunkCoord chunkCoord = new ChunkCoord(x >> 4, z >> 4);
        int blockIndex = (y * 16 + (x & 15)) * 16 + (z & 15);
        chunksMap.compute(chunkCoord, (cc, bitSet) -> {
            if (bitSet == null) bitSet = new BitSet(16 * 16 * 383);
            if (place) bitSet.set(blockIndex);
            else bitSet.clear(blockIndex);
            return bitSet;
        });
    }

    private boolean isBlockPlaced(final Location loc) {
        int x = loc.getBlockX();
        int y = loc.getBlockY() + 64;
        int z = loc.getBlockZ();
        String worldName = loc.getWorld().getName();
        Map<ChunkCoord, BitSet> chunksMap = globalChunkMap.get(worldName);
        if (chunksMap == null) {
            return false;
        }
        ChunkCoord chunkCoord = new ChunkCoord(x >> 4, z >> 4);
        int blockIndex = (y * 16 + (x & 15)) * 16 + (z & 15);
        BitSet bitSet = chunksMap.get(chunkCoord);
        return bitSet != null && bitSet.get(blockIndex);
    }
}
