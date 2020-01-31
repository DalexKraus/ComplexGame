package at.dalex.grape.graphics;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.*;
import java.nio.IntBuffer;

import at.dalex.grape.developer.GameInfo;
import at.dalex.grape.toolbox.Type;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import at.dalex.grape.GrapeEngine;
import at.dalex.grape.input.KeyListener;
import at.dalex.grape.input.MouseClickListener;
import at.dalex.grape.input.MouseListener;
import at.dalex.grape.input.Scroll;


/**
 * This class is used to manage the display (or the window).
 *
 * For productivity reasons, the width and height of the window
 * can be easily accessed using the following static fields:
 *  - {@link DisplayManager#windowWidth}
 *  - {@link DisplayManager#windowHeight}
 *
 */
public class DisplayManager {

	/* Static fields, so we can easily access the dimensions of the display later */
	public static int windowWidth = 720;
	public static int windowHeight = 480;

	public static float mouseScaleX;
	public static float mouseScaleY;

	private String windowTitle = "Grape Engine";
	private boolean useVerticalSync;

	private final int GL_VERSION_MAJOR = 4;
	private final int GL_VERSION_MINOR = 1;

	/* GLFW Window Handle */
	private long windowHandle;
	private DisplayCallback callback_reference;

	/* Timer instance, which is used to calculate delta time or count fps */
	private Timer timer;

	private DisplayCallback handler;

	/**
	 * Creates a new {@link DisplayManager} and parses the preferred
	 * width and height of the window as well as fullscreen mode
	 * from the GameInfo.txt file.
	 *
	 * Furthermore, the Main Game Loop is also present here.
	 * To interact with this loop, a {@link DisplayCallback}
	 * needs to be specified.
	 *
	 * @param title	The title of the window
	 * @param windowHandler A callback to interact with the Main Game Loop
	 */
	public DisplayManager(String title, DisplayCallback windowHandler) {
		this.windowTitle = title;
		this.handler = windowHandler;

		//Parse width and height which is set in GameInfo.txt
		GameInfo gameInfo = GrapeEngine.getEngine().getGameInfo();
		if (gameInfo.getValue("window_override").equals("true")) {
			windowWidth = Type.parseInt(gameInfo.getValue("window_width"));
			windowHeight = Type.parseInt(gameInfo.getValue("window_height"));
		}

		//Change resolution to display's, when fullscreen is set in GameInfo.txt
		if (GrapeEngine.getEngine().getGameInfo().getValue("fullscreen").equalsIgnoreCase("true")) {
			Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			windowWidth = (int) screenSize.getWidth();
			windowHeight = (int) screenSize.getHeight();
		}

		//Change VSync if set in GameInfo.txt
		useVerticalSync = Boolean.parseBoolean(GrapeEngine.getEngine().getGameInfo().getValue("use_vsync"));
		timer = new Timer(); //Create timer
	}

	/**
	 * Creates a new window and sets the default keyboard and mouse callbacks.
	 *
	 * @throws IllegalStateException When GLFW could not be initialized.
	 * @throws RuntimeException When the window could not be created.
	 */
	public void createDisplay() {
		System.out.println("========== [DisplayManager] ==========");
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		//GL Version selection
		System.out.printf("Target OpenGL version: %14s.%s\n", GL_VERSION_MAJOR, GL_VERSION_MINOR);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, GL_VERSION_MAJOR);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, GL_VERSION_MINOR);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

		//Create the window
		windowHandle = glfwCreateWindow(windowWidth, windowHeight, windowTitle + " Snapshot", NULL, NULL);
		if (windowHandle == NULL) {
			throw new RuntimeException("Failed to create the GLFW window.");
		}

		// Make the OpenGL context current
		glfwMakeContextCurrent(windowHandle);

		//Initialize OpenGL functions
		GL.createCapabilities();

		//Setup callbacks
		glfwSetKeyCallback(windowHandle, new KeyListener());
		glfwSetCursorPosCallback(windowHandle, new MouseListener());
		glfwSetMouseButtonCallback(windowHandle, new MouseClickListener());
		glfwSetScrollCallback(windowHandle, new Scroll());

		glfwSetWindowCloseCallback(windowHandle, new GLFWWindowCloseCallback() {
			@Override
			public void invoke(long arg0) {
				GrapeEngine.getEngine().onDisable();
				System.exit(0);
			}
		});

		//Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); //int*
			IntBuffer pHeight = stack.mallocInt(1); //int*
			glfwGetWindowSize(windowHandle, pWidth, pHeight);

			//Center the window
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(
					windowHandle,
					(vidMode.width() - pWidth.get(0)) / 2,
					(vidMode.height() - pHeight.get(0)) / 2
			);
		}

		// Set v-sync
		if (useVerticalSync)
			glfwSwapInterval(1);
		else glfwSwapInterval(0);

		// Make the window visible
		glfwShowWindow(windowHandle);
		//On high-resolution displays the actual render resolution will be
		//greater than the defined resolution in the GameInfo.txt
		int[] width = new int[1], height = new int[1];
		//Read the current size of the window in pixels
		glfwGetFramebufferSize(windowHandle, width, height);
		//As the resolution is scaled, the mouse position needs to be re-calculated as well
		mouseScaleX = width[0]  / (float) windowWidth;
		mouseScaleY = height[0] / (float) windowHeight;

		//Debug information
		System.out.printf("Target window size: %20s %-6s\n", windowWidth, windowHeight);
		System.out.printf("Actual window size: %20s %-6s\n", width[0], height[0]);
		System.out.printf("Mouse position scale value: %11s\n", mouseScaleX, mouseScaleY);

		//Replace the engine's constants with the actual resolution
		windowWidth = width[0];
		windowHeight = height[0];

		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	/**
	 * Starts the Main Game Loop.
	 * If not called before the {@link DisplayManager#destroy()}
	 * method, the window will close.
	 * */
	public void loop() {
		while (!glfwWindowShouldClose(windowHandle)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			timer.update();
			timer.updateUPS();
			timer.updateFPS();
			handler.updateEngine(timer.getDelta());

			// swap the color buffers
			glfwSwapBuffers(windowHandle);

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	/**
	 * Destroys the window and frees the default callbacks
	 */
	public void destroy() {
		//Free window callbacks and destroy the window
		glfwFreeCallbacks(windowHandle);
		glfwDestroyWindow(windowHandle);

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	/**
	 * Enables V-Sync
	 * <b>PLEASE NOTE:</b>
	 * This method needs to be called before the {@link DisplayManager#createDisplay()}
	 * method, otherwise it will not change! (Might be fixed in the future)
	 * @param useVerticalSync Use V-Sync?
	 */
	public void enableVsync(boolean useVerticalSync) {
		this.useVerticalSync = useVerticalSync;
	}

	/**
	 * Returns the {@link Timer} of this Display.
	 * The Timer is used for well.. (you guessed it) timing stuff.
	 *
	 * @return The {@link Timer} of this window
	 */
	public Timer getTimer() {
		return this.timer;
	}
}
