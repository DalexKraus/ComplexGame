package at.dalex.grape.input;

import at.dalex.grape.graphics.DisplayManager;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseListener extends GLFWCursorPosCallback {
	
	@Override
	public void invoke(long window, double xPos, double yPos) {
		Input.mousePosition.x = (float) xPos * DisplayManager.mouseScaleX;
		Input.mousePosition.y = (float) yPos * DisplayManager.mouseScaleY;
	}
}
