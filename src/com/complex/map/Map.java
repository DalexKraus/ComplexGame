package com.complex.map;

import java.util.ArrayList;

public class Map {
    ArrayList<Layer> layers = new ArrayList<>();

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }
}
