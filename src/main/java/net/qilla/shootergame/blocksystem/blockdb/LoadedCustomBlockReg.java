package net.qilla.shootergame.blocksystem.blockdb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class LoadedCustomBlockReg {

    private final ConcurrentMap<String, ConcurrentMap<ChunkCoord, Map<Integer, MineableData>>> globalChunkMap = new ConcurrentHashMap<>();

    public ConcurrentMap<String, ConcurrentMap<ChunkCoord, Map<Integer, MineableData>>> getGlobalChunkMap() {
        return globalChunkMap;
    }

    public void addChunk(String world, ChunkCoord chunkCoord,  Map<Integer, MineableData> map) {
        globalChunkMap.computeIfAbsent(world, k -> new ConcurrentHashMap<>()).put(chunkCoord, map);
    }
}