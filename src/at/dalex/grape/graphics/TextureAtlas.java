package at.dalex.grape.graphics;

public class TextureAtlas extends Image {

    private int numberOfRows;
    private int numberOfColumns;
    private int[] cellSize;

    /**
     * Creates a new TextureAtlas instance.
     *
     * A Texture Atlas is an Image which contains
     * numberOfRows^2 individual images.
     *
     * (The source image has to be a power of two texture!)
     *
     * Contains the width, height and texture id of a OpenGL texture
     * and information about the atlas itself.
     *
     * @param textureId The OpenGL id of the atlas texture
     * @param atlasWidth The width of the atlas source image
     * @param numberOfRows The number of separate images contained in one row
     */
    public TextureAtlas(int textureId, int atlasWidth, int atlasHeight, int numberOfRows, int numberOfColumns) {
        super(textureId, atlasWidth, atlasHeight);

        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.cellSize = new int[] { atlasWidth / numberOfRows, atlasHeight / numberOfColumns };
    }

    public float[] recalculateUVCoordinates(float[] uvs, int cellId) {
        int yOffset = cellId / numberOfRows;
        int xOffset = cellId - (yOffset * numberOfRows);

        float normalizedCellWidth  = 1.0f / numberOfColumns;
        float normalizedCellHeight = 1.0f / numberOfRows;

        //Normalize all UVs
        for (int i = 0; i < uvs.length; i++) {
            if (i % 2 == 0) {
                uvs[i] /= numberOfColumns;
            }
            else {
                uvs[i] /=  numberOfRows;
            }
        }

        //Offset
        for (int i = 0; i < uvs.length; i++) {
            if (i % 2 == 0) {
                uvs[i] += xOffset * normalizedCellWidth;
            }
            else {
                uvs[i] += yOffset * normalizedCellHeight;
            }
        }

        return uvs;
    }

    public int getNumberOfRows() {
        return this.numberOfRows;
    }

    public int getNumberOfColumns() {
        return this.numberOfColumns;
    }

    public int[] getCellSize() {
        return this.cellSize;
    }
}
