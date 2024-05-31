package net.qilla.shootergame.blocksystem.blockdb;

import net.qilla.shootergame.ShooterGame;
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

    private final LoadedCustomBlockReg loadedCustomBlockReg;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public BlockMapper(LoadedCustomBlockReg loadedCustomBlockReg) {
        this.loadedCustomBlockReg = loadedCustomBlockReg;
    }

    public void mapChunkFromDB(World world, final ChunkCoord chunkCoord) {
        this.executorService.submit(() -> {
            BlockDBManager chunkHolder = new BlockDBManager(world, chunkCoord);
            if(!chunkHolder.lookupChunkInDB()) return;
            try {
                ConcurrentMap<ChunkCoord, Map<Integer, MineableData>> localChunkMap = this.loadedCustomBlockReg.getGlobalChunkMap().computeIfAbsent(world.getName(), k -> new ConcurrentHashMap<>());
                localChunkMap.putAll(chunkHolder.getChunkFromDB());
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void unMapChunkSendToDB(final World world, final ChunkCoord chunkCoord) {
        this.executorService.submit(() -> {
            if(!this.loadedCustomBlockReg.getGlobalChunkMap().containsKey(world.getName())) return;
            this.loadedCustomBlockReg.getGlobalChunkMap().get(world.getName()).computeIfPresent(chunkCoord, (cc, blockMap) -> {
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
        this.executorService.submit(() -> {
            Map<ChunkCoord, Map<Integer, MineableData>> localChunkMap = this.loadedCustomBlockReg.getGlobalChunkMap().get(world.getName());
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
        final ConcurrentMap<ChunkCoord, Map<Integer, MineableData>> localChunkMap = this.loadedCustomBlockReg.getGlobalChunkMap().computeIfAbsent(world.getName(), k -> new ConcurrentHashMap<>());
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
        final ConcurrentMap<ChunkCoord, Map<Integer, MineableData>> localChunkMap = this.loadedCustomBlockReg.getGlobalChunkMap().get(world.getName());
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
        if(!this.loadedCustomBlockReg.getGlobalChunkMap().containsKey(world.getName())) return null;
        final Map<Integer, MineableData> blockMap = this.loadedCustomBlockReg.getGlobalChunkMap().get(world.getName()).get(chunkCoord);
        if(blockMap == null) return null;
        return blockMap.get(blockIndex);
    }
}