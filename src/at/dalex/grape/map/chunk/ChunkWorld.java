package at.dalex.grape.map.chunk;

import at.dalex.grape.map.Tile;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Random;

public class ChunkWorld implements IChunkProvider {

    private final int SEED;
    private OpenSimplexNoise simplexNoise;
    private HashMap<Vector2i, Chunk> generatedChunks;

    public ChunkWorld() {
        this(new Random().nextInt(Integer.MAX_VALUE));
    }

    public ChunkWorld(int seed) {
        this.SEED = seed;
        this.simplexNoise = new OpenSimplexNoise(SEED);
        this.generatedChunks = new HashMap<>();
    }

    @Override
    public Chunk getChunkAt(int x, int y) {
        for (Vector2i chunkPos : generatedChunks.keySet()) {
            if (chunkPos.x == x && chunkPos.y == y)
                return generatedChunks.get(chunkPos);
        }
        return null;
    }

    @Override
    public void deleteChunkAt(int x, int y) {
        for (Vector2i chunkPos : generatedChunks.keySet()) {
            if (chunkPos.x == x && chunkPos.y == y) {
                generatedChunks.remove(chunkPos);
            }
        }
    }

    @Override
    public void generateChunkAt(int x, int y) {
        if (!isChunkGenerated(x, y)) {
            Chunk chunk = new Chunk(x, y);
            for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
                for (int j = 0; j < Chunk.CHUNK_SIZE; j++) {
                    int WATER = 0;
                    int GRASS = 1;
                    int STONE = 2;
                    int SNOW = 3;
                    int SAND = 4;
                    double value = simplexNoise.eval(
                            (i + chunk.getChunkX() * Chunk.CHUNK_SIZE) / 13f,
                            (j + chunk.getChunkY() * Chunk.CHUNK_SIZE) / 13f);
                    value = (value + 1) / 2f;
                    int tileId;
                    if (value <= 0.30) tileId = WATER;
                    else if (value > 0.30 && value <= 0.47) tileId = SAND;
                    else if (value > 0.47 && value <= 0.7) tileId = GRASS;
                    else if  (value > 0.7 && value < 0.9) tileId = STONE;
                    else tileId = SNOW;
                    chunk.setTileAt(i, j, new Tile(tileId, false, false));
                }
            }
            this.generatedChunks.put(new Vector2i(x, y), chunk);
        }
    }

    @Override
    public void regenerateChunkAt(int x, int y) {
        deleteChunkAt(x, y);
        generateChunkAt(x, y);
    }

    @Override
    public boolean isChunkGenerated(int x, int y) {
        for (Vector2i chunkPos : generatedChunks.keySet())
            if (chunkPos.x == x && chunkPos.y == y) return true;
        return false;
    }

    public int getSeed() {
        return this.SEED;
    }
}
