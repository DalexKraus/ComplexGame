package at.dalex.grape.tiled;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import at.dalex.grape.graphics.DisplayManager;
import at.dalex.grape.graphics.Graphics;

/**
 * Created by Da vid on 20.10.2017.
 */
public class TileMap {

    private int x = 0;
    private int y = 0;

    private ArrayList<MapLayer> layers = new ArrayList<>();
//    private ArrayList<AbstractEntity> entities = new ArrayList<>();
    private ArrayList<Rectangle> collision = new ArrayList<>();
    private MapInfo mapInfo;
    private int tileSize;
    private int width, height;
    private int widthPixels, heightPixels;
    private boolean isPrepared = false;
    boolean debug = false;
    private MapLayer collisionLayer;

    // bounds
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;

    public TileMap(MapInfo mapInfo, int tileSize) {
        this.mapInfo = mapInfo;
        this.tileSize = tileSize;

        width = mapInfo.getLayers().get(0).getWidth();
        height = mapInfo.getLayers().get(0).getHeight();
        widthPixels = width * tileSize;
        heightPixels = height * tileSize;

        this.layers = mapInfo.getLayers();
        convertCollisionLayer();
        int screenWidth = (int) DisplayManager.windowWidth;
        int screenHeight = (int) DisplayManager.windowHeight;
        xmin = screenWidth - width;
        ymin = screenHeight - height;
        xmax = 0;
        ymax = 0;
    }

    private void convertCollisionLayer() {
        for (MapLayer layer : mapInfo.getLayers()) {
            if (layer.getName().equals("collision")) {
                collisionLayer = layer;
                for (int y = 0; y < layer.getHeight(); y++) {
                    for (int x = 0; x < layer.getWidth(); x++) {
                        if (layer.getTileAt(x, y).getId() == MapConstants.COLLISION_TILE_ID) {
                            collision.add(new Rectangle(x * tileSize, y * tileSize, tileSize, tileSize));
                        }
                    }
                }
                //Remove visible collision layer from stack of layers
                layers.remove(layer);
            }
        }
    }

    public MapInfo getMapInfo() {
        return this.mapInfo;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidthPixels() {
        return widthPixels;
    }

    public int getHeightPixels() {
        return heightPixels;
    }

//    public ArrayList<AbstractEntity> getEntities() {
//        return entities;
//    }
//
//    public void addEntity(AbstractEntity entity) {
//        entities.add(entity);
//    }
//
//    public void removeEntity(AbstractEntity entity) {
//        entities.remove(entity);
//    }
//
//    public Lightmap getLightmap() {
//        return this.lightmap;
//    }

    public MapLayer getCollisionLayer() {
        return this.collisionLayer;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean isColliding(Rectangle collisionBox) {
        for (Rectangle collisionTile : collision) {
            if (collisionTile.intersects(collisionBox)) {
                return true;
            }
        }
        return false;
    }

    public Rectangle getCollidingRectangle(Rectangle collisionBox) {
        for (Rectangle collisionTile : collision) {
            if (collisionTile.intersects(collisionBox)) {
                return collisionTile;
            }
        }
        return null;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void translate(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void fixBounds() {
        if (x < xmin) x = xmin;
        if (y < ymin) y = ymin;
        if (x > xmax) x = xmax;
        if (y > ymax) y = ymax;
    }

    public void update(double delta) {
//        for (AbstractEntity entity : entities) {
//            entity.update(delta);
//        }
    }

    public void draw(float[] matrices, int x, int y, Matrix4f projectionAndViewMatrix) {
    	GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        for (MapLayer layer : layers) {
            layer.draw(0, 0, projectionAndViewMatrix);
        }
        if (debug) {
            for (Rectangle collisionTile : collision) {
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                Graphics.fillRectangle((int) collisionTile.getX(), (int) collisionTile.getY(), (int) collisionTile.getWidth(), (int) collisionTile.getHeight(), Color.RED, projectionAndViewMatrix);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }
        GL11.glDisable(GL11.GL_BLEND);
    }
}
