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
import com.complex.entity.Enemy;
import com.complex.entity.Player;
import com.complex.entity.bullet.Bullet;
import com.complex.entity.bullet.LaserBullet;
import com.complex.manager.BulletManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class PlayState extends GameState {

	private Player player;
	private Enemy enemy;
	private ArrayList<Entity> entities = new ArrayList<>();
	private HUD playerHud;

	private Camera camera;
	private double cameraRotation;
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

	private FrameBufferObject worldBuffer;

	@Override
	public void init() {
		float w = DisplayManager.windowHeight;
		float h = DisplayManager.windowWidth;
		int screenDiag = (int) Math.sqrt(w * w + h * h);
		this.worldBuffer = new FrameBufferObject(screenDiag, screenDiag);

		this.backgroundFBO = new FrameBufferObject(worldBuffer.getWidth(), worldBuffer.getHeight());
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
		Image image = ImageUtils.loadImage(new File("textures/entity/player/player.png"));
		this.enemy = new Enemy(image, 0, 0);
		entities.add(player);
		entities.add(enemy);

		this.playerHud = new HUD(player);

		//Extract projection matrix
		this.camera = GrapeEngine.getEngine().getCamera();
		this.projectionMatrix = camera.getProjectionMatrix();
	}

	@Override
	public void draw(Matrix4f projectionAndViewMatrix) {
		worldBuffer.bindFrameBuffer();
		{
			Matrix4f worldViewMatrix = camera.getProjectionAndViewMatrix(worldBuffer.getProjectionMatrix());
			Graphics.enableBlending(true);
			backgroundFBO.bindFrameBuffer();
			{
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				Graphics.enableBlending(true);
				drawBackgroundImage(backgroundFBO.getProjectionMatrix());
				plane5.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());
				plane4.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());
				plane3.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());
				plane2.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());
				plane1.drawPlane(scrollPos, backgroundFBO.getProjectionMatrix());

				entities.forEach(ent -> ent.draw(worldViewMatrix));
				bulletManager.getBullets().forEach(bullet -> bullet.draw(worldViewMatrix));
			}
			backgroundFBO.unbindFrameBuffer();

//			Matrix4f transformation = Graphics.transformMatrix(worldBuffer.getProjectionMatrix(), 0, 0, dW, dH, 0f);
//			hueShader.drawMesh(scrollPos / 4069f, 0.75f, backgroundModel, transformation);
			Graphics.drawFrameBufferObject(backgroundFBO, 0, 0, backgroundFBO.getWidth(), backgroundFBO.getHeight(), worldBuffer.getProjectionMatrix());
		}
		worldBuffer.unbindFrameBuffer();

		int excessWidth = worldBuffer.getWidth() - DisplayManager.windowWidth;
		int excessHeight = worldBuffer.getHeight() - DisplayManager.windowHeight;
		int pivotX = DisplayManager.windowWidth / 2;
		int pivotY = DisplayManager.windowHeight / 2;
		Matrix4f rotationMatrix = new Matrix4f(projectionMatrix);
		Toolbox.rotateProjectionMatrix(rotationMatrix, (float) (cameraRotation + Math.PI), pivotX, pivotY);
		Graphics.drawImage(worldBuffer.getColorTextureID(), -excessWidth / 2, -excessHeight / 2, worldBuffer.getWidth(), worldBuffer.getHeight(), rotationMatrix);

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

		if (Input.isButtonPressed(1)) {
			if (!right) {
				//Spawn bullet
				int xPos = (int) (player.getX());
				int yPos = (int) (player.getY());
				Bullet bullet = new LaserBullet(xPos, yPos, player.getPlayerRotation(), 6144);
				bulletManager.spawnBullet(bullet);
				player.applyDamage(5);
				right = true;
			}
		} else right = false;
	}

	private void drawBackgroundImage(Matrix4f projectionMatrix) {
		float imageAspect = (float) background.getWidth() / (float) background.getHeight();
		int targetHeight  = worldBuffer.getHeight();
		int targetWidth   = (int) (targetHeight * imageAspect);
		int imageOffset   = -(targetWidth - worldBuffer.getWidth()) / 2;
		Graphics.drawImage(background, imageOffset, 0, targetWidth, targetHeight, projectionMatrix);
	}

	/**
	 * Updates the position of the camera in space, always trying to keep the player in focus.
	 */
	private void updateCamera(double delta) {
		float dwH = worldBuffer.getWidth() 	/ 2f;
		float dwW = worldBuffer.getHeight() / 2f;

		float px = (float) (player.getX() - dwW);
		float py = (float) (player.getY() - dwH);
		Vector3f playerPosition = new Vector3f(px, py, 0f);

		//Calculate the vector from the camera pointing towards the player
		Vector3f cameraOffset = playerPosition.sub(camera.getPosition());
		//Move camera to target in 166ms
		cameraOffset.mul((float) delta * 6);
		//Scale that time to two seconds
		cameraOffset.mul(2f);

		//Finally, translate the camera in that direction
		camera.translate(cameraOffset);

		/* Slowly rotate the camera to keep the player upright */
        double angleDifference = player.getPlayerRotation() - cameraRotation;
        angleDifference *= (delta * 4);
        this.cameraRotation += angleDifference;
	}

	private void placeComponentsOnPlane(ParallaxPlane plane, Image compImage, int w, int h, int count) {
		Random rand = new Random();
		for (int i = 0; i < count; i++) {
			int x = rand.nextInt(plane1.getPlaneWidth());
			int y = rand.nextInt(plane1.getPlaneHeight() - 512) + 256;

			plane.getComponents().add(new ParallaxPlane.PlaneComponent(x, y, w, h, compImage));
		}
	}

	public BulletManager getBulletManager() {
		return this.bulletManager;
	}

	public Player getPlayer() {
		return this.player;
	}
}
