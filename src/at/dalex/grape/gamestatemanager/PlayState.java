package at.dalex.grape.gamestatemanager;

import at.dalex.grape.GrapeEngine;
import at.dalex.grape.entity.Entity;
import at.dalex.grape.graphics.*;
import at.dalex.grape.graphics.mesh.TexturedModel;
import at.dalex.grape.graphics.shader.HueShader;
import at.dalex.grape.input.Input;
import at.dalex.grape.resource.Assets;
import com.complex.HUD;
import com.complex.entity.Player;
import com.complex.entity.bullet.Bullet;
import com.complex.entity.bullet.LaserBullet;
import com.complex.manager.BulletManager;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class PlayState extends GameState {

	private Player player;
	private ArrayList<Entity> entities = new ArrayList<>();
	private HUD playerHud;

	private BulletManager bulletManager = new BulletManager();
	private FrameBufferObject backgroundFBO;
	private TexturedModel backgroundModel;
	private HueShader hueShader;

	private Image background;

	float scrollPos = 0f;
	private ParallaxPlane plane1;
	private ParallaxPlane plane2;
	private ParallaxPlane plane3;
	private ParallaxPlane plane4;
	private ParallaxPlane plane5;

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

		this.player = new Player(512, 512);
		entities.add(player);

		this.playerHud = new HUD(player);
	}

	@Override
	public void draw(Matrix4f projectionAndViewMatrix) {
		int dW = DisplayManager.windowWidth;
		int dH = DisplayManager.windowHeight;

		Graphics.enableBlending(true);
		backgroundFBO.bindFrameBuffer();
		{
			glClear(GL_COLOR_BUFFER_BIT);
			Graphics.enableBlending(true);
			Graphics.drawImage(background, 0, 0, dW, dH, backgroundFBO.getProjectionMatrix());
			plane5.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());
			plane4.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());
			plane3.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());
			plane2.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());
			plane1.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());
		}
		backgroundFBO.unbindFrameBuffer();

		entities.forEach(ent -> ent.draw(projectionAndViewMatrix));
		bulletManager.getBullets().forEach(bullet -> bullet.draw(projectionAndViewMatrix));

		Matrix4f transformation = Graphics.transformMatrix(projectionAndViewMatrix, 0, 0, dW, dH, 0f);
		//hueShader.drawMesh(scrollPos / 4069f, 0.75f, backgroundModel, transformation);

		Graphics.enableBlending(true);
		//hueShader.drawMesh(scrollPos / 4069f, 0.25f, backgroundModel, transformation);

		//Draw hud without view projection to remain static on screen
		Matrix4f projectionMatrix = GrapeEngine.getEngine().getCamera().getProjectionMatrix();
		playerHud.draw(projectionMatrix);
	}

	boolean r = false;
	boolean b = false;
	@Override
	public void update(double delta) {
		//if (Input.isButtonPressed(0))
		//	scrollPos  += delta * 1024;
		//else scrollPos += delta * 256;

		entities.forEach(ent -> ent.update(delta));

		bulletManager.getBullets().forEach(bullet -> bullet.update(delta));
		bulletManager.validateBullets();

		playerHud.update(delta);

		//shoot
		if (Input.isButtonPressed(0)) {
			//Spawn bullet
			int xPos = (int) (player.getX() + 4);
			int yPos = (int) (player.getY() + 6);
			Bullet bullet = new LaserBullet(xPos, yPos, player.getPlayerRotation(), 4069);
			bulletManager.spawnBullet(bullet);
		}

		if (Input.isButtonPressed(1)) {
			if (!r) {
				player.applyDamage(5);
				r = true;
			}
		} else r = false;
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
