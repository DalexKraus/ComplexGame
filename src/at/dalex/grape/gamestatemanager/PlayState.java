package at.dalex.grape.gamestatemanager;

import org.joml.Matrix4f;
import at.dalex.grape.graphics.graphicsutil.Graphics;

public class PlayState extends GameState {
	
	@Override
	public void init() {

	}

	@Override
	public void draw(Matrix4f projectionAndViewMatrix) {
		Graphics.enableBlending(true);


		Graphics.enableBlending(false);
	}

	@Override
	public void update(double delta) {

	}
}
