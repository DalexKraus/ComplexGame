package at.dalex.grape.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseClickListener extends GLFWMouseButtonCallback {

	@Override
	public void invoke(long window, int button, int action, int mods) {
		System.out.println("Clicked '" + button + "' button action: " + action + " mods: " + mods);

		if (action == GLFW.GLFW_PRESS) {
			if (!Input.heldKeys.contains(button)) {
				Input.heldKeys.add(button);
			}
		}

		else if (action == GLFW.GLFW_RELEASE) {
			if (Input.heldKeys.contains(button)) {
				Input.heldKeys.remove(button);
			}
		}
	}
}
