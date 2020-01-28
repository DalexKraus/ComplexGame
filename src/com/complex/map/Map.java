package com.complex.map;

import java.util.ArrayList;
import java.util.Iterator;

public class Map {
    ArrayList<Layer> layers = new ArrayList<>();

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public Iterator getLayers() {
        return layers.iterator();
    }
}
