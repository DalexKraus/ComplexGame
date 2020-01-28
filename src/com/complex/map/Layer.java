package com.complex.map;

public class Layer {
    int[][] tiles;
    public Layer(int[][] tiles) {
        this.tiles = tiles;
    }
    public int getTile(int x, int y) {
        return tiles[x][y];
    }
}