package at.dalex.grape.gamestatemanager;

import java.awt.Color;
import java.io.File;

import org.joml.Matrix4f;

import at.dalex.grape.GrapeEngine;
import at.dalex.grape.graphics.DisplayManager;
import at.dalex.grape.graphics.graphicsutil.Graphics;
import at.dalex.grape.graphics.graphicsutil.Image;
import at.dalex.grape.graphics.graphicsutil.ImageUtils;

public class IntroState extends GameState {

	Image engineLogo;

	private int alpha = 255;
	private boolean fadein = true;
	private boolean fadeout = false;
	private boolean waiting = false;
	private int intro_sleep = 30;
	private int wait_ticks;
	private int wait_time = 30;

	@Override
	public void init() {
		engineLogo = ImageUtils.loadImage(new File("textures/engine_logo.png"));
	}

	@Override
	public void draw(Matrix4f projectionAndViewMatrix) {
		Graphics.fillRectangle(0, 0, DisplayManager.windowWidth, DisplayManager.windowHeight, new Color(255, 255, 255), projectionAndViewMatrix);
		Graphics.enableBlending(true);
		int x = (DisplayManager.windowWidth / 2) - (engineLogo.getWidth() / 2);
		int y = (DisplayManager.windowHeight / 2) - (engineLogo.getHeight() / 2);
		Graphics.drawImage(engineLogo, x, y, engineLogo.getWidth(), engineLogo.getHeight(), projectionAndViewMatrix);
		Graphics.fillRectangle(0, 0, DisplayManager.windowWidth, DisplayManager.windowHeight, new Color(0, 0, 0, alpha), projectionAndViewMatrix);
		Graphics.enableBlending(false);
	}

	@Override
	public void update(double delta) {

		if (intro_sleep > 0) {
			intro_sleep--;
		}
		else {
			if (fadein) {
				if (alpha >= 17) {
					alpha -= 17;
				}
				else {
					fadein = false;
					waiting = true;
				}
			}
			if (waiting) {
				wait_ticks++;
				if (wait_ticks >= wait_time) {
					waiting = false;
					fadeout = true;
				}
			}
			else if (fadeout) {
				if (alpha <= 252) {
					alpha += 3;
				}
				else {
					GameStateManager gst = GrapeEngine.getEngine().getGameStateManager();
					PlayState playState = new PlayState();
					gst.addGameState(playState);
					gst.setState(playState.getId());
				}
			}
		}
	}
}
