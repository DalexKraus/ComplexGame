package at.dalex.grape.map;

import java.util.ArrayList;

import at.dalex.grape.graphics.BatchRenderer;
import at.dalex.grape.graphics.Image;

import at.dalex.grape.graphics.Tileset;
import at.dalex.grape.graphics.DisplayManager;

public class MapLayer {

	private ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();
	private int width;
	private int height;
	private Tileset tileset;

	public MapLayer(int width, int height, Tileset tileset) {
		this.width = width;
		this.height = height;
		this.tileset = tileset;
		Image atlasImage = tileset.getRawTextureImage();

		int atlasWidth = atlasImage.getWidth();
		int atlasHeight = atlasImage.getHeight();
		int tileSize = tileset.getTileSize();

		//Fill layer with empty tiles
		for (int y = 0; y < height; y++) {
			ArrayList<Tile> list = new ArrayList<>();
			for (int x = 0; x < width; x++) {
				list.add(new Tile(-1, false, false));
			}
			tiles.add(list);
		}
	}

	public void draw(BatchRenderer renderer, int dx, int dy, float scale) {
		int windowWidth = DisplayManager.windowWidth;
		int windowHeight = DisplayManager.windowHeight;
		int tileSize = tileset.getTileSize();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				int xPos = (int) (((x * tileSize) + dx) * scale);
				int yPos = (int) (((y * tileSize) + dy) * scale);
				
				//To increase performance, only render what is visible
				if (xPos < windowWidth && yPos < windowHeight && xPos > -tileSize * scale && yPos > -tileSize * scale) {
					int tileId = tiles.get(y).get(x).getId();
					if (tileId > -1) {
						renderer.queueRender(xPos, yPos, (int) (tileSize * scale), (int) (tileSize * scale), tileId);
					}
				}
			}
		}
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public ArrayList<ArrayList<Tile>> getTiles() {
		return this.tiles;
	}

	public void setTileAt(int x, int y, int tileId, boolean isSolid, boolean isEmitter) {
		tiles.get(y).set(x, new Tile(tileId, isSolid, isEmitter));
	}
}
