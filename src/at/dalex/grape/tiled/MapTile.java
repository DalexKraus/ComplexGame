package at.dalex.grape.tiled;

/**
 * Created by Da vid on 20.10.2017.
 */

public class MapTile {

    private int id;
    private boolean breakable;
    private boolean isSolid;
    private boolean isEmitter;

    public MapTile(int id, boolean breakable, boolean isEmitter, boolean isSolid) {
        this.id = id;
        this.breakable = breakable;
        this.isEmitter = isEmitter;
        this.isSolid = isSolid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isBreakable() {
        return breakable;
    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public void setSolid(boolean solid) {
        isSolid = solid;
    }

    public boolean isEmitter() {
        return isEmitter;
    }

    public void setEmitter(boolean emitter) {
        isEmitter = emitter;
    }
}
