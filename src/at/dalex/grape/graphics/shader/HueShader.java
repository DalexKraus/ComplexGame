package at.dalex.grape.graphics.shader;

import at.dalex.grape.graphics.mesh.TexturedModel;
import at.dalex.grape.toolbox.MemoryManager;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class HueShader extends ShaderProgram {

    private int position_projectionMatrix;
    private int position_hueValue;
    private int position_saturationValue;

    public HueShader() {
        super("shaders/image/HueShader.vsh", "shaders/image/HueShader.fsh");
    }

    @Override
    public void getAllUniformLocations() {
        position_projectionMatrix   = getUniformLoader().getUniformLocation("projectionMatrix");
        position_hueValue           = getUniformLoader().getUniformLocation("hueValue");
        position_saturationValue    = getUniformLoader().getUniformLocation("saturationValue");
    }

    @Override
    public void bindAttributes() {

    }

    public void drawMesh(float hue, float saturation, TexturedModel model, Matrix4f projectionAndViewMatrix) {
        start();
        model.getBaseModel().getVao().bindVAO();
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        getUniformLoader().loadMatrix(position_projectionMatrix, projectionAndViewMatrix);
        getUniformLoader().loadFloat(position_hueValue, hue);
        getUniformLoader().loadFloat(position_saturationValue, saturation);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, model.getTextureId());
        glDrawElements(GL_TRIANGLES, model.getBaseModel().getVertexCount(), GL_UNSIGNED_INT, 0);

        MemoryManager.verticesAmount += model.getBaseModel().getVertexCount();
        MemoryManager.drawCallsAmount++;

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        model.getBaseModel().getVao().unbindVAO();
        stop();
    }
}
