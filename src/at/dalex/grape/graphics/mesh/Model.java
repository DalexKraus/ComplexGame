package at.dalex.grape.graphics.mesh;

import at.dalex.grape.graphics.VertexArrayObject;


public class Model {

    private ModelMeta modelMeta;
    private int vertexCount;

    private VertexArrayObject vao;
    private int textureCoordBufferId;  //Store texCoordVBOid so we can change them when using a texture atlas.

    /**
     * Creates a new Model object
     *
     */
    public Model(float[] vertices, int[] indices, int vertexDimension) {
        this(vertices, indices, null, vertexDimension); //Call other constructor for more performance (switching VAOs)
    }

    public Model(float[] vertices, int[] indices, float[] textureCoordinates, int vertexDimension) {
        this.modelMeta = new ModelMeta(vertices, indices);

        this.vertexCount = indices.length;

        //Create VertexArrayObject
        this.vao = new VertexArrayObject();
        vao.bindVAO();
        vao.bindIndicesBuffer(indices);
        vao.storeDataInAttributeList(0, 3, vertices, false);

        //Store texture coordinates if present
        if (textureCoordinates != null) {
            this.modelMeta.setTextureCoordinates(textureCoordinates);
            vao.storeDataInAttributeList(1, 2, textureCoordinates, true); //True for dynamic access
        }

        vao.unbindVAO();
    }

    public ModelMeta getModelMeta() {
        return this.modelMeta;
    }

    /**
     * Returns the {@link VertexArrayObject} ID of this <code>RawModel</code>
     */
    public VertexArrayObject getVao() {
        return this.vao;
    }

    /**
     * Returns the amount of vertices
     * @return Amount of vertices
     */
    public int getVertexCount() {
        return this.vertexCount;
    }
}
