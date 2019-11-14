package at.dalex.grape.graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Logger;

import at.dalex.grape.graphics.graphicsutil.Image;
import at.dalex.grape.graphics.graphicsutil.ImageUtils;

/**
 * This class was written by dalex on 15.10.2017.
 * You are not permitted to edit this file.
 *
 * @author dalex
 */

public class Tileset {

    private BufferedImage rawImage;
    private Image tilesetImage;
    private Image[][] tileArray;
    private ArrayList<Image> tileList;
    private int tileSize;
    private int rows;
    private int cols;

    public Tileset(BufferedImage rawImage, int tileSize) {
        this.tileSize = tileSize;
        this.tilesetImage = ImageUtils.convertBufferedImage(rawImage);

        tileList = new ArrayList<>();
        Image[][] ret;
        try {
            //Convert source image to texture
            this.rawImage = rawImage;

            int width = rawImage.getWidth() / tileSize;
            int height = rawImage.getHeight() / tileSize;
            this.rows = height;
            this.cols = width;
            ret = new Image[height][width];
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    Image tile = ImageUtils.convertBufferedImage(rawImage.getSubimage(j * tileSize, i * tileSize, tileSize, tileSize));
                    ret[i][j] = tile;
                    tileList.add(tile);
                }
            }
            tileArray = ret;

            for (int i = 0; i < tileArray.length; i++) {
                for (int j = 0; j < tileArray[i].length; j++) {
                    tileList.add(tileArray[i][j]);
                }
            }
        }
        catch(Exception e) {
            Logger.getLogger("GrapeEngine").warning("Error loading spritesheet!");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public int getTileSize() {
        return this.tileSize;
    }

    public BufferedImage getRawImage() {
        return this.rawImage;
    }

    public Image getTilesetImage() {
        return this.tilesetImage;
    }

    public Image[][] getTileArray() {
        return this.tileArray;
    }

    public ArrayList<Image> getTileList() {
        return this.tileList;
    }

    public Image getRawTextureImage() {
        return ImageUtils.convertBufferedImage(rawImage);
    }

    public Image[][] getTileTextureArray() {
        return this.tileArray;
    }

    public int getNumberOfRows() {
        return this.rows;
    }

    public int getNumberOfColumns() {
        return this.cols;
    }
}

