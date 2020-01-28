package com.complex.map;

import at.dalex.grape.script.XMLReader;
import org.jdom2.Element;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MapReader {

    public static void parseMapFile(String mapPath) throws FileNotFoundException {
        XMLReader xmlReader = new XMLReader(new FileInputStream(mapPath));

        Element rootElement = xmlReader.getRootElement();
        List<Element> children = rootElement.getChildren();
        List<Element> layers = new ArrayList<>();
        int width = 0;
        int height = 0;

        for (Element child : children) {
            if (child.getName().equals("layer")) {
                layers.add(child);
            }
        }
        String[] values = null;
        for (Element child : layers) {
            width = Integer.parseInt(child.getAttributeValue("width"));
            height = Integer.parseInt(child.getAttributeValue("height"));
            String content = child.getValue().trim();
            values = content.split(",");
            int[][] mapTiles = new int[width][height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    mapTiles[i][j] = Integer.parseInt(values[i * width + j]);
                }
            }
        }
    }
}
