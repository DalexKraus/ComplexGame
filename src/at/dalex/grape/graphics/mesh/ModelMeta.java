package at.dalex.grape.graphics.mesh;

public class ModelMeta {

    private float[] vertices;
    private float[] textureCoordinates;
    private int[]   indices;

    public ModelMeta(float[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }

    public ModelMeta(float[] vertices, int[] indices, float[] textureCoordinates) {
        this(vertices, indices);
        this.textureCoordinates = textureCoordinates;
    }

    public float[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public float[] getTextureCoordinates() {
        return textureCoordinates;
    }

    public void setTextureCoordinates(float[] textureCoordinates) {
        this.textureCoordinates = textureCoordinates;
    }
}
