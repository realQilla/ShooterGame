package net.qilla.shootergame.blocksystem.blockdb;

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

public class BlockDBManager {

    private static final Gson gson = new Gson();

    private final Path path;
    private final ChunkCoord chunkCoord;

    protected BlockDBManager(World world, ChunkCoord chunkCoord) {
        this.path = Paths.get(world.getWorldFolder().getPath() + File.separator + "mineable", chunkCoord.toString() + ".json");
        this.chunkCoord = chunkCoord;
    }

    public void sendChunkToDB(Map<Integer, MineableData> blockMap) throws IOException {
        if (blockMap == null || blockMap.isEmpty()) {
            if (lookupChunkInDB()) deleteChunkFromDB();
            return;
        }

        if (!Files.exists(path.getParent())) Files.createDirectories(this.path.getParent());
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.path.toFile()));
        gson.toJson(blockMap, writer);
        writer.close();
        System.out.println("Chunk data for " + this.chunkCoord + " has been saved to the database.");
    }

    public Map<ChunkCoord, Map<Integer, MineableData>> getChunkFromDB() throws IOException {
        if (!Files.exists(path.getParent())) Files.createDirectories(this.path.getParent());
        BufferedReader reader = new BufferedReader(new FileReader(this.path.toFile()));

        Type type = new TypeToken<Map<Integer, MineableData>>() {}.getType();
        Map<Integer, MineableData> blockMap = gson.fromJson(reader, type);
        reader.close();
        Map<ChunkCoord, Map<Integer, MineableData>> chunkDataMap = new HashMap<>();
        chunkDataMap.put(this.chunkCoord, blockMap);
        System.out.println("Chunk " + this.chunkCoord + " has been loaded into the chunk pool.");
        return chunkDataMap;
    }

    private void deleteChunkFromDB() throws IOException {
        if (!(Files.exists(this.path.getParent()) || Files.exists(this.path))) throw new FileNotFoundException("Could not delete " + this.chunkCoord + " because the file or directory does not exist.");
        System.out.println("Chunk data for " + this.chunkCoord + " has been deleted from the database.");
        Files.delete(this.path);
    }

    public boolean lookupChunkInDB() {
        return Files.exists(this.path);
    }
}