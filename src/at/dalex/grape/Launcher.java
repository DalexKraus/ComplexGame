package at.dalex.grape;

import at.dalex.grape.gamestatemanager.PlayState;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Launcher extends GrapeEngine {

	private static String gameLocation = ".";
	
	private PlayState playState;
	private static Launcher instance;
	
	public Launcher() {
		super(gameLocation);
		instance = this;
		System.out.println(" --- Starting engine ---");
		startEngine();
	}

	@Override
	public void onEnable() {
		playState = new PlayState();
		GrapeEngine.getEngine().getGameStateManager().addGameState(playState);
		GrapeEngine.getEngine().getGameStateManager().setState(playState.getId());
	}

	@Override
	public void onDisable() {
		
	}

	public static void main(String[] args) {

		if (args.length > 0 && args[0].equals("-game"))
			if (args[1] != null)
				gameLocation = args[1];

		if (gameLocation.equals(".")) {
			String path = Launcher.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			gameLocation = URLDecoder.decode(path, StandardCharsets.UTF_8);
		}

		gameLocation = gameLocation.replace("\\", "/");
		System.out.println("[BOOT] Using game '" + gameLocation + "'");
		new Launcher();
	}

	public PlayState getPlayState() {
		return playState;
	}

	public static Launcher getInstance() {
		return instance;
	}
}
