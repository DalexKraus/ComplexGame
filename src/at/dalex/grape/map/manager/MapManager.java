package at.dalex.grape.map.manager;

import at.dalex.grape.GrapeEngine;
import at.dalex.grape.toolbox.Dialog;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

public class MapManager {

    private final String MAPLIST_RES_LOCATION = "resources/maplist.res";
    private ArrayList<MapInfo> mapInfoArrayList = new ArrayList<>();

    public void upateMapInformations() {
        System.out.println("\n[MapManager] Updating map caches ...");
        try {
            String gameLocation = GrapeEngine.getEngine().getGameLocation();
            readMapInformations(new File(gameLocation + "/" + MAPLIST_RES_LOCATION));
        } catch (FileNotFoundException e) {
            Dialog.error("ERROR",  "The Path '" + MAPLIST_RES_LOCATION + "'\n" +
                    "does not lead to a valid maplist.res file.\n\n" +
                    "Map-Informations stored in this File will not be loaded!");
            System.out.println("[MapManager] An Error occurred while updating Map Caches.");
            e.printStackTrace();
        }
        System.out.println("[MapManager] Done updating Map Caches.");
    }

    public boolean isLoaded(String mapName) {
        for (MapInfo mapInfo : mapInfoArrayList) {
            if (mapInfo.getMapName().equals(mapName)) return true;
        }
        return false;
    }

    private void readMapInformations(File mapResourceInfo) throws FileNotFoundException {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject root = (JSONObject) jsonParser.parse(new InputStreamReader(new FileInputStream(mapResourceInfo)));
            for (Object rawMapName : root.keySet()) {
                JSONObject mapObject = (JSONObject) root.get(rawMapName);
                String mapName = (String) rawMapName;

                if (!isLoaded(mapName)) {
                    String mapPath = (String) mapObject.get("mapPath");
                    int tileSize = Integer.parseInt((String) mapObject.get("tileSize"));
                    String tilesetPath = (String) mapObject.get("tilesetPath");

                    mapInfoArrayList.add(new MapInfo(mapName, mapPath, tileSize, tilesetPath));
                    System.out.println("  --> Registered map '" + mapName + "'");
                }
                else System.err.println("[MapManager] Map '" + mapName + "' has already been loaded before!");
            }
        } catch (IOException | ParseException e) {
            System.err.println("Could not parse maplist.res file located at '" + mapResourceInfo.getPath() + "'.");
            e.printStackTrace();
        }
    }
}
