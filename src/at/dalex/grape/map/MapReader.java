package at.dalex.grape.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import at.dalex.grape.developer.GameInfo;
import at.dalex.grape.info.Logger;
import org.jdom2.Element;

import at.dalex.grape.graphics.Tileset;
import at.dalex.grape.script.XMLReader;
import at.dalex.grape.toolbox.Type;

public class MapReader {

	public static Map parseMap(String pathToTmxFile, Tileset tileset) {
		Logger.info("[MapReader] Parsing map '" + pathToTmxFile + "'");
		Map map = null;
		XMLReader reader = null;
		try {
			reader = new XMLReader(new FileInputStream(new File(GameInfo.engine_location + "/" + pathToTmxFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (reader != null) {
			int width = Type.parseInt(reader.getRootElement().getAttributeValue("width"));
			int height = Type.parseInt(reader.getRootElement().getAttributeValue("height"));
			int layerAmount = reader.getRootElement().getChildren().size();
			map = new Map(width, height);
			
			List<Element> rootChildren = reader.getRootElement().getChildren();
			
			for (int z = 0; z < rootChildren.size(); z++) {
				if (rootChildren.get(z).getName().equals("layer")) {
					MapLayer layer = new MapLayer(width, height, tileset);
					List<Element> dataElement = rootChildren.get(z).getChildren();
					String map_layerContent = dataElement.get(0).getValue();

					String[] rows = map_layerContent.split("\n");
					for (int y = 0; y < rows.length; y++) {
						String[] cols = rows[y].split(",");
						for (int x = 0; x < cols.length; x++) {
							if (!cols[x].equals("") && !cols[x].equals(" ") && !cols[x].equals("  ")) {
								int tileId = Type.parseInt(cols[x]);
								layer.setTileAt(x, y - 1, tileId - 1, false, false);
							}
						}
					}
					map.addLayer(layer);
				}
			}
		}
		Logger.info("[MapReader] ... [DONE]");
		return map;
	}
}
