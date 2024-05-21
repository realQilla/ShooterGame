package net.qilla.zombieshooter.BlockSystem.BlockDatabase;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class NodeMapping {

    private static final NodeMapping instance = new NodeMapping();

    private NodeMapping() {
    }

    public static ConcurrentMap<String, ConcurrentMap<ChunkCoord, Map<Integer, Short>>> nodeMap = new ConcurrentHashMap<>();
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void mapChunkFromDB(final World world, final ChunkCoord chunkCoord) {
        BlockDBWriter chunkHolder = BlockDBWriter.getInstance(world, BlockDBWriter.StorageType.BREAKABLE);
        if (!chunkHolder.checkDBForChunk(chunkCoord)) return;
        executorService.submit(() -> {
            try {
                ConcurrentMap<ChunkCoord, Map<Integer, Short>> chunksMap = nodeMap.computeIfAbsent(world.getName(), k -> new ConcurrentHashMap<>());
                chunksMap.putAll(chunkHolder.loadFromDB(chunkCoord));
                System.out.println("Chunk " + chunkCoord + " loaded. " + nodeMap.get(world.getName()).size() + " entries in " + world.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void unMapChunkSendToDB(final World world, final ChunkCoord chunkCoord) {
        executorService.submit(() -> {
            if (!nodeMap.containsKey(world.getName())) return;
            nodeMap.get(world.getName()).computeIfPresent(chunkCoord, (key, bitSet) -> {
                try {
                    BlockDBWriter.getInstance(world, BlockDBWriter.StorageType.BREAKABLE).sendToDB(chunkCoord, bitSet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Chunk " + chunkCoord + " unloaded. " + nodeMap.get(world.getName()).size() + " entries in " + world.getName());
                return null;
            });
        });
    }

    public void sendWorldToDB(final World world) {
        Map<ChunkCoord, Map<Integer, Short>> chunksMap = nodeMap.get(world.getName());
        if (chunksMap == null) return;
        chunksMap.forEach((chunkCoord, blockMap) -> {
            try {
                BlockDBWriter.getInstance(world, BlockDBWriter.StorageType.BREAKABLE).sendToDB(chunkCoord, blockMap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @NotNull
    private ChunkCoord getChunkCoord(@NotNull final Location loc) {
        return new ChunkCoord(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
    }

    private int getBlockIndex(@NotNull final Location loc) {
        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();
        return ((y * 16 + (x & 15)) * 16 + (z & 15));
    }

    public void addBlock(@NotNull final Location loc, short blockID) {
        final World world = loc.getWorld();
        final ChunkCoord chunkCoord = getChunkCoord(loc);
        final int blockIndex = getBlockIndex(loc);
        final ConcurrentMap<ChunkCoord, Map<Integer, Short>> chunksMap = nodeMap.computeIfAbsent(world.getName(), k -> new ConcurrentHashMap<>());
        chunksMap.compute(chunkCoord, (cc, blockMap) -> {
            if (blockMap == null) blockMap = new ConcurrentHashMap<>();
            blockMap.put(blockIndex, blockID);
            return blockMap;
        });
    }

    public void removeBreakable(@NotNull final Location loc) {
        final World world = loc.getWorld();
        final ChunkCoord chunkCoord = getChunkCoord(loc);
        final int blockIndex = getBlockIndex(loc);
        final Map<ChunkCoord, Map<Integer, Short>> chunksMap = nodeMap.get(world.getName());
        if (chunksMap == null) return;
        chunksMap.compute(chunkCoord, (cc, blockMap) -> {
            if (blockMap == null) return null;
            blockMap.remove(blockIndex);
            return blockMap;
        });
    }

    @Nullable
    public Short getBlockID(@NotNull final Location blockLoc) {
        final World world = blockLoc.getWorld();
        final ChunkCoord chunkCoord = getChunkCoord(blockLoc);
        final int blockIndex = getBlockIndex(blockLoc);
        if(!nodeMap.containsKey(world.getName())) return null;
        final Map<ChunkCoord, Map<Integer, Short>> chunksMap = nodeMap.get(world.getName());
        Map<Integer, Short> blockMap = chunksMap.get(chunkCoord);
        return blockMap != null ? blockMap.get(blockIndex) : null;
    }

    public static NodeMapping getInstance() {
        return instance;
    }

}
