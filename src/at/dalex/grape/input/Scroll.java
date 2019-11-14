package at.dalex.grape.input;

import org.lwjgl.glfw.GLFWScrollCallback;

public class Scroll extends GLFWScrollCallback {

	private static double xOffset;
	private static double yOffset;
	
	@Override
	public void invoke(long windowHandle, double xoffset, double yoffset) {
		xOffset = xoffset;
		yOffset = yoffset;
	}
	
	public static double getXOffset() {
		return xOffset;
	}
	
	public static double getYOffset() {
		return yOffset;
	}
	
}
