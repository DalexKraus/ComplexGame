package at.dalex.grape.graphics;

import at.dalex.grape.map.Map;
import at.dalex.grape.map.MapLayer;
import org.joml.Matrix4f;

public class TilemapRenderer {

    private BatchRenderer tileRenderer;
    private Map map;

    public TilemapRenderer(Map map, Tileset tileset) {
        this.map = map;
        int atlasRows = tileset.getRawTextureImage().getHeight() / tileset.getTileSize();
        int atlasColumns = tileset.getRawTextureImage().getWidth() / tileset.getTileSize();
        this.tileRenderer = new BatchRenderer(tileset.getRawTextureImage(), atlasRows, atlasColumns);
    }

    public void preCacheRender() {
        //Clear previous cache
        tileRenderer.flush();
        for (int i = 0; i < map.getLayerCount(); i++) {
            MapLayer currentLayer = map.getLayer(i);
            currentLayer.draw(tileRenderer, map.getX(), map.getY(), map.getScale());
        }
    }

    public void draw(Matrix4f projectionAndViewMatrix) {
        //Draw entire queue at once
        tileRenderer.drawQueue(projectionAndViewMatrix);
    }
}
