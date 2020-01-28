package at.dalex.grape.graphics;

import at.dalex.grape.toolbox.Toolbox;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * This class represents a camera in the scene
 * with which can be interacted (moved, rotated [WIP]).
 *
 * The main purpose of this camera is to provide
 * a projection matrix, which is vital for drawing.
 * When manipulating the position, changes are made to the view matrix.
 *
 * <b>PLEASE NOTE:</b>
 * Although we (might) only use a 2D perspective,
 * OpenGL handles everything as 3D, so we still need three-dimensional positions.
 *
 * @author David Kraus
 * @since 1.0
 */
//TODO: Update class to allow rotations
public class Camera {

	private Vector3f position;
	private float zRotation;
	private Matrix4f projectionMatrix;

	/**
	 * Creates a new camera bound to specific screen dimensions.
	 * The projection matrix will be created from those dimensions.
	 * @param width	The width of the screen (preferably)
	 * @param height The height of the screen (preferably)
	 */
	public Camera(int width, int height) {
		position = new Vector3f(0, 0, 0);
		projectionMatrix = new Matrix4f().setOrtho2D(0, width / 2f, height / 2f, 0);
	}

	/**
	 * Sets the position of the camera in the world.
	 * @param position New position for the camera.
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * Sets the position of the camera in the world.
	 * @param x x-coordinate of the camera
	 * @param y y-coordinate of the camera
	 * @param z z-coordiante of the camera
	 */
	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	/**
	 * Translates the camera using a given vector.
	 *
	 * The difference to {@link Camera#setPosition(Vector3f)}
	 * is that the position is only manipulated, not set entirely anew.
	 * Example: When the last position was (0, 2, 0) and you translate by the vector (1, 2, 0),
	 * 			the new position ends up at (1, 4, 0).
	 *
	 * @param position The translation vector.
	 */
	public void translate(Vector3f position) {
		this.position.add(position);
	}

	/**
	 * Translates the camera using a vector, represented in 3 float values.
	 * For further information see {@link Camera#translate(Vector3f)}
	 *
	 * @param tx x-translation
	 * @param ty y-translation
	 * @param tz z-translation
	 */
	public void translate(float tx, float ty, int tz) {
		this.position.x += tx;
		this.position.y += ty;
		this.position.z += tz;
	}

	/**
	 * @return The current position of the camera.
	 */
	public Vector3f getPosition() {
		return this.position;
	}

	/**
	 * Offsets the camera's rotation around the z-axis.
	 * @param dR The delta rotation in radians
	 */
	public void rotate(float dR) {
		this.zRotation += dR;
	}

	/**
	 * Sets the z-rotation of the camera
	 * @param r The rotation angle in radians
	 */
	public void setRotation(float r) {
		this.zRotation = r;
	}

	/**
	 * @return The z-rotation of the camera
	 */
	public float getRotation() {
		return this.zRotation;
	}

	/**
	 * This method creates a view matrix using the camera's position
	 * and combines (multiplies) it with the projection matrix.
	 *
	 * @eturn The generated view matrix combined with the projection matrix.
	 */
	public Matrix4f getProjectionAndViewMatrix() {
		Matrix4f transformed = new Matrix4f(projectionMatrix);
		float rot_translationX = 0.5f * DisplayManager.windowWidth  / 2;
		float rot_translationY = 0.5f * DisplayManager.windowHeight / 2;

		Toolbox.rotateProjectionMatrix(transformed, zRotation, rot_translationX, rot_translationY);

		transformed.translate(position.x * -0.5f, position.y * -0.5f, position.z * -0.5f);
		return transformed;
	}

	/**
 	 * @return The projection matrix of this camera.
	 */
	public Matrix4f getProjectionMatrix() {
		return new Matrix4f(projectionMatrix);
	}
}
