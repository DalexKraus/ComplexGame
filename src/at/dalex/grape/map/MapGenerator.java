package at.dalex.grape.map;

import at.dalex.grape.graphics.Tileset;
import at.dalex.grape.graphics.ImageUtils;

public class MapGenerator {

	private final static int WATER = 0;
	private final static int GRASS = 1;
	private final static int STONE = 2;
	private final static int SNOW = 3;
	private final static int SAND = 4;
	
	private static PerlinNoise perlinNoiseGenerator;
	
	public static Map generateFromPerlinNoise(int width, int height, int seed) {
		perlinNoiseGenerator = new PerlinNoise(seed);
		Tileset tileset = new Tileset(ImageUtils.loadBufferedImage("textures/base.png"), 16);
		Map map = new Map(width, height);
		MapLayer layer = new MapLayer(width, height, tileset);
		
		float[][] values = perlinNoiseGenerator.generatePerlinNoise(0, 0, width, height, 6);
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				float value = values[i][j];
				if (value <= 0.45) layer.setTileAt(i, j, WATER, false, false);
				else if (value > 0.45 && value <= 0.47) layer.setTileAt(i, j, SAND, false, false);
				else if (value > 0.47 && value <= 0.7) layer.setTileAt(i, j, GRASS, false, false);
				else if  (value > 0.7 && value < 0.9) layer.setTileAt(i, j, STONE, false, false);
				else layer.setTileAt(i, j, SNOW, false, false);
			}
		}
		map.addLayer(layer);
		
		//Tileset
		map.setScale(0.25f);
		
		return map;
	}
}
