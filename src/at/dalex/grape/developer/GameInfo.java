package at.dalex.grape.developer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.json.simple.JSONObject;
import at.dalex.grape.script.JSONReader;
import at.dalex.grape.toolbox.FileUtil;

/**
 * This class holds essential information about the game,
 * including resource locations and other options.
 *
 * Those informations are meant to be read before
 * game launch.
 *
 * @author David Kraus
 * @since 1.2
 */
public class GameInfo {

	private HashMap<String, String> options = new HashMap<>();
	private JSONReader reader;

	public static String engine_location;
	private final File DEFAULT_SEARCH_PATH;

	/**
	 * Parses the GameInfo file at the given location.
	 * @param gameLocation <b>Parent</b> folder of the GameInfo.txt (game location)
	 */
	public GameInfo(String gameLocation) {
		if (gameLocation == ".") {
			try {
				engine_location = new File(".").getCanonicalPath();
			} catch (IOException e1) {
				System.err.println("Could not locate engine in the file tree.");
				e1.printStackTrace();
				System.exit(-1);
			}
		}
		else engine_location = gameLocation;
		
		/* Construct default search path */
		DEFAULT_SEARCH_PATH = new File(engine_location + "/GameInfo.txt");

		if (FileUtil.isFile(DEFAULT_SEARCH_PATH)) {
			try {
				this.reader = new JSONReader(new FileInputStream(DEFAULT_SEARCH_PATH));
				parseConfig();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else {
			JOptionPane.showConfirmDialog(null, "Unable to locate GameInfo.txt", "Error",
					JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}

	/**
	 * Parses every element in the GameInfo.txt file and stores it for later use.
	 */
	private void parseConfig() {
		JSONObject info = (JSONObject) reader.getRootElement().get("GameInfo");
		for (Object value : info.keySet()) {
			options.put((String) value, (String) info.get(value));
		}
	}

	/**
	 * Get a value present in the GameInfo.txt file.
	 * @param key The key under which the value can be accessed.
	 */
	public String getValue(String key) {
		String value = null;
		if (options.containsKey(key)) {
			value = options.get(key);
		}
		return value;
	}
}
