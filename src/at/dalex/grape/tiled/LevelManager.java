package at.dalex.grape.tiled;

import java.io.InputStream;
import java.util.HashMap;

import org.json.simple.JSONObject;

import at.dalex.grape.resource.Assets;
import at.dalex.grape.script.JSONReader;

/**
 * Created by Da vid on 20.10.2017.
 */

public class LevelManager {

    private HashMap<String, LevelInfo> mapList = new HashMap<String, LevelInfo>();

    public LevelManager() {

        loadMapList();
    }

    public LevelInfo getLevelInfo(String levelName) {
        return mapList.get(levelName);
    }

    private void loadMapList() {
        JSONReader reader = new JSONReader(Assets.get("resource.maplist", InputStream.class));

        for (Object entry : reader.getRootElement().keySet()) {
            JSONObject mapEntry = ((JSONObject) reader.getRootElement().get(entry));

            LevelInfo levelInfo = new LevelInfo();
            levelInfo.levelName = (String) mapEntry.get("Name");
            levelInfo.levelPath = (String) mapEntry.get("Source");

            //Store LevelInfo
            mapList.put(levelInfo.levelName, levelInfo);
        }
    }

    public static class LevelInfo {
        public String levelName;
        public String levelPath;
    }
}
