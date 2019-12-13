package at.dalex.grape;

import at.dalex.grape.graphics.font.BitmapFont;
import at.dalex.grape.graphics.Graphics;
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

import static org.lwjgl.opengl.GL11.glGetError;

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

		debugFont = new BitmapFont(4, false);

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
		debugFont.drawQueuedText("GrapeEngine", 0, 0);
		debugFont.drawQueuedText("Version 0.8", 0, 14);
		debugFont.drawQueuedText("FPS: " + displayManager.getTimer().getFPS(), 0, 66);
		debugFont.drawQueuedText("Uptime: " + (int) displayManager.getTimer().getTime() + " Sek.", 0, 80);
		debugFont.drawQueuedText("OpenGL Resources", 0, 106);
		debugFont.drawQueuedText("----------------", 0, 114);
		debugFont.drawQueuedText("Textures: " + MemoryManager.createdTextures.size(), 0, 136);
		debugFont.drawQueuedText("VAOs:" + MemoryManager.createdVAOs.size(), 0, 150);
		debugFont.drawQueuedText("VBOs: " + MemoryManager.createdVBOs.size(), 0, 164);
		debugFont.drawQueuedText("DrawCalls: " + MemoryManager.drawCallsAmount, 0, 178);
		debugFont.drawQueue(camera.getProjectionMatrix());

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
