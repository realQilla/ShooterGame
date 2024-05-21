package net.qilla.zombieshooter.BlockSystem.BlockDatabase;

import com.google.gson.Gson;
import io.leangen.geantyref.TypeToken;
import org.bukkit.World;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BlockDBWriter {

    private static final Gson gson = new Gson();
    private static final Map<String, BlockDBWriter> instances = new HashMap<>();

    private final String filePath;

    private BlockDBWriter(World world, StorageType storageType) {
        this.filePath = world.getWorldFolder().getPath() + File.separator + "custom" + File.separator + storageType.toString();
    }

    public synchronized static BlockDBWriter getInstance(World world, StorageType storageType) {
        return instances.computeIfAbsent(world.getName(), k -> new BlockDBWriter(world, storageType));
    }


    public void sendToDB(ChunkCoord chunkCoord, Map<Integer, Short> blockMap) throws IOException {
        if (blockMap == null || blockMap.isEmpty()) {
            if (checkDBForChunk(chunkCoord)) deleteFromDB(chunkCoord);
            System.out.println("Chunk data deleted for " + chunkCoord);
            return;
        }

        Path filePath = Paths.get(this.filePath, chunkCoord.toString() + ".json");
        if (!Files.exists(filePath.getParent())) Files.createDirectories(filePath.getParent());
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        gson.toJson(blockMap, writer);
        writer.close();
        System.out.println("Chunk data saved for " + chunkCoord);
    }

    public Map<ChunkCoord, Map<Integer, Short>> loadFromDB(ChunkCoord chunkCoord) throws IOException {
        Path filePath = Paths.get(this.filePath, chunkCoord.toString() + ".json");
        if (!Files.exists(filePath.getParent())) Files.createDirectories(filePath.getParent());
        BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));

        Type type = new TypeToken<Map<Integer, Short>>() {}.getType();
        Map<Integer, Short> blockMap = gson.fromJson(reader, type);
        reader.close();
        Map<ChunkCoord, Map<Integer, Short>> chunkDataMap = new HashMap<>();
        chunkDataMap.put(chunkCoord, blockMap);
        System.out.println("Chunk data map has been created for " + chunkCoord);
        return chunkDataMap;
    }

    private void deleteFromDB(ChunkCoord chunkCoord) throws IOException {
        Path filePath = Paths.get(this.filePath, chunkCoord.toString() + ".json");
        if (!Files.exists(filePath.getParent())) Files.createDirectories(filePath.getParent());
        if(!Files.exists(filePath)) return;
        System.out.println("Chunk data deleted for " + chunkCoord);
        Files.delete(filePath);
    }

    public boolean checkDBForChunk(ChunkCoord chunkCoord) {
        Path filePath = Paths.get(this.filePath, chunkCoord.toString() + ".json");
        if(Files.exists(filePath)) {
            System.out.println("Chunk data found for " + chunkCoord);
            return true;
        } else {
            return false;
        }
    }

    public enum StorageType {
        NODE("node"),
        BREAKABLE("breakable");

        private final String folder;

        StorageType(String folder) {
            this.folder = folder;
        }

        @Override
        public String toString() {
            return folder;
        }
    }
}