package at.dalex.grape.graphics.shader;

import at.dalex.grape.GrapeEngine;
import at.dalex.grape.graphics.Texture;
import at.dalex.grape.graphics.Timer;
import at.dalex.grape.graphics.mesh.TexturedModel;
import at.dalex.grape.toolbox.MemoryManager;
import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class MotionBlurShader extends ShaderProgram {

    private int matrixLocation;
    private int sceneTexLocation;
    private int velTexLocation;
    private int velScaleLocation;

    public MotionBlurShader() {
        super("shaders/fx_motionblur/MotionBlurShader.vsh", "shaders/fx_motionblur/MotionBlurShader.fsh");
    }

    @Override
    public void getAllUniformLocations() {
        matrixLocation = getUniformLoader().getUniformLocation("projectionAndViewMatrix");
        sceneTexLocation = getUniformLoader().getUniformLocation("uTexInput");
        velTexLocation = getUniformLoader().getUniformLocation("uTexVelocity");
        velScaleLocation = getUniformLoader().getUniformLocation("velocityScale");
    }

    @Override
    public void bindAttributes() { }

    public void drawMesh(TexturedModel model, Texture velocityTexture, Matrix4f projectionAndViewMatrix) {
        start();
        model.getBaseModel().getVao().bindVAO();
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        getUniformLoader().loadMatrix(matrixLocation, projectionAndViewMatrix);

        Timer timer = GrapeEngine.getEngine().getDisplayManager().getTimer();
        float velScale = (1f / timer.getDelta()) / 60;
        getUniformLoader().loadFloat(velScaleLocation, velScale);

        //Set texture samplers to corresponding texture locations
        getUniformLoader().loadInt(sceneTexLocation, 0);
        getUniformLoader().loadInt(velTexLocation,   1);

        //Bind textures
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, model.getTextureId());
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, velocityTexture.getTextureId());

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
