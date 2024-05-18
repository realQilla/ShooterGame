package net.qilla.zombieshooter.BlockSystem;

import com.google.gson.Gson;
import org.bukkit.World;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class ChunkDataHandler {

    private static final Gson gson = new Gson();
    private static final Map<String, ChunkDataHandler> instances = new HashMap<>();

    private final String folderPath;

    private ChunkDataHandler(World world) {
        this.folderPath = world.getWorldFolder().getPath() + File.separator + "custom";
    }

    public synchronized static ChunkDataHandler getInstance(World world) {
        return instances.computeIfAbsent(world.getName(), k -> new ChunkDataHandler(world));
    }


    public void saveChunkData(ChunkCoord chunkCoord, BitSet bitSet) throws IOException {
        if(bitSet == null) return;
        Path filePath = Paths.get(folderPath, chunkCoord.toString() + ".json");
        if(!Files.exists(filePath.getParent())) Files.createDirectories(filePath.getParent());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            gson.toJson(bitSet, writer);
        } catch (IOException exc) {
            throw new IOException(exc);
        }
    }

    public Map<ChunkCoord, BitSet> loadChunkData(ChunkCoord chunkCoord) throws IOException {
        Path filePath = Paths.get(folderPath, chunkCoord.toString() + ".json");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            BitSet bitSet = gson.fromJson(reader, BitSet.class);
            Map<ChunkCoord, BitSet> chunkDataMap = new HashMap<>();
            chunkDataMap.put(chunkCoord, bitSet);
            System.out.println("Chunk data map has been created for " + chunkCoord);
            return chunkDataMap;
        } catch (IOException exc) {
            throw new IOException(exc);
        }
    }

    public boolean lookupChunk(ChunkCoord chunkCoord) {
        Path filePath = Paths.get(folderPath, chunkCoord.toString() + ".json");
        if(Files.exists(filePath)) {
            System.out.println("Chunk data found for " + chunkCoord);
            return true;
        } else {
            return false;
        }
    }
}