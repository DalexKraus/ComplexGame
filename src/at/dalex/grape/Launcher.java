package at.dalex.grape;

import at.dalex.grape.gamestatemanager.GameState;
import at.dalex.grape.gamestatemanager.IntroState;
import at.dalex.grape.gamestatemanager.PlayState;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Launcher extends GrapeEngine {

	private static String gameLocation = ".";
	
	private GameState playState;
	
	public Launcher() {
		super(gameLocation);
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
			try {
				gameLocation = URLDecoder.decode(path, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				System.err.println("[BOOT] Could not decode game path. ('" + path + "')");
				e.printStackTrace();
			}
		}

		gameLocation = gameLocation.replace("\\", "/");
		System.out.println("[BOOT] Using game '" + gameLocation + "'");
		new Launcher();
	}
}
