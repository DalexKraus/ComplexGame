package at.dalex.grape.gamestatemanager;

import at.dalex.grape.GrapeEngine;
import at.dalex.grape.entity.Entity;
import at.dalex.grape.graphics.*;
import at.dalex.grape.graphics.Graphics;
import at.dalex.grape.graphics.Image;
import at.dalex.grape.graphics.mesh.TexturedModel;
import at.dalex.grape.graphics.shader.HueShader;
import at.dalex.grape.input.Input;
import at.dalex.grape.resource.Assets;
import at.dalex.grape.toolbox.Toolbox;
import com.complex.HUD;
import com.complex.entity.Player;
import com.complex.entity.bullet.Bullet;
import com.complex.entity.bullet.LaserBullet;
import com.complex.manager.BulletManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.windows.DISPLAY_DEVICE;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class PlayState extends GameState {

	private Player player;
	private ArrayList<Entity> entities = new ArrayList<>();
	private HUD playerHud;

	private Camera camera;
	private Matrix4f projectionMatrix;

	private BulletManager bulletManager = new BulletManager();
	private FrameBufferObject backgroundFBO;
	private TexturedModel backgroundModel;
	private HueShader hueShader;

	private Image background;

	private float scrollPos = 0f;
	private ParallaxPlane plane1;
	private ParallaxPlane plane2;
	private ParallaxPlane plane3;
	private ParallaxPlane plane4;
	private ParallaxPlane plane5;

	private float angle;

	@Override
	public void init() {
		this.backgroundFBO = new FrameBufferObject();
		this.hueShader = new HueShader();
		this.background = Assets.get("sky.background", Image.class);
		this.backgroundModel = Graphics.getFrameBufferModel(backgroundFBO);

		this.plane1 = new ParallaxPlane(DisplayManager.windowWidth, 4069, 0.75f);
		this.plane2 = new ParallaxPlane(DisplayManager.windowWidth, 4069, 0.25f);
		this.plane3 = new ParallaxPlane(DisplayManager.windowWidth, 4069, 0.1f);
		this.plane4 = new ParallaxPlane(DisplayManager.windowWidth, 4069, 0.05f);
		this.plane5 = new ParallaxPlane(DisplayManager.windowWidth, 4069, 0.015f);

		Image stars = Assets.get("sky.stars", Image.class);
		Image far1 = Assets.get("sky.farplanet.1", Image.class);
		Image far2 = Assets.get("sky.farplanet.2", Image.class);
		Image bigPlanet = Assets.get("sky.bigplanet", Image.class);
		Image ringPlanet = Assets.get("sky.ringplanet", Image.class);

		placeComponentsOnPlane(plane5, stars, 540, 290, 50); 		// Faaar away
		placeComponentsOnPlane(plane4, far2, 24, 24, 20);  			// A little bit closer
		placeComponentsOnPlane(plane3, far1, 32, 32, 30);  			// A little more closer
		placeComponentsOnPlane(plane2, ringPlanet, 90, 215, 3);  	// Close
		placeComponentsOnPlane(plane1, bigPlanet, 512, 512, 5);   	// What the fuck

		plane1.bufferComponents();
		plane2.bufferComponents();
		plane3.bufferComponents();
		plane4.bufferComponents();
		plane5.bufferComponents();

		this.player = new Player(0, 0);
		entities.add(player);

		this.playerHud = new HUD(player);

		//Extract projection matrix
		this.camera = GrapeEngine.getEngine().getCamera();
		this.projectionMatrix = camera.getProjectionMatrix();
	}

	@Override
	public void draw(Matrix4f projectionAndViewMatrix) {
		int dW = DisplayManager.windowWidth;
		int dH = DisplayManager.windowHeight;

		Graphics.enableBlending(true);
		backgroundFBO.bindFrameBuffer();
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Graphics.enableBlending(true);
			Graphics.drawImage(background, 0, 0, dW, dH, backgroundFBO.getProjectionMatrix());
			plane5.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());
			plane4.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());
			plane3.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());
			plane2.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());
			plane1.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());

			entities.forEach(ent -> ent.draw(projectionAndViewMatrix));
			bulletManager.getBullets().forEach(bullet -> bullet.draw(projectionAndViewMatrix));
		}
		backgroundFBO.unbindFrameBuffer();

		Matrix4f transformation = Graphics.transformMatrix(projectionMatrix, 0, 0, dW, dH, 0f);
		hueShader.drawMesh(scrollPos / 4069f, 0.75f, backgroundModel, transformation);
//		hueShader.drawMesh(scrollPos / 4069f, 0.25f, backgroundModel, transformation);

		//Draw hud without view projection to remain static on screen
		playerHud.draw(projectionMatrix);
	}

	private boolean right = false;
	@Override
	public void update(double delta) {
		updateCamera(delta);
		scrollPos = -camera.getPosition().y;
		entities.forEach(ent -> ent.update(delta));
		bulletManager.getBullets().forEach(bullet -> bullet.update(delta));
		bulletManager.validateBullets();
		playerHud.update(delta);

		angle = (float) Math.toRadians(Input.mousePosition.y);

		if (Input.isButtonPressed(1)) {
			if (!right) {
				//Spawn bullet
				int xPos = (int) (player.getX());
				int yPos = (int) (player.getY());
				Bullet bullet = new LaserBullet(xPos, yPos, player.getPlayerRotation(), 4069);
				bulletManager.spawnBullet(bullet);
				player.applyDamage(5);
				right = true;
			}
		} else right = false;
	}

	/**
	 * Updates the position of the camera in space, always trying to keep the player in focus.
	 */
	private void updateCamera(double delta) {
		float dwH = DisplayManager.windowHeight / 2f;
		float dwW = DisplayManager.windowWidth  / 2f;

		/* Keep the camera relative to the player */
		float px = (float) (player.getX() - dwW);
		float py = (float) (player.getY() - dwH);
		Vector3f playerPosition = new Vector3f(px, py, 0f);

		//Calculate the vector from the camera pointing towards the player
		Vector3f cameraOffset = playerPosition.sub(camera.getPosition());
		//Move camera to target in one second
		cameraOffset.mul((float) delta);
		//Scale that time to two seconds
		cameraOffset.mul(2f);

		//Finally, translate the camera in that direction
		camera.translate(cameraOffset);

		/* Keep the camera's rotation relative to the player */
		float prevRot = angle;
		camera.setRotation(angle);

//		Toolbox.rotateProjectionMatrix(backgroundFBO.getProjectionMatrix(), -prevRot, dwW, dwH);
		Toolbox.rotateProjectionMatrix(backgroundFBO.getProjectionMatrix(), angle, dwW, dwH);
	}

	private void placeComponentsOnPlane(ParallaxPlane plane, Image compImage, int w, int h, int count) {
		Random rand = new Random();
		for (int i = 0; i < count; i++) {
			int x = rand.nextInt(plane1.getPlaneWidth());
			int y = rand.nextInt(plane1.getPlaneHeight() - 512) + 256;

			plane.getComponents().add(new ParallaxPlane.PlaneComponent(x, y, w, h, compImage));
		}
	}
}
