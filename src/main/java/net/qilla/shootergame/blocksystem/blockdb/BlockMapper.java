package net.qilla.shootergame.blocksystem.blockdb;

import org.bukkit.Bukkit;
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

public final class BlockMapper {

    private static final BlockMapper instance = new BlockMapper();

    private BlockMapper() {
    }

    public static ConcurrentMap<String, ConcurrentMap<ChunkCoord, Map<Integer, MineableData>>> globalChunkMap = new ConcurrentHashMap<>();
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void mapChunkFromDB(World world, final ChunkCoord chunkCoord) {
        executorService.submit(() -> {
            BlockDBManager chunkHolder = new BlockDBManager(world, chunkCoord);
            if(!chunkHolder.lookupChunkInDB()) return;
            try {
                ConcurrentMap<ChunkCoord, Map<Integer, MineableData>> localChunkMap = globalChunkMap.computeIfAbsent(world.getName(), k -> new ConcurrentHashMap<>());
                localChunkMap.putAll(chunkHolder.getChunkFromDB());
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void unMapChunkSendToDB(final World world, final ChunkCoord chunkCoord) {
        executorService.submit(() -> {
            if(!globalChunkMap.containsKey(world.getName())) return;
            globalChunkMap.get(world.getName()).computeIfPresent(chunkCoord, (cc, blockMap) -> {
                try {
                    new BlockDBManager(world, chunkCoord).sendChunkToDB(blockMap);
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
        });
    }

    public void sendWorldToDB(final World world) {
        executorService.submit(() -> {
            Map<ChunkCoord, Map<Integer, MineableData>> localChunkMap = globalChunkMap.get(world.getName());
            if(localChunkMap == null) return;
            localChunkMap.forEach((chunkCoord, blockMap) -> {
                try {
                    new BlockDBManager(world, chunkCoord).sendChunkToDB(blockMap);
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    public void sendGlobalToDB() {
        Bukkit.getWorlds().forEach(this::sendWorldToDB);
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

    public void addBlock(@NotNull final Location loc, final short blockID, boolean isPermanent) {
        final World world = loc.getWorld();
        final ChunkCoord chunkCoord = getChunkCoord(loc);
        final int blockIndex = getBlockIndex(loc);
        final MineableData mineableData = new MineableData(isPermanent, blockID);
        final ConcurrentMap<ChunkCoord, Map<Integer, MineableData>> localChunkMap = globalChunkMap.computeIfAbsent(world.getName(), k -> new ConcurrentHashMap<>());
        localChunkMap.compute(chunkCoord, (cc, blockMap) -> {
            if(blockMap == null) blockMap = new ConcurrentHashMap<>();
            blockMap.put(blockIndex, mineableData);
            return blockMap;
        });
    }

    public void removeBlock(@NotNull final Location loc) {
        final World world = loc.getWorld();
        final ChunkCoord chunkCoord = getChunkCoord(loc);
        final int blockIndex = getBlockIndex(loc);
        final ConcurrentMap<ChunkCoord, Map<Integer, MineableData>> localChunkMap = globalChunkMap.get(world.getName());
        if(localChunkMap == null) return;
        localChunkMap.compute(chunkCoord, (cc, blockMap) -> {
            if(blockMap == null) return null;
            blockMap.remove(blockIndex);
            return blockMap;
        });
    }

    @Nullable
    public MineableData getMineableData(@NotNull final Location blockLoc) {
        final World world = blockLoc.getWorld();
        final ChunkCoord chunkCoord = getChunkCoord(blockLoc);
        final int blockIndex = getBlockIndex(blockLoc);
        if(!globalChunkMap.containsKey(world.getName())) return null;
        final Map<Integer, MineableData> blockMap = globalChunkMap.get(world.getName()).get(chunkCoord);
        if(blockMap == null) return null;
        return blockMap.get(blockIndex);
    }


    public static BlockMapper getInstance() {
        return instance;
    }

}