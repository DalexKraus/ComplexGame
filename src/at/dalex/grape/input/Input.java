package at.dalex.grape.input;

import java.util.ArrayList;

import org.joml.Vector2f;

public interface Input {
	
	boolean[] keys = new boolean[65536];
	ArrayList<Integer> heldKeys = new ArrayList<>();
	Vector2f mousePosition = new Vector2f(0, 0);
	
	static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	static boolean isButtonPressed(int button) {
		return heldKeys.contains(button);
	}

	static Vector2f getMousePosition() {
		return mousePosition;
	}
}
