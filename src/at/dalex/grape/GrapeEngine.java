package at.dalex.grape;

import at.dalex.grape.graphics.font.BitmapFont;
import at.dalex.grape.graphics.graphicsutil.Graphics;
import at.dalex.grape.map.manager.MapManager;
import at.dalex.grape.toolbox.MemoryManager;
import org.lwjgl.opengl.GL11;

import at.dalex.grape.developer.GameInfo;
import at.dalex.grape.gamestatemanager.GameStateManager;
import at.dalex.grape.graphics.Camera;
import at.dalex.grape.graphics.DisplayCallback;
import at.dalex.grape.graphics.DisplayManager;
import at.dalex.grape.resource.DefaultResources;
import at.dalex.grape.tiled.TilesetManager;
import at.dalex.grape.toolbox.OSManager;

import java.awt.Font;

public abstract class GrapeEngine implements DisplayCallback {

	private String gameLocation;
	private DisplayManager displayManager;
	private GameStateManager gameStateManager;
	private TilesetManager tilesetManager;
	private MapManager mapManager;

	private GameInfo gameInfo;
	private static GrapeEngine instance;

	private BitmapFont debugFont;

	public abstract void onEnable();
	public abstract void onDisable();
	
	private Camera camera;
	
	public GrapeEngine(String gameLocation) {
		instance = this;

		OSManager.setLook();
		gameInfo = new GameInfo(gameLocation);
		this.gameLocation = gameLocation;
	}
	
	public void startEngine() {
		//Parse Window-Data and create DisplayManager
		String windowTitle = gameInfo.getValue("title");
		displayManager = new DisplayManager(windowTitle, this);
		displayManager.createDisplay();

		//Create Managers
		Graphics.init();
		new DefaultResources();
		//resourceMonitor = new ResourceMonitor();
		gameStateManager = new GameStateManager();
		tilesetManager = new TilesetManager();
		mapManager = new MapManager();

		camera = new Camera(DisplayManager.windowWidth, DisplayManager.windowHeight);
		mapManager.upateMapInformations();

		debugFont = new BitmapFont(new Font("Arial", Font.PLAIN, 12), true);

		onEnable();
		
		displayManager.loop();
		System.out.println("[INFO] Shutting down ...");
		onDisable();
		displayManager.destroy();
		System.out.println("Goodbye.");
	}
	
	@Override
	public void updateEngine(double delta) {
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		//resourceMonitor.update();

		tilesetManager.updateTileset();
		gameStateManager.update(displayManager.getTimer().getDelta());
		gameStateManager.draw(camera.getProjectionAndViewMatrix());

		Graphics.enableBlending(true);
		/*
		debugFont.drawText("GrapeEngine", 0, 0, camera.getProjectionAndViewMatrix());
		debugFont.drawText("Version 0.8", 0, 12, camera.getProjectionAndViewMatrix());
		debugFont.drawText("Using Lua Integration [BETA]", 0, 36, camera.getProjectionAndViewMatrix());
		debugFont.drawText("FPS: " + displayManager.getTimer().getFPS(), 0, 60, camera.getProjectionAndViewMatrix());
		debugFont.drawText("Uptime: " + (int) displayManager.getTimer().getTime() + " Sek.", 0, 72, camera.getProjectionAndViewMatrix());
		debugFont.drawText("OpenGL Resources", 0, 96, camera.getProjectionAndViewMatrix());
		debugFont.drawText("--------------------------------", 0, 102, camera.getProjectionAndViewMatrix());
		debugFont.drawText("Textures: " + MemoryManager.createdTextures.size(), 0, 120, camera.getProjectionAndViewMatrix());
		debugFont.drawText("VAOs:" + MemoryManager.createdVAOs.size(), 0, 132, camera.getProjectionAndViewMatrix());
		debugFont.drawText("VBOs: " + MemoryManager.createdVBOs.size(), 0, 144, camera.getProjectionAndViewMatrix());
		debugFont.drawText("DrawCalls: " + MemoryManager.drawCallsAmount, 0, 156, camera.getProjectionAndViewMatrix());
		*/
		MemoryManager.drawCallsAmount = 0;
		MemoryManager.verticesAmount = 0;

		Graphics.enableBlending(false);
	}
	
	public DisplayManager getDisplayManager() {
		return this.displayManager;
	}
	
	public GameStateManager getGameStateManager() {
		return this.gameStateManager;
	}
	
	public TilesetManager getTilesetManager() {
		return this.tilesetManager;
	}

	public MapManager getMapManager() {
		return this.mapManager;
	}

	public Camera getCamera() {
		return this.camera;
	}
	
	public GameInfo getGameInfo() {
		return this.gameInfo;
	}

	public String getGameLocation() {
		return this.gameLocation;
	}

	public static GrapeEngine getEngine() {
		return instance;
	}
}
