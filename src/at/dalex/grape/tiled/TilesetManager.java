package at.dalex.grape.tiled;

import java.awt.image.BufferedImage;

import at.dalex.grape.graphics.Tileset;

public class TilesetManager {

	private Tileset currentTileset;
	private TilesetData tilesetData;
	private boolean updateTileset = false;
	
	public void loadTileset(TilesetData tilesetData) {
		this.tilesetData = tilesetData;
		updateTileset = true;
	}
	
	public void updateTileset() {
		if (updateTileset) {
			if (tilesetData != null) {
				currentTileset = new Tileset(tilesetData.getTilesetImage(), tilesetData.getTileSize());
//				EventCenter.getCenter().addToQueue(new TilesetLoadedEvent(currentTileset));
				if (tilesetData.getTilesetParseListener() != null) {
					tilesetData.getTilesetParseListener().tilesetFinishedLoading();
				}
				updateTileset = false;
			}
		}
	}
	
	public Tileset getCurrentTileset() {
		return this.currentTileset;
	}
	
	public static class TilesetData {
		
		private BufferedImage tilesetSrc;
		private int tileSize;

		private TilesetParseListener parseListener;

		public TilesetData(BufferedImage tilesetSrc, int tileSize) {
			this.tilesetSrc = tilesetSrc;
			this.tileSize = tileSize;
		}

		public void addParseListener(TilesetParseListener parseListener) {
			this.parseListener = parseListener;
		}
		
		public BufferedImage getTilesetImage() {
			return this.tilesetSrc;
		}
		
		public int getTileSize() {
			return this.tileSize;
		}

		public TilesetParseListener getTilesetParseListener() {
			return this.parseListener;
		}
	}

	public static interface TilesetParseListener {

		void tilesetFinishedLoading();
	}
}
