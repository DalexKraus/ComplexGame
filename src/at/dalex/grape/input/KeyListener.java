package at.dalex.grape.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyListener extends GLFWKeyCallback implements Input {

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS)
			keys[key] = true;
		else if (action == GLFW_RELEASE)
			keys[key] = false;
	}
	
	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
}
