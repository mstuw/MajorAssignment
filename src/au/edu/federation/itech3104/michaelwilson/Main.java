package au.edu.federation.itech3104.michaelwilson;

import java.io.IOException;
import java.util.List;

import au.edu.federation.itech3104.michaelwilson.camera.CameraController;
import au.edu.federation.itech3104.michaelwilson.camera.PerspectiveCamera;
import au.edu.federation.itech3104.michaelwilson.graphics.Mesh;
import au.edu.federation.itech3104.michaelwilson.graphics.data.BufferUsageHint;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;
import au.edu.federation.itech3104.michaelwilson.graphics.material.StandardMaterial;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.BasicRenderer;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.IDrawableRenderer;
import au.edu.federation.itech3104.michaelwilson.lighting.DirectionalLight;
import au.edu.federation.itech3104.michaelwilson.math.Mat4f;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;
import au.edu.federation.itech3104.michaelwilson.model.Model;
import au.edu.federation.itech3104.michaelwilson.model.ModelMesh;
import au.edu.federation.itech3104.michaelwilson.model.loader.OBJLoader;
import au.edu.federation.itech3104.michaelwilson.model.loader.RawModel;

public class Main extends Engine {

	private static final String WINDOW_TITLE = "Project";
	private static final int WINDOW_WIDTH = 900;
	private static final int WINDOW_HEIGHT = WINDOW_WIDTH / 16 * 10; // 16:10 aspect ratio

	private static final float NEAR_Z = 0.1f;
	private static final float FAR_Z = 100.0f;
	private static final float FOV = 90f;

	private final CameraController cameraController;

	private ModelMesh windmill;
	private ModelMesh windmill_body;
	private ModelMesh windmill_blades;

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
		// Shaders
		resourceManager.loadShader("standard", "./shaders/standard.vs", "./shaders/standard.fs");
		resourceManager.loadShader("light_source", "./shaders/light_cube.vs", "./shaders/light_cube.fs");

		// Textures
		resourceManager.loadTexture("container2", "./images/container2.png");
		resourceManager.loadTexture("container2_specular", "./images/container2_specular.png");

		resourceManager.loadTexture("windmill_diffuse", "./models/LowPolyWindmill/textures/Windmill_Windmill_MAT_BaseColor.png");

		// Materials
		StandardMaterial matStandard = new StandardMaterial(resourceManager.getShader("standard"));
		matStandard.setDiffuseTexture(resourceManager.getTexture("container2"));
		matStandard.setSpecularTexture(resourceManager.getTexture("container2_specular"));
		matStandard.setShininess(64.0f);
		resourceManager.addMaterial("standard", matStandard); // Load material object into resource manager so we can access it elsewhere

		// Windmill Material
		StandardMaterial matWindmill = new StandardMaterial(resourceManager.getShader("standard"));
		matWindmill.setDiffuseTexture(resourceManager.getTexture("windmill_diffuse"));
		resourceManager.addMaterial("Windmill_MAT", matWindmill); // name used by the .obj file.

		resourceManager.addMaterial("light_source", new Material(resourceManager.getShader("light_source")));

		// RawModel rawModel =
		// OBJLoader.INSTANCE.loadModel("./models/TradeTent/TradeTent1_2.obj");

		// Load the windmill model.
		RawModel rawModel = OBJLoader.INSTANCE.loadModel("./models/LowPolyWindmill/Windmill_Split.obj");
		List<Mesh> meshes = rawModel.toMeshes(resourceManager, matStandard, BufferUsageHint.STATIC_DRAW);

		// Any object added to the scene graph with the IDisposable interface will be
		// automatically disposed when the
		// application closes. See Engine#dispose() and the Node#dispose() method.

		// Build windmill tree structure.
		windmill = new ModelMesh(meshes.get(0));
		windmill_blades = new ModelMesh(meshes.get(1));
		windmill_body = new ModelMesh(meshes.get(2));

		windmill_blades.setParent(windmill_body);
		windmill_body.setParent(windmill);

		windmill.setParent(root); // the root node is from the superclass and is the base for the scene graph.
	}

	@Override
	protected void initScene() {
		camera.getPosition().set(0, 0, 3);

		new DirectionalLight(new Vec3f(-0.2f, -1.0f, -0.3f), new Vec3f(0.5f), new Vec3f(0.4f), new Vec3f(0.5f)).setParent(root);

		// Move & scale the windmill object.
		windmill.localMatrix.scale(0.2f);
		windmill.localMatrix.translate(-3, -3, 10);
		windmill.updateGlobalMatrix();
	}

	@Override
	protected void update(float deltaTime) {
		cameraController.update(windowHandle, deltaTime);

		// Rotate the blades.
		windmill_blades.localMatrix.translate(0, 6.59f, 0);
		windmill_blades.localMatrix.rotateAboutLocalAxisDegs(-50f * deltaTime, windmill_blades.localMatrix.getXBasis().normalise());
		windmill_blades.localMatrix.translate(0, -6.59f, 0);

		// Rotate the windmill body/blades.
		windmill_body.localMatrix.rotateAboutLocalAxisDegs(10 * deltaTime, new Vec3f(0, 1f, 0));
		windmill_body.updateGlobalMatrix();

	}

	@Override
	protected void draw(IDrawableRenderer renderer) {
		super.draw(renderer); 
	}

	public static void main(String[] args) throws Exception {

		try (Main main = new Main()) {

			main.run();

		}

	}

}
