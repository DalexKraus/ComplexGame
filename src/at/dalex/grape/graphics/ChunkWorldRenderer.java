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

    public void queueChunkAt(int xPos, int yPos, int tileSize) {
        Chunk chunk  = world.getChunkAt(xPos, yPos);
        if (chunk != null) {
            for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                    chunkRenderer.queueRender(
                            (xPos * Chunk.CHUNK_SIZE * tileSize) + x * tileSize,
                            (yPos * Chunk.CHUNK_SIZE * tileSize) + y * tileSize,
                            tileSize, tileSize, chunk.getTileAt(x, y).getId());
                }
            }
        }
    }

    public void cacheChunksInRange(int centerChunkX, int centerChunkY, int tileSize) {
        for (int xOffset = -3; xOffset < 3; xOffset++)
            for (int yOffset = -3; yOffset < 3; yOffset++) {
                world.generateChunkAt(centerChunkX + xOffset, centerChunkY + yOffset);
                queueChunkAt(centerChunkX + xOffset, centerChunkY + yOffset, tileSize);
            }
    }

    public void drawChunkQueue(Matrix4f projectionAndViewMatrix) {
        chunkRenderer.drawQueue(projectionAndViewMatrix);
        chunkRenderer.flush();
    }
}
