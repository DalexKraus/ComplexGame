package at.dalex.grape.graphics.font;

public class Glyph {

    public final int width;
    public final int height;
    public final int x, y;
    public final float advance;

    public Glyph(int x, int y, int width, int height, float advance) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.advance = advance;
    }
}
