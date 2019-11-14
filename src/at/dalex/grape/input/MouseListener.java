package at.dalex.grape.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseListener extends GLFWCursorPosCallback {
	
	@Override
	public void invoke(long window, double xPos, double yPos) {
		Input.mousePosition.x = (float) xPos;
		Input.mousePosition.y = (float) yPos;
	}
}
