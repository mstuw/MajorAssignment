package au.edu.federation.itech3104.michaelwilson;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;

import au.edu.federation.itech3104.michaelwilson.camera.Camera;
import au.edu.federation.itech3104.michaelwilson.graph.LightTrackingTransform;
import au.edu.federation.itech3104.michaelwilson.graph.Transform;
import au.edu.federation.itech3104.michaelwilson.graphics.IDisposable;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.IDrawableRenderer;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.IRenderer;

public abstract class Engine implements IDisposable, AutoCloseable {

	protected final long windowHandle;

	private final GLFWErrorCallback errorCallback;

	protected final ResourceManager resourceManager = new ResourceManager();

	protected final Transform root = new LightTrackingTransform(); // The scene graph root.

	protected final Camera camera;

	private final IRenderer renderer;

	public Engine(String title, int windowWidth, int windowHeight, Camera camera, IRenderer renderer) {
		this.camera = camera;
		this.renderer = renderer;
		this.renderer.setCamera(camera);

		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

		// Init glfw.
		System.out.println("Initialising GLFW...");
		if (glfwInit() != GL_TRUE)
			throw new IllegalStateException("Failed to initialise GLFW!");

		// Setup window hints.
		glfwDefaultWindowHints();

		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4); // OpenGL 4.3
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

		// Create window.
		System.out.println("Creating GLFW window...");
		windowHandle = glfwCreateWindow(windowWidth, windowHeight, title, NULL, NULL);
		if (windowHandle == NULL)
			throw new RuntimeException("Failed to create GLFW window!");

		// Center on screen.
		ByteBuffer videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(windowHandle, (GLFWvidmode.width(videoMode) - windowWidth) / 2, (GLFWvidmode.height(videoMode) - windowHeight) / 2);

		// Setup OpenGL context.
		glfwMakeContextCurrent(windowHandle);
		GLContext.createFromCurrent();

		// Setup the camera.
		camera.init(windowWidth, windowHeight);

		// Setup window resize callback for updating the viewport and projection matrix.
		glfwSetWindowSizeCallback(windowHandle, new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				glViewport(0, 0, width, height);
				camera.updateMatrices(width, height); // update the aspect ratio of the projection matrix
			}
		});

		glfwSwapInterval(1);
		glfwShowWindow(windowHandle);
	}

	protected abstract void initResources() throws IOException;

	protected abstract void initScene();

	/**
	 * Separate update method not associated with the scene graph. Called before the
	 * scene graph.
	 */
	protected void update(float deltaTime) {

	}

	/**
	 * Separate draw method not associated with the scene graph. Called before the
	 * scene graph.
	 */
	protected void draw(IDrawableRenderer renderer) {

	}

	public void run() throws IOException {
		resourceManager.dispose();
		
		initResources();
		initScene();

		glEnable(GL_DEPTH_TEST);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		// glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		double lastTime = glfwGetTime();

		while (glfwWindowShouldClose(windowHandle) == GL_FALSE) {

			// Calculate frame time.
			float deltaTime = (float) (glfwGetTime() - lastTime); // using float since we use Vec3f most often.
			lastTime = glfwGetTime();

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			update(deltaTime);
			draw(renderer);

			// root.updateAll(deltaTime);
			renderer.renderTree(root);

			glfwSwapBuffers(windowHandle);
			glfwPollEvents();
		}

	}

	@Override
	public void dispose() {
		System.out.println("Cleaning up...");

		errorCallback.release();

		Transform.dispose(root);

		resourceManager.dispose();

		glfwDestroyWindow(windowHandle);
		glfwTerminate();
	}

	// Used for try-with-resources statement
	@Override
	public void close() {
		dispose();
	}

}
