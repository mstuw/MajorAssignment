package au.edu.federation.itech3104.michaelwilson;

import java.io.IOException;

import au.edu.federation.itech3104.michaelwilson.camera.CameraController;
import au.edu.federation.itech3104.michaelwilson.camera.PerspectiveCamera;
import au.edu.federation.itech3104.michaelwilson.graphics.Mesh;
import au.edu.federation.itech3104.michaelwilson.graphics.data.BufferUsageHint;
import au.edu.federation.itech3104.michaelwilson.graphics.data.VertexBufferLayout;
import au.edu.federation.itech3104.michaelwilson.graphics.material.StandardMaterial;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.BasicRenderer;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.IRenderer;
import au.edu.federation.itech3104.michaelwilson.lighting.DirectionalLight;
import au.edu.federation.itech3104.michaelwilson.math.Mat4f;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;
import au.edu.federation.itech3104.michaelwilson.model.loader.OBJLoader;
import au.edu.federation.itech3104.michaelwilson.model.loader.RawMesh;

public class Main extends Engine {

	private static final String WINDOW_TITLE = "Project";
	private static final int WINDOW_WIDTH = 900;
	private static final int WINDOW_HEIGHT = WINDOW_WIDTH / 16 * 10; // 16:10 aspect ratio

	private static final float NEAR_Z = 0.1f;
	private static final float FAR_Z = 100.0f;
	private static final float FOV = 90f;

	private final CameraController cameraController;

//	private final Vec3f[] cube_positions = { new Vec3f(0.0f, 0.0f, 0.0f), new Vec3f(2.0f, 5.0f, -15.0f), new Vec3f(-1.5f, -2.2f, -2.5f),
//			new Vec3f(-3.8f, -2.0f, -12.3f), new Vec3f(2.4f, -0.4f, -3.5f), new Vec3f(-1.7f, 3.0f, -7.5f), new Vec3f(1.3f, -2.0f, -2.5f),
//			new Vec3f(1.5f, 2.0f, -2.5f), new Vec3f(1.5f, 0.2f, -1.5f), new Vec3f(-1.3f, 1.0f, -1.5f) };

//	private final Vec3f[] light_positions = { new Vec3f(0.7f, 0.2f, 2.0f), new Vec3f(2.3f, -3.3f, -4.0f), new Vec3f(-4.0f, 2.0f, -12.0f),
//			new Vec3f(0.0f, 0.0f, -3.0f) };

	public Main() {
		super(WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, new PerspectiveCamera(FOV, NEAR_Z, FAR_Z), new BasicRenderer(null)); // Renderer camera will
																																// be set in super().
		cameraController = new CameraController(camera);

	}

	@Override
	protected void initResources() throws IOException {
		resourceManager.loadShader("standard", "./shaders/standard.vs", "./shaders/standard.fs");
		resourceManager.loadShader("light_source", "./shaders/light_cube.vs", "./shaders/light_cube.fs");

		resourceManager.loadTexture("container2", "./images/container2.png");
		resourceManager.loadTexture("container2_specular", "./images/container2_specular.png");

		StandardMaterial material = new StandardMaterial(resourceManager.getShader("standard"));
		material.setDiffuseTexture(resourceManager.getTexture("container2"));
		material.setSpecularTexture(resourceManager.getTexture("container2_specular"));
		material.setShininess(512.0f);
		resourceManager.addMaterial("standard", material); // Load material object into resource manager so we can access it elsewhere

		resourceManager.loadMaterial("light_source", "light_source");

		RawMesh rawMesh = OBJLoader.loadMesh("./models/TradeTent/TradeTent1_2.obj");
		Mesh mesh = rawMesh.toMesh(resourceManager.getMaterial("standard"), BufferUsageHint.STATIC_DRAW);
		resourceManager.addMesh("trade_tent", mesh);
	}

	@Override
	protected void initScene() {
		camera.getPosition().set(0, 0, 3);

		// Any object added to the scene graph will be automatically disposed when the
		// application closes.
		new DirectionalLight(new Vec3f(-0.2f, -1.0f, -0.3f), new Vec3f(0.5f), new Vec3f(0.4f), new Vec3f(0.5f)).setParent(root);
	}

	@Override
	protected void update(float deltaTime) {
		cameraController.update(windowHandle, deltaTime);

	}

	@Override
	protected void draw(IRenderer renderer) {
		renderer.draw(resourceManager.getMesh("trade_tent"), Mat4f.IDENTITY.scaleNew(0.01f, 0.01f, 0.01f));
	}

	public static void main(String[] args) throws Exception {

		try (Main main = new Main()) {

			main.run();

		}

	}

}
