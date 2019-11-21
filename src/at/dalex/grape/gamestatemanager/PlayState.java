package at.dalex.grape.gamestatemanager;

import at.dalex.grape.graphics.graphicsutil.Image;
import at.dalex.grape.graphics.graphicsutil.ImageUtils;
import org.joml.Matrix4f;
import at.dalex.grape.graphics.graphicsutil.Graphics;

import java.io.File;

public class PlayState extends GameState {

	private Image playerImage;
	private float angle = 0.0f;

	@Override
	public void init() {

		this.playerImage = ImageUtils.loadImage(new File("textures/entity/player/player.png"));

	}

	@Override
	public void draw(Matrix4f projectionAndViewMatrix) {
		Graphics.enableBlending(true);

		projectionAndViewMatrix.translate(-playerImage.getWidth() / 2.0f, -playerImage.getHeight() / 2.0f, 0.0f);
		projectionAndViewMatrix.rotate(angle, 0.0f, 0.0f, 1.0f);
		projectionAndViewMatrix.translate(playerImage.getWidth() / 2.0f, playerImage.getHeight() / 2.0f, 0.0f);

		Graphics.drawImage(playerImage, 0, 0, 512, 512, projectionAndViewMatrix);
		Graphics.enableBlending(false);
	}

	@Override
	public void update(double delta) {
		angle += 2f * delta;
	}
}
