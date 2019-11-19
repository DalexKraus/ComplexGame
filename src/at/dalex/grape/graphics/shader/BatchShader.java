package at.dalex.grape.graphics.shader;

import at.dalex.grape.resource.FileContentReader;

public class BatchShader extends ShaderProgram {

    public int projectionMatrixLocation;

    public BatchShader() {
        super(FileContentReader.readFile("/shaders/BatchShader.vsh"), FileContentReader.readFile("/shaders/BatchShader.fsh"));
    }

    @Override
    public void getAllUniformLocations() {
        this.projectionMatrixLocation = getUniformLoader().getUniformLocation("transformationMatrix");
    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "vertex");
        super.bindAttribute(1, "viewMatrix");
        super.bindAttribute(5, "transformationMatrix");
        super.bindAttribute(9, "uvOffset");
        super.bindAttribute(10, "uvScale");
    }
}
