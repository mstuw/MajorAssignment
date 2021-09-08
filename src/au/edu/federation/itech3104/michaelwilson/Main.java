package au.edu.federation.itech3104.michaelwilson;

import java.io.IOException;
import java.util.List;

import au.edu.federation.itech3104.michaelwilson.camera.CameraController;
import au.edu.federation.itech3104.michaelwilson.camera.PerspectiveCamera;
import au.edu.federation.itech3104.michaelwilson.graphics.Mesh;
import au.edu.federation.itech3104.michaelwilson.graphics.data.BufferUsageHint;
import au.edu.federation.itech3104.michaelwilson.graphics.data.VertexBufferLayout;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;
import au.edu.federation.itech3104.michaelwilson.graphics.material.StandardMaterial;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.BasicRenderer;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.IDrawableRenderer;
import au.edu.federation.itech3104.michaelwilson.graphics.texture.WrapMode;
import au.edu.federation.itech3104.michaelwilson.lighting.DirectionalLight;
import au.edu.federation.itech3104.michaelwilson.lighting.SpotLight;
import au.edu.federation.itech3104.michaelwilson.math.Mat4f;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;
import au.edu.federation.itech3104.michaelwilson.model.Model;
import au.edu.federation.itech3104.michaelwilson.model.ModelMesh;
import au.edu.federation.itech3104.michaelwilson.model.loader.OBJLoader;
import au.edu.federation.itech3104.michaelwilson.model.loader.RawModel;

public class Main extends Engine {

	private static final String WINDOW_TITLE = "Major Assignment";
	private static final int WINDOW_WIDTH = 900;
	private static final int WINDOW_HEIGHT = WINDOW_WIDTH / 16 * 10; // 16:10 aspect ratio

	private static final float NEAR_Z = 0.01f;
	private static final float FAR_Z = 100.0f;
	private static final float FOV = 90f;

	private static final float WINDMILL_BLADE_SPIN_SPEED = 50f;
	private static final float WINDMILL_ROTATE_SPEED = 10f;

	private final CameraController cameraController;

	private ModelMesh windmill;
	private ModelMesh windmill_body;
	private ModelMesh windmill_blades;

	private Mesh terrainMesh;
	private final Mat4f terrainModelMatrix = new Mat4f(1.0f);

	private SpotLight spotLight;

	public Main() {
		super(WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, new PerspectiveCamera(FOV, NEAR_Z, FAR_Z), new BasicRenderer(null));
		cameraController = new CameraController(camera);
	}

	@Override
	protected void initResources() throws IOException {
		// Shaders
		resourceManager.loadShader("standard", "./shaders/standard.vs", "./shaders/standard.fs");
		// resourceManager.loadShader("light_source", "./shaders/light_cube.vs",
		// "./shaders/light_cube.fs");

		// Textures (name -> filepath)
		resourceManager.loadTexture("container2", "./images/container2.png");
		resourceManager.loadTexture("container2_specular", "./images/container2_specular.png");
		resourceManager.loadTexture("grass", "./images/grass.png");

		// Windmill model textures
		resourceManager.loadTexture("windmill_diffuse", "./models/LowPolyWindmill/textures/Windmill_Windmill_MAT_BaseColor.png");

		// TradeTent model textures
		resourceManager.loadTexture("flag_diffuse", "./models/TradeTent/textures/Flag.png");
		resourceManager.loadTexture("metal_diffuse", "./models/TradeTent/textures/Metal.png");
		resourceManager.loadTexture("wood_diffuse", "./models/TradeTent/textures/Wood.png");
		resourceManager.loadTexture("wood1_diffuse", "./models/TradeTent/textures/Wood1.png");

		// Materials
		StandardMaterial matStandard = new StandardMaterial(resourceManager.getShader("standard"));
		matStandard.setDiffuseTexture(resourceManager.getTexture("container2"));
		matStandard.setSpecularTexture(resourceManager.getTexture("container2_specular"));
		matStandard.setShininess(64.0f);
		resourceManager.addMaterial("standard", matStandard); // Load material object into resource manager so we can access it elsewhere

		// Grass Material
		StandardMaterial matGrass = matStandard.copy();
		matGrass.setDiffuseTexture(resourceManager.getTexture("grass"));
		matGrass.setSpecularTexture(null);
		resourceManager.addMaterial("grass", matGrass); // name used by the .obj file.

		// Windmill Material
		StandardMaterial matWindmill = matStandard.copy();
		matWindmill.setDiffuseTexture(resourceManager.getTexture("windmill_diffuse"));
		resourceManager.addMaterial("Windmill_MAT", matWindmill); // name used by the .obj file.

		// TradeTent Materials
		StandardMaterial matFlag = matStandard.copy();
		matFlag.setDiffuseTexture(resourceManager.getTexture("flag_diffuse"));
		resourceManager.addMaterial("Flag", matFlag);

		StandardMaterial matWood = matStandard.copy();
		matWood.setDiffuseTexture(resourceManager.getTexture("wood_diffuse"));
		resourceManager.addMaterial("Wood", matWood);

		StandardMaterial matWood1 = matStandard.copy();
		matWood1.setDiffuseTexture(resourceManager.getTexture("wood1_diffuse"));
		resourceManager.addMaterial("Wood1", matWood1);

		StandardMaterial matMetal = matStandard.copy();
		matMetal.setDiffuseTexture(resourceManager.getTexture("metal_diffuse"));
		resourceManager.addMaterial("Metal", matMetal);

		// Load the trade tent model.
		RawModel rmTradeTent = OBJLoader.INSTANCE.loadModel("./models/TradeTent/TradeTent1-2.obj");

		Model tradeTent = new Model(rmTradeTent.toMeshes(resourceManager, matStandard, BufferUsageHint.STATIC_DRAW));
		tradeTent.localMatrix.translate(1.5f, -0.62f, 0.5f);
		tradeTent.localMatrix.scale(0.005f);
		tradeTent.updateGlobalMatrix();
		tradeTent.setParent(root);

		// Load the windmill model.
		RawModel rmWindmill = OBJLoader.INSTANCE.loadModel("./models/LowPolyWindmill/Windmill_Split.obj");
		List<Mesh> meshes = rmWindmill.toMeshes(resourceManager, matStandard, BufferUsageHint.STATIC_DRAW);

		// Any object added to the scene graph with the IDisposable interface will be
		// automatically disposed when the
		// application closes. See Engine#dispose() and the Node#dispose() method.

		// Build windmill tree structure, since OBJ files don't have structure
		// information.
		windmill = new ModelMesh(meshes.get(0));
		windmill_blades = new ModelMesh(meshes.get(1));
		windmill_body = new ModelMesh(meshes.get(2));

		windmill_blades.setParent(windmill_body);
		windmill_body.setParent(windmill);

		windmill.setParent(root); // the root node is from the superclass and is the base for the scene graph.

		// Create the terrain mesh and make sure the texture will tile on the plane. See
		// ShapeUtil#getPlaneVertices()
		terrainMesh = resourceManager.addMesh("terrain", ShapeUtil.getPlaneVertices(10, 10), ShapeUtil.PLANE_INDICES, BufferUsageHint.STATIC_DRAW,
				VertexBufferLayout.Float3_3_2, "grass");
	}

	@Override
	protected void initScene() {
		camera.getPosition().set(0, 0, 3);

		new DirectionalLight(new Vec3f(-0.2f, -1.0f, -0.3f), new Vec3f(0.5f), new Vec3f(0.4f), new Vec3f(0.9f)).setParent(root);

		// Move & scale the windmill object.
		windmill.localMatrix.scale(0.2f);
		windmill.localMatrix.translate(-3, -3, 10);
		windmill.updateGlobalMatrix();

		// Move and scale the terrain mesh.
		terrainModelMatrix.translate(0, -0.62f, 0).scale(10.0f);

		// Create a spot light.
		spotLight = new SpotLight(camera.getFront().normalised(), new Vec3f(0.0f), new Vec3f(0.5f), new Vec3f(0.2f), 1.0f, 0.09f, 0.032f,
				(float) Math.cos(Math.toRadians(12.5f)), (float) Math.cos(Math.toRadians(15.0f)));
		spotLight.setParent(root);

	}

	@Override
	protected void update(float deltaTime) {
		cameraController.update(windowHandle, deltaTime);

		// Rotate the blades.
		windmill_blades.localMatrix.translate(0, 6.59f, 0);
		windmill_blades.localMatrix.rotateAboutLocalAxisDegs(-WINDMILL_BLADE_SPIN_SPEED * deltaTime,
				windmill_blades.localMatrix.getXBasis().normalise());
		windmill_blades.localMatrix.translate(0, -6.59f, 0);

		// Rotate the windmill body/blades.
		windmill_body.localMatrix.rotateAboutLocalAxisDegs(WINDMILL_ROTATE_SPEED * deltaTime, new Vec3f(0, 1f, 0));
		windmill_body.updateGlobalMatrix();

		// Make the spot light both follow and look in the direction of the camera.
		spotLight.localMatrix.setOrigin(camera.getPosition());
		spotLight.getDirection().set(camera.getFront().normalised());
		spotLight.updateGlobalMatrix();
	}

	@Override
	protected void draw(IDrawableRenderer renderer) {
		super.draw(renderer);
		renderer.render(terrainMesh, terrainModelMatrix);
	}

	public static void main(String[] args) throws Exception {

		try (Main main = new Main()) {

			main.run();

		}

	}

}
