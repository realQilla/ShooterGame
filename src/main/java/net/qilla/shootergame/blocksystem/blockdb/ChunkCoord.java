package net.qilla.shootergame.blocksystem.blockdb;

public record ChunkCoord(int x, int z) {

    @Override
    public String toString() {
        return "chunk_" + x + "_" + z;
    }
}