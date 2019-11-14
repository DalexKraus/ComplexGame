package at.dalex.grape.tiled;

import java.util.ArrayList;

import org.joml.Matrix4f;

import at.dalex.grape.GrapeEngine;
import at.dalex.grape.graphics.Tileset;
import at.dalex.grape.graphics.graphicsutil.Graphics;
import at.dalex.grape.graphics.graphicsutil.Image;
/**
 * Created by Da vid on 20.10.2017.
 */

public class MapLayer {

    private ArrayList<ArrayList<MapTile>> tiles = new ArrayList<ArrayList<MapTile>>();
    private int width;
    private int height;
    private String name;

    public MapLayer(int width, int height) {
        this.width = width;
        this.height = height;

        //Fill layer with blank numbers
        for (int y = 0; y < height; y++) {
            ArrayList<MapTile> list = new ArrayList<MapTile>();
            for (int x = 0; x < width; x++) {
                list.add(new MapTile(Integer.MAX_VALUE, false, false, false));
            }
            tiles.add(list);
        }
    }

    public ArrayList<ArrayList<MapTile>> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayList<ArrayList<MapTile>> tiles) {
        this.tiles = tiles;
    }

    public MapTile getTileAt(int x, int y) {
        return tiles.get(y).get(x);
    }

    public void setTileAt(int x, int y, MapTile newTile) {
        tiles.get(y).set(x, newTile);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void draw(int mapX, int mapY, Matrix4f projectionAndViewMatrix) {
        Tileset tileset = GrapeEngine.getEngine().getTilesetManager().getCurrentTileset();
        int tileSize = tileset.getTileSize();

        for (int y = 0; y < tiles.size(); y++) {
            for (int x = 0; x < tiles.get(0).size(); x++) {
                Image tileImage;
                if (tiles.get(y).get(x).getId() > 0)
                    tileImage = tileset.getTileList().get((tiles.get(y).get(x).getId() - 1));
                else
                    tileImage = tileset.getTileList().get(tiles.get(y).get(x).getId());

                Graphics.drawImage(tileImage, mapX + (x * tileSize), mapY + (y * tileSize), tileSize, tileSize, projectionAndViewMatrix);
            }
        }
    }
}
