package at.dalex.grape.graphics.shader;

import at.dalex.grape.graphics.mesh.TexturedModel;
import at.dalex.grape.resource.FileContentReader;
import at.dalex.grape.toolbox.MemoryManager;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class VelocityShader extends ShaderProgram {

    private int currentMatrixLocation;
    private int previousMatrixLocation;

    public VelocityShader() {
        super(FileContentReader.readFile("shaders/VelocityShader.vsh"),
              FileContentReader.readFile("shaders/VelocityShader.fsh"));
    }

    @Override
    public void getAllUniformLocations() {
        currentMatrixLocation = getUniformLoader().getUniformLocation("currentModelViewProjectionMat");
        previousMatrixLocation = getUniformLoader().getUniformLocation("previousModelViewProjectionMat");
    }

    @Override
    public void bindAttributes() { }

    public void drawMesh(TexturedModel model, Matrix4f currentMatrix, Matrix4f previousMatrix) {
        start();
        model.getBaseModel().getVao().bindVAO();
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        getUniformLoader().loadMatrix(currentMatrixLocation, currentMatrix);
        getUniformLoader().loadMatrix(previousMatrixLocation, previousMatrix);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, model.getTextureId());
        glDrawElements(GL_TRIANGLES, model.getBaseModel().getVertexCount(), GL_UNSIGNED_INT, 0);
        glBindTexture(GL_TEXTURE_2D, 0);

        MemoryManager.verticesAmount += model.getBaseModel().getVertexCount();
        MemoryManager.drawCallsAmount++;

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        model.getBaseModel().getVao().unbindVAO();
        stop();
    }
}
