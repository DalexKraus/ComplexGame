package at.dalex.grape.map.chunk;

import at.dalex.grape.map.Tile;

public class Chunk {

    private int chunkX;
    private int chunkY;
    private Tile[][] blocks;

    public static final int CHUNK_SIZE = 16;

    public Chunk(int chunkX, int chunkY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.blocks = new Tile[CHUNK_SIZE][CHUNK_SIZE];
    }

    public Tile getTileAt(int x, int y) {
        return this.blocks[x][y];
    }

    public void setTileAt(int x, int y, Tile tile) {
        this.blocks[x][y] = tile;
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public int getChunkY() {
        return this.chunkY;
    }
}
