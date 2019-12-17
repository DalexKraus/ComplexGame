package at.dalex.grape.graphics.shader;

import at.dalex.grape.graphics.DisplayManager;
import at.dalex.grape.graphics.Graphics;
import at.dalex.grape.graphics.Texture;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

public class VelocityDilateShader extends ShaderProgram {

    private int location_directionSelector;
    private int location_blurAmount;
    private int location_textureSize;

    private int location_sourceTexture;
    private int location_destTexture;

    private Texture velTex1;
    private Texture velTex2;

    public VelocityDilateShader() {
        super("/shaders/fx_motionblur/VelocityDilateShader.csh");

        /* Create necessary textures */
        this.velTex1 = new Texture
        (
                GL11.GL_TEXTURE_2D,
                1280, 720,
                0, false, false,
                GL_RG16F, GL_RG, GL_FLOAT,
                GL_LINEAR, GL_LINEAR, GL_CLAMP
        );
        velTex1.load(null);

        this.velTex2 = new Texture
        (
                GL_TEXTURE_2D,
                velTex1.getWidth(), velTex1.getHeight(),
                0, false, false,
                GL_RG16F, GL_RG, GL_FLOAT,
                GL_LINEAR, GL_LINEAR,GL_CLAMP
        );
        velTex2.load(null);
    }

    @Override
    public void getAllUniformLocations() {
        location_directionSelector  = getUniformLoader().getUniformLocation("direction_selector");
        location_blurAmount         = getUniformLoader().getUniformLocation("blur_amount");
        location_textureSize        = getUniformLoader().getUniformLocation("texture_size");

        location_sourceTexture      = getUniformLoader().getUniformLocation("sourceImage");
        location_destTexture        = getUniformLoader().getUniformLocation("destImage");
    }

    @Override
    public void bindAttributes() {
    }

    public Texture dilateVelocity(int velocityTextureId, int width, int height) {
        UniformUtil.UniformLoader loader = getUniformLoader();

        start();
        int blur_amount = 150;
        int fragmentation = 2;

        loader.loadInt(location_blurAmount, blur_amount);
        loader.load2DVector(location_textureSize, width, height);

        //Horizontal blur
        loader.loadInt(location_directionSelector, 0);
        loader.loadInt(location_sourceTexture, 0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, velocityTextureId);
        velTex1.bindImageUnit(location_destTexture, 1, GL_WRITE_ONLY, 0, 0);

        glDispatchCompute(velTex1.getWidth(), fragmentation, 1);
        glMemoryBarrier(GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);

        //Vertical blur
        loader.loadInt(location_directionSelector, 1);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, velTex1.getTextureId());
        velTex2.bindImageUnit(location_destTexture, 1, GL_WRITE_ONLY, 0, 0);

        glDispatchCompute(velTex1.getWidth(), fragmentation, 1);
        glMemoryBarrier(GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        stop();

        return velTex2;
    }
}
