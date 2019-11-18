package at.dalex.grape.gamestatemanager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import at.dalex.grape.GrapeEngine;
import at.dalex.grape.graphics.BatchRenderer;
import at.dalex.grape.graphics.ChunkWorldRenderer;
import at.dalex.grape.graphics.TilemapRenderer;
import at.dalex.grape.graphics.Tileset;
import at.dalex.grape.graphics.font.BitmapFont;
import at.dalex.grape.graphics.graphicsutil.ImageUtils;
import at.dalex.grape.map.MapGenerator;
import at.dalex.grape.map.chunk.ChunkWorld;
import org.joml.Matrix4f;

import at.dalex.grape.entity.Entity;
import at.dalex.grape.map.Map;
import at.dalex.grape.graphics.graphicsutil.Graphics;

public class PlayState extends GameState {
	
	public static Map current_map;
	public static ArrayList<Entity> entities = new ArrayList<>();

	private ChunkWorld world;
	private ChunkWorldRenderer chunkWorldRenderer;
	private TilemapRenderer renderer;

	private BatchRenderer batchRenderer;

	private BitmapFont font;

	@Override
	public void init() {
		current_map = MapGenerator.generateFromPerlinNoise(256, 256, new Random().nextInt());
		current_map.setScale(0.125f);
		this.world = new ChunkWorld(2);
		BufferedImage atlas = ImageUtils.loadBufferedImage("textures/base.png");
		renderer = new TilemapRenderer(current_map, new Tileset(atlas, 16));
		renderer.preCacheRender();
		world.generateChunkAt(0, 0);
		chunkWorldRenderer = new ChunkWorldRenderer(new Tileset(atlas, 16), world);
		font = new BitmapFont(new Font("Arial", Font.PLAIN, 24), true);

		this.batchRenderer = new BatchRenderer(ImageUtils.convertBufferedImage(atlas), 3, 2);
	}

	@Override
	public void draw(Matrix4f projectionAndViewMatrix) {
		Graphics.enableBlending(true);

//		chunkWorldRenderer.cacheChunksInRange(0, 0);
//		chunkWorldRenderer.drawChunkQueue(GrapeEngine.getEngine().getCamera().getProjectionAndViewMatrix());

//		for (Entity entity : entities) {
//			entity.draw(GrapeEngine.getEngine().getCamera().getProjectionMatrix());
//		}

//		font.drawText("Hello World!", 0, 0, projectionAndViewMatrix);

		batchRenderer.queueRender(128, 128, 64, 64, 2);
		batchRenderer.queueRender(64, 128, 64, 64, 0);
		batchRenderer.queueRender(512, 128, 64, 64, 3);
		batchRenderer.queueRender(128, 256, 64, 64, 5);
		batchRenderer.drawQueue(projectionAndViewMatrix);
		batchRenderer.flush();

		Graphics.enableBlending(false);
	}

	@Override
	public void update(double delta) {
//		if (current_map != null)
//			current_map.update();
//
//		for (Entity entity : entities) {
//			entity.update(delta);
//		}
	}
}
