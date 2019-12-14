package at.dalex.grape.graphics;

import org.lwjgl.opengl.GL11;

/**
 * This class is used by OpenGL to store image data.
 * The class does not actually store any of the data,
 * it just holds an id associated with the image in the GPU's memory.
 *
 * For increased productivity, this class also holds the dimensions
 * of the original image.
 *
 * @author David Kraus
 * @since 1.0
 */
public class Image {

	protected int textureId;
	protected int glTarget = GL11.GL_TEXTURE_2D;
	private int width;
	private int height;

	/**
	 * Creates a new Image instance,
	 * Contains the width and height, but the texture id must still be created.
	 */
	public Image(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Creates a new Image instance. 
	 * Contains the width, height and texture id of a OpenGL texture
	 */
	public Image(int textureId, int width, int height) {
		this.textureId = textureId;
		this.width = width;
		this.height = height;
	}

	/**
	 * Bind this texture to the OpenGL context
	 */
	public void bind() {
		GL11.glBindTexture(glTarget, textureId);
	}

	/**
	 * Release this texture from the OpenGL context
	 */
	public void unbind() {
		GL11.glBindTexture(glTarget, 0);
	}

	/**
	 * Get the OpenGL texture id of this image
	 * @return OpenGL texture id
	 */
	public int getTextureId() {
		return this.textureId;
	}
	
	/**
	 * Get the width of this image
	 * @return width of this image
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * Get the height of this image
	 * @return height of this image
	 */
	public int getHeight() {
		return this.height;
	}
}
