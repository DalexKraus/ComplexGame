package at.dalex.grape.tiled;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import at.dalex.grape.script.XMLReader;
import at.dalex.grape.toolbox.Type;

/**
 * Created by Da vid on 20.10.2017.
 */

public class MapParser {

    public static MapInfo parseMap(LevelManager.LevelInfo levelInfo) {

        ArrayList<MapLayer> layers = new ArrayList<MapLayer>();

        XMLReader reader = null;

        try {
            reader = new XMLReader(new FileInputStream(new File("resources/maplist.res")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (reader != null) {

            int width, height;

            width = Type.parseInt(reader.getRootElement().getAttributeValue("width"));
            height = Type.parseInt(reader.getRootElement().getAttributeValue("height"));

            //Loop through layers
            for (Element layerElement : reader.getRootElement().getChildren()) {
                if (layerElement.getName().equals("layer")) {

                    MapLayer layer = new MapLayer(width, height);
                    layer.setName(layerElement.getAttributeValue("name"));
                    List<Element> dataElement = layerElement.getChildren();
                    String map_layerContent = dataElement.get(0).getValue();

                    String[] first = map_layerContent.split("\n");
                    for (int j = 0; j < first.length; j++) {

                        String[] second = first[j].split(",");
                        
                        for (int k = 0; k < second.length; k++) {

                            if (!(second[k].equals(""))) {
                                if (!(second[k].equals(" "))) {
                                    if (!second[k].equals("  ")) {
                                        int id = 0;

                                        try {
                                            id = Integer.parseInt(second[k]);

                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }

                                        // Check collision
                                        //if (collTiles.getIds().contains(id)) {
                                        //    layer.setTileAt(k, j - 1, new MapTile(id, true, false, false));
                                        //}
                                        /*else*/ layer.setTileAt(k, j - 1, new MapTile(id, false, false, false));
                                    }
                                }
                            }
                        }
                    }

                    layers.add(layer);
                    System.out.println("Added new Layer: '" + layer.getName() + "'");
                }
            }
        }
        else {
        	System.err.println("Could not find MapFile for Level called '" + levelInfo.levelName + "'! Please check your 'mapinfo.res' config file!");
        }

        return new MapInfo(layers, null);
    }
}
