package at.dalex.grape.tiled;

import java.util.ArrayList;

import org.joml.Vector2f;

/**
 * Created by Da vid on 20.10.2017.
 */

public class MapInfo {

    private ArrayList<MapLayer> layers = new ArrayList<MapLayer>();
    private Vector2f playerSpawn;

    public MapInfo(ArrayList<MapLayer> layers, Vector2f playerSpawn) {
        this.layers = layers;
        this.playerSpawn = playerSpawn;
    }

    public ArrayList<MapLayer> getLayers() {
        return layers;
    }

    public void setLayers(ArrayList<MapLayer> layers) {
        this.layers = layers;
    }

    public Vector2f getPlayerSpawn() {
        return playerSpawn;
    }

    public void setPlayerSpawn(Vector2f playerSpawn) {
        this.playerSpawn = playerSpawn;
    }
}
