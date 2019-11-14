package at.dalex.grape.map.chunk;

public interface IChunkProvider {

    Chunk getChunkAt(int x, int y);
    void deleteChunkAt(int x, int y);
    void generateChunkAt(int x, int y);
    void regenerateChunkAt(int x, int y);
    boolean isChunkGenerated(int x, int y);
}
