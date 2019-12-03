package at.dalex.grape.gamestatemanager;

import at.dalex.grape.entity.Entity;
import at.dalex.grape.graphics.*;
import at.dalex.grape.map.chunk.ChunkWorld;
import com.complex.entity.Player;
import org.joml.Matrix4f;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlayState extends GameState {

	private Player player;
	private ArrayList<Entity> entities = new ArrayList<>();

	private ChunkWorld chunkWorld;
	private ChunkWorldRenderer renderer;

	@Override
	public void init() {
		player = new Player(512, 512);
		entities.add(player);

		this.chunkWorld = new ChunkWorld();

		chunkWorld.generateChunkAt(0, 0);

		BufferedImage atlas = ImageUtils.loadBufferedImage("textures/base.png");
		Tileset tileset = new Tileset(atlas, 16);
		this.renderer = new ChunkWorldRenderer(tileset, chunkWorld);
	}

	@Override
	public void draw(Matrix4f projectionAndViewMatrix) {
		renderer.cacheChunksInRange(3, 3, 4);

		renderer.drawChunkQueue(projectionAndViewMatrix);
		entities.forEach(entity -> entity.draw(projectionAndViewMatrix));
	}

	@Override
	public void update(double delta) {
		entities.forEach(entity -> entity.update(delta));
	}
}
