package at.dalex.grape.input;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyListener extends GLFWKeyCallback implements Input {

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = (action != GLFW_RELEASE);
	}
	
	public boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
}
