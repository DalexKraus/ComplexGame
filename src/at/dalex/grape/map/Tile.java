package at.dalex.grape.map;

public class Tile {

	private int tileId;
	private boolean isSolid;
	private boolean isEmitter;

	public Tile(int tileId, boolean isSolid, boolean isEmitter) {
		this.tileId = tileId;
		this.isSolid = isEmitter;
		this.isEmitter = isEmitter;
	}
	
	public int getId() {
		return this.tileId;
	}
	
	public boolean isSolid() {
		return this.isSolid;
	}
	
	public boolean isEmitter() {
		return this.isEmitter;
	}
}
