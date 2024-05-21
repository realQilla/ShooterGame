package net.qilla.zombieshooter.BlockSystem.BlockDatabase;

public record ChunkCoord(int x, int z) {

    @Override
    public String toString() {
        return "chunk_" + x + "_" + z;
    }
}