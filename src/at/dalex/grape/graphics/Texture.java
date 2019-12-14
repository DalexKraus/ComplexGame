package at.dalex.grape.graphics;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL42.glBindImageTexture;

/**
 * A more superior version of {@link Image}
 *
 * @author Dalex
 */
public class Texture extends Image {

    private int depth;
    private boolean enableMipMap;
    private boolean enableAniso;
    private int pixelInternalFormat;
    private int pixelFormat;
    private int pixelDataType;
    private int minFilter;
    private int magFilter;
    private int wrapMode;

    private int maxMipmapLevel;
    private float maxAnisotropy;

    /**
     * Creates a new Texture instance.
     *
     * @param target The texture target type (GL_TEXTURE_1D, 2D, 3D or cubemap)
     * @param width The width of the texture
     * @param height The height of the texture
     * @param depth The depth of the texture
     * @param enableMipMap Enable mip-mapping?
     * @param enableAniso Enable anisotropic filtering?
     * @param pixelInternalFormat The internal format of each pixel
     * @param pixelFormat The format of each pixel
     * @param pixelDataType The data type of each pixel (GL_FLOAT, GL_UNSIGNED_INT, etc.)
     * @param minFilter The min-filter setting
     * @param magFilter The mag-filter setting
     * @param wrapMode The texture wrapping setting
     *
     */
    public Texture(int target, int width, int height, int depth, boolean enableMipMap, boolean enableAniso,
                   int pixelInternalFormat, int pixelFormat, int pixelDataType, int minFilter, int magFilter, int wrapMode) {
        super(width, height);

        //Apply values to all fields
        this.glTarget = target;
        this.depth = depth;
        this.enableMipMap = enableMipMap;
        this.enableAniso = enableAniso;
        this.pixelInternalFormat = pixelInternalFormat;
        this.pixelFormat = pixelFormat;
        this.pixelDataType = pixelDataType;
        this.minFilter = minFilter;
        this.magFilter = magFilter;
        this.wrapMode = wrapMode;

        if (enableMipMap) {
            this.maxMipmapLevel = getMaxMipMap(width, height);
            this.minFilter = GL_LINEAR;
        }

        if (enableAniso) {
            this.maxAnisotropy = glGetFloat(GL_TEXTURE_MAX_ANISOTROPY_EXT);
        }

        this.textureId = glGenTextures();
    }

    public void load(ByteBuffer imageData) {
        glBindTexture(glTarget, textureId);
        switch (glTarget) {
            case GL_TEXTURE_1D:
                glTexImage1D(glTarget, 0, pixelInternalFormat, getWidth(), 0, pixelFormat, pixelDataType, imageData);
                break;
            case GL_TEXTURE_2D:
                glTexImage2D(glTarget, 0, pixelInternalFormat, getWidth(), getHeight(), 0, pixelFormat, pixelDataType, imageData);
                break;
            case GL_TEXTURE_2D_ARRAY:
                glTexImage3D(glTarget, 0, pixelInternalFormat, getWidth(), getHeight(), depth, 0, pixelFormat, pixelDataType, 0);
                break;
            case GL_TEXTURE_CUBE_MAP:
                for (int face = 0; face < depth; face++) {
                    int faceTarget = GL_TEXTURE_CUBE_MAP_POSITIVE_X + face;
                    glTexImage2D(faceTarget, 0, pixelInternalFormat, getWidth(), getHeight(), 0, pixelFormat, pixelDataType, imageData);
                }
                break;
            default:
                throw new IllegalArgumentException("Could not load texture with unsupported texture target '" + glTarget + "'!");
        }

        setTextureParameters();
    }

    private void setTextureParameters() {
        glTexParameteri(glTarget, GL_TEXTURE_MIN_FILTER, minFilter);
        glTexParameteri(glTarget, GL_TEXTURE_MAG_FILTER, magFilter);
        glTexParameteri(glTarget, GL_TEXTURE_WRAP_R, wrapMode);
        glTexParameteri(glTarget, GL_TEXTURE_WRAP_S, wrapMode);
        glTexParameteri(glTarget, GL_TEXTURE_WRAP_T, wrapMode);

        if (enableMipMap) {
            glTexParameteri(glTarget, GL_TEXTURE_BASE_LEVEL, 0);
            glTexParameteri(glTarget, GL_TEXTURE_MAX_LEVEL, maxMipmapLevel);
        }

        if (enableAniso) {
            glTexParameterf(glTarget, GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropy);
        }
    }

    public void bindImageUnit(int texture_uniform, int index, int textureAccess, int level, int layer) {
        boolean isLayered = false;
        switch (glTarget)
        {
            case GL_TEXTURE_2D_ARRAY:
            case GL_TEXTURE_CUBE_MAP:
            case GL_TEXTURE_3D:
                isLayered = true;
                break;
        }

        glActiveTexture(GL_TEXTURE0 + index);
        glBindImageTexture(index, textureId, level, isLayered, layer, textureAccess, pixelInternalFormat);
        glUniform1i(texture_uniform, index);
    }

    private int getMaxMipMap(int width, int height) {
       int largestDimension = Math.max(width, height);
       return (int) (Math.log(largestDimension) / Math.log(2)) - 1;
    }
}
