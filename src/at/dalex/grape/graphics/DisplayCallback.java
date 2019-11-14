package at.dalex.grape.graphics;

/**
 * To interact with the Main Game Loop, a {@link DisplayCallback}
 * needs to be specified when creating a window using the {@link DisplayManager}
 */
public interface DisplayCallback {

	/**
	 * Callback: Called when a new iteration of the
	 * Main Game Loop happens
	 *
	 * @param delta The time it took for the last iteration to finish
	 */
	void updateEngine(double delta);
}
