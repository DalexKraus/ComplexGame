package at.dalex.grape.graphics;

import at.dalex.grape.map.chunk.Chunk;
import at.dalex.grape.map.chunk.ChunkWorld;
import org.joml.Matrix4f;

public class ChunkWorldRenderer {

    private BatchRenderer chunkRenderer;
    private Tileset tileset;
    private ChunkWorld world;

    public ChunkWorldRenderer(Tileset tileset, ChunkWorld world) {
        this.chunkRenderer = new BatchRenderer(
                tileset.getRawTextureImage(),
                tileset.getNumberOfRows(),
                tileset.getNumberOfColumns());
        this.tileset = tileset;
        this.world = world;
    }

    public void queueChunkAt(int xPos, int yPos) {
        Chunk chunk  = world.getChunkAt(xPos, yPos);
        if (chunk != null) {
            for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                    int size = tileset.getTileSize();
                    chunkRenderer.queueRender(
                            (xPos * Chunk.CHUNK_SIZE * size) + x * size,
                            (yPos * Chunk.CHUNK_SIZE * size) + y * size,
                            size, size, chunk.getTileAt(x, y).getId());
                }
            }
        }
    }

    public void cacheChunksInRange(int centerChunkX, int centerChunkY) {
        for (int xOffset = -3; xOffset < 3; xOffset++)
            for (int yOffset = -3; yOffset < 3; yOffset++) {
                world.generateChunkAt(centerChunkX + xOffset, centerChunkY + yOffset);
                queueChunkAt(centerChunkX + xOffset, centerChunkY + yOffset);
            }
    }

    public void drawChunkQueue(Matrix4f projectionAndViewMatrix) {
        chunkRenderer.drawQueue(projectionAndViewMatrix);
        chunkRenderer.flush();
    }
}
