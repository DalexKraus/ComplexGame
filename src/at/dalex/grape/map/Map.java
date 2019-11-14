package at.dalex.grape.map;

import java.util.ArrayList;
import java.util.List;

import at.dalex.grape.graphics.BatchRenderer;
import at.dalex.grape.graphics.graphicsutil.Image;
import org.joml.Matrix4f;

public class Map {

	private List<MapLayer> layers = new ArrayList<>();
	private int width;
	private int height;
	
	private int x;
	private int y;
	
	private float scale_factor = 1f;

	public Map(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void update() { }

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public MapLayer getLayer(int zIndex) {
		return layers.get(zIndex);
	}

	public void addLayer(MapLayer layer) {
		layers.add(layer);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getLayerCount() {
		return this.layers.size();
	}

	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}

	public void scale(float scaleFactor) {
		scale_factor += scaleFactor;
	}

	public void setScale(float scale) {
		scale_factor = scale;
	}

	public float getScale() {
		return this.scale_factor;
	}
}
