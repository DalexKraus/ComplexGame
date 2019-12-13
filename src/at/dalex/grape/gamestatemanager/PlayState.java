package at.dalex.grape.gamestatemanager;

import at.dalex.grape.entity.Entity;
import at.dalex.grape.graphics.*;
import at.dalex.grape.graphics.shader.MotionBlurShader;
import at.dalex.grape.graphics.shader.VelocityShader;
import at.dalex.grape.input.Input;
import at.dalex.grape.map.chunk.ChunkWorld;
import com.complex.entity.Player;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class PlayState extends GameState {

	private Player player;
	private ArrayList<Entity> entities = new ArrayList<>();

	private ChunkWorld chunkWorld;
	private ChunkWorldRenderer renderer;

	private Matrix4f previousMatrix;
	private FrameBufferObject velFBO;
	private FrameBufferObject sceneFBO;
	private VelocityShader velocityShader;
	private MotionBlurShader motionBlurShader;
	private Image playerImage;
	private double angle = 0D;

	@Override
	public void init() {
		player = new Player(512, 512);
		entities.add(player);

		this.chunkWorld = new ChunkWorld();
		chunkWorld.generateChunkAt(0, 0);

		BufferedImage atlas = ImageUtils.loadBufferedImage("textures/base.png");
		Tileset tileset = new Tileset(atlas, 16);
		this.renderer = new ChunkWorldRenderer(tileset, chunkWorld);

		this.playerImage = ImageUtils.loadImage(new File("textures/entity/player/player.png"));
		this.velFBO = new FrameBufferObject(1280, 720);
		this.sceneFBO = new FrameBufferObject(1280, 720);
		velocityShader = new VelocityShader();
		motionBlurShader = new MotionBlurShader();
	}

	@Override
	public void draw(Matrix4f projectionAndViewMatrix) {
		int xOff = 0;
		int yOff = 0;
		int x = (int) Input.getMousePosition().x - 64;
		int y = (int) Input.getMousePosition().y - 64;

		sceneFBO.bindFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		Graphics.enableBlending(true);
		Graphics.drawImage(playerImage, xOff + x, yOff + y, 128, 128, projectionAndViewMatrix);
		sceneFBO.unbindFrameBuffer();

		velFBO.bindFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		Matrix4f transform = Graphics.transformMatrix(projectionAndViewMatrix, xOff + x, yOff + y, 128, 128, 0f);
		if (previousMatrix == null) previousMatrix = transform;
		velocityShader.drawMesh(Graphics.getRectangleModel(playerImage.getTextureId()), transform, previousMatrix);
		previousMatrix = transform;
		velFBO.unbindFrameBuffer();

		Matrix4f fboTransform = Graphics.transformMatrix(projectionAndViewMatrix, 0, 0, 1280, 720, 0f);
		if (Input.isButtonPressed(0)) {
			motionBlurShader.drawMesh(Graphics.getRectangleModel(sceneFBO.getColorTextureID()), velFBO, fboTransform);
		}
		else Graphics.drawFrameBufferObject(sceneFBO, 0, 0, 1280, 720, projectionAndViewMatrix);
	}

	@Override
	public void update(double delta) {
		angle += 4D;
	}
}
