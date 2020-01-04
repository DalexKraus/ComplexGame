package at.dalex.grape.graphics;

import java.awt.Color;

import at.dalex.grape.graphics.mesh.Model;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import at.dalex.grape.graphics.mesh.TexturedModel;
import at.dalex.grape.graphics.shader.ImageShader;
import at.dalex.grape.graphics.shader.SolidColorShader;

import static org.lwjgl.opengl.GL11.glGetError;

/**
 * This class represents a graphics utility for drawing
 * forms, images and lines with only one method call.
 *
 * //TODO: Add line shader
 */
public class Graphics {

	private static SolidColorShader solidColorShader;
	private static ImageShader 		imageShader;

	/* Some vertices as well as indices for the basic shapes */
	private static float[] rectangleUvs = new float[] {
			0, 0,
			0, 1,
			1, 1,
			1, 0
	};

	/* A framebuffer's y values are inverted */
	private static float[] fboUVs = new float[] {
		0, 1,
		0, 0,
		1, 0,
		1, 1
	};
	
	private static float[] rectangleBaseVertices = new float[] {
		0, 0, 0,
		0, 1, 0,
		1, 1, 0,
		1, 0, 0
	};
	
	private static int[] rectangleBaseIndices = new int[] {
		0, 1, 2, 0, 2, 3
	};

	/* The rectangle model which is used for all rectangle drawing calls */
	private static Model defaultRectangleModel;

	/* A framebuffer's model, just a rectangle with inverted y coords for UVs */
	private static Model frameBufferModel;

	/**
	 * Initializes the graphics utility.
	 * This means loading and compiling shaders as well as
	 * creating the default models.
	 */
	public static void init() {
		solidColorShader 		= new SolidColorShader();
		imageShader 			= new ImageShader();
		defaultRectangleModel 	= new Model(rectangleBaseVertices, rectangleBaseIndices, rectangleUvs, 2);
		frameBufferModel 		= new Model(rectangleBaseVertices, rectangleBaseIndices, fboUVs, 2);
	}

	/**
	 * Draws an {@link Image} at the given X Y Coordinates.
	 *
	 * @param image The {@link Image} you want to be drawn
	 * @param x The target X-coordinate
	 * @param y The target Y-coordinate
	 * @param width The desired width
	 * @param height The desired height
 	 */
	public static void drawImage(Image image, int x, int y, int width, int height, Matrix4f projectionAndViewMatrix) {
		drawImage(image.getTextureId(), x, y, width, height, projectionAndViewMatrix);
	}

	/**
	 * Draws an {@link Image} at the given X Y Coordinates.
	 *
	 * @param image The {@link Image} you want to be drawn
	 * @param x The target X-coordinate
	 * @param y The target Y-coordinate
	 * @param width The desired width
	 * @param height The desired height
	 * @param rotZ The desired angle in degrees.
	 */
	public static void drawRotatedImage(Image image, int x, int y, int width, int height, float rotZ, Matrix4f projectionAndViewMatrix) {
		drawRotatedImage(image.getTextureId(), x, y, width, height, rotZ, projectionAndViewMatrix);
	}
	
	/**
	 * Draws an {@link Image} at the given X Y Coordinates.
	 *
	 * @param textureId The id of the texture you want to be drawn
	 * @param x The target X-coordinate
	 * @param y The target Y-coordinate
	 * @param width The desired width
	 * @param height The desired height
 	 */
	public static void drawImage(int textureId, int x, int y, int width, int height, Matrix4f projectionAndViewMatrix) {
		drawRotatedImage(textureId, x, y, width, height, 0f, projectionAndViewMatrix);
	}

	/**
	 * Draws an rotated {@link Image} at the given X Y Coordinates.
	 *
	 * @param textureId The id of the texture you want to be drawn
	 * @param x The target X-coordinate
	 * @param y The target Y-coordinate
	 * @param width The desired width
	 * @param height The desired height
	 * @param rotZ The desired angle in degrees
	 */
	public static void drawRotatedImage(int textureId, int x, int y, int width, int height, float rotZ, Matrix4f projectionAndViewMatrix) {
		Matrix4f matrices = transformMatrix(projectionAndViewMatrix, x, y, width, height, rotZ);
		imageShader.drawMesh(new TexturedModel(defaultRectangleModel, textureId), matrices);
	}

	public static void drawImageFromAtlas(TextureAtlas atlas, int imageId, int x, int y, int width, int height, Matrix4f projectionAndViewMatrix) {
		Matrix4f matrices = transformMatrix(projectionAndViewMatrix, x, y, width, height, 0f);
		TexturedModel model = new TexturedModel(defaultRectangleModel, 0);
		model.useTextureAtlas(true);
		model.setTextureAtlas(atlas);
		model.setActiveAtlasTextureIndex(imageId);

		imageShader.drawMesh(model, matrices);
	}

	/**
	 * Draws a {@link FrameBufferObject} at the given X Y coordinates.
	 *
	 * @param fbo The {@link FrameBufferObject} to draw
	 * @param x The target X-coordinate
	 * @param y The target Y-coordinate
	 * @param width The desired width
	 * @param height The desired height
	 */
	public static void drawFrameBufferObject(FrameBufferObject fbo, int x, int y, int width, int height, Matrix4f projectionAndViewMatrix) {
		Matrix4f matrices = transformMatrix(projectionAndViewMatrix, x, y, width, height, 0f);
		imageShader.drawMesh(new TexturedModel(frameBufferModel, fbo.getColorTextureID()), matrices);
	}

	/**
	 * Draws a filled rectangle at the given X Y Coordinates.
	 *
	 * @param x The target X-coordinate
	 * @param y The target Y-coordinate
	 * @param width The desired width
	 * @param height The desired height
	 * @param color The color you want the rectangle to have
	 */
	public static void fillRectangle(int x, int y, int width, int height, Color color, Matrix4f projectionAndViewMatrix) {
		Matrix4f matrices = transformMatrix(projectionAndViewMatrix, x, y, width, height, 0f);
		solidColorShader.drawMesh(defaultRectangleModel, matrices, color);
	}
	
	public static void enableBlending(boolean blending) {
		if (blending) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		} else GL11.glDisable(GL11.GL_BLEND);
	}

	public static TexturedModel getRectangleModel(int textureId) {
		return new TexturedModel(defaultRectangleModel, textureId);
	}

	public static TexturedModel getFrameBufferModel(FrameBufferObject fbo) {
		return new TexturedModel(frameBufferModel, fbo.getColorTextureID());
	}

	public static Matrix4f transformMatrix(Matrix4f projectionAndViewMatrix, int x, int y, int width, int height, float rZ) {
		float scaleX = width / 2f;
		float scaleY = height / 2f;

		Matrix4f transformationMatrix = new Matrix4f();
		transformationMatrix.translate(x / 2f, y / 2f, 0);

		transformationMatrix.translate(scaleX / 2f, scaleY / 2, 0f);
		transformationMatrix.rotateZ((float) Math.toRadians(rZ));
		transformationMatrix.translate(-scaleX / 2, -scaleY / 2f, 0.0f);

		transformationMatrix.scale(scaleX, scaleY, 1.0f);

		Matrix4f transformedMatrix = new Matrix4f(projectionAndViewMatrix);
		transformedMatrix.mul(transformationMatrix);
		return transformedMatrix;
	}	
}
