package at.dalex.grape.graphics;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL30.*;

public class FrameBufferObject {

	private int width;
	private int height;

	private int frameBufferID;
	private int renderBufferID;
	private int colorTextureID;

	/* Every framebuffer needs a different projection matrix, as the dimensions are not the same */
	private Matrix4f projectionMatrix;

	public FrameBufferObject(int width, int height) {
		this.width = width;
		this.height = height;
		this.projectionMatrix = new Matrix4f().setOrtho2D(0, width / 2f, height / 2f, 0);

		initFrameBuffer();
	}

	private void initFrameBuffer() {
		this.frameBufferID  = glGenFramebuffers();
		this.renderBufferID = createRenderBuffer();
		this.colorTextureID = createColorTexture();

		//Attach color texture and renderbuffer
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorTextureID, 0);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, renderBufferID);

		//Check framebuffer status
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
			System.err.println("Unable to create framebuffer!");

		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	private int createRenderBuffer() {
		int bufferID = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, bufferID);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
		return bufferID;
	}

	private int createColorTexture() {
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				(ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		return textureID;
	}

	public void bindFrameBuffer() {
		GL30.glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBufferID);
		GL11.glViewport(0, 0, width, height);
	}

	public void unbindFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, DisplayManager.windowWidth, DisplayManager.windowHeight);
	}

	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}

	public int getColorTextureID() {
		return this.colorTextureID;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}
}
