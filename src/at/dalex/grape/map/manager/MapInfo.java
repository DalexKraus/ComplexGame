package at.dalex.grape.map.manager;

//Package private class
class MapInfo {

    private String mapName;

    private String mapPath;
    private int tileSize;
    private String tilesetPath;

    public MapInfo(String mapName, String mapPath, int tileSize, String tilesetPath) {
        this.mapName = mapName;
        this.mapPath = mapPath;
        this.tileSize = tileSize;
        this.tilesetPath = tilesetPath;
    }

    public String getMapName() {
        return this.mapName;
    }

    public String getMapPath() {
        return this.mapPath;
    }

    public int getTileSize() {
        return this.tileSize;
    }

    public String getTilesetPath() {
        return this.tilesetPath;
    }
}
