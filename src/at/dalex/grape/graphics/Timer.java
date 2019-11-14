package at.dalex.grape.graphics;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * This class is used to time the Main Game Loop.
 * It can also be used to determine FPS or UPS.
 * (Frames per second, updates per second)
 *
 * The Delta Time is also calculated here.
 */
public class Timer {

	/* Some sick private fields */
	private double lastLoopTime;
	private float deltaTime;
	private float timeCount;
	private int fps;
	private int fpsCount;
	private int ups;
	private int upsCount;

	/**
	 * Initializes the timer.
	 * Basically just sets the last loop time to
	 * the current time.
	 */
	public void init() {
		lastLoopTime = getTime();
	}

	/**
	 * @return The time the game is running
	 */
	public double getTime() {
		return glfwGetTime();
	}

	/**
	 * @return The time it took for the last frame to (update and) render.
	 */
	public float getDelta() {
		return this.deltaTime;
	}

	/**
	 * Increments the amount of frames
	 */
	public void updateFPS() {
		fpsCount++;
	}

	/**
	 * Increments the amount of updates
	 */
	public void updateUPS() {
		upsCount++;
	}

	/**
	 * Update the Timer, this will result in recalculation of the FPS & UPS
	 * and delta time
	 */
	public void update() {
		double time = getTime();
		deltaTime = (float) (time - lastLoopTime);
		lastLoopTime = time;
		timeCount += deltaTime;

		if (timeCount > 1f) {
			fps = fpsCount;
			fpsCount = 0;

			ups = upsCount;
			upsCount = 0;

			timeCount -= 1f;
		}
	}

	/**
	 * @return The amount of frames per second
	 */
	public int getFPS() {
		return fps > 0 ? fps : fpsCount;
	}

	/**
	 * @return The amount of updates per second
	 */
	public int getUPS() {
		return ups > 0 ? ups : upsCount;
	}

	/**
	 * @return The time of the last loop iteration
	 */
	public double getLastLoopTime() {
		return lastLoopTime;
	}
}
