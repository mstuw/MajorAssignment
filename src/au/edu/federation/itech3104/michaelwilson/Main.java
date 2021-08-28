package au.edu.federation.itech3104.michaelwilson;

import java.io.IOException;

import au.edu.federation.itech3104.michaelwilson.camera.CameraController;
import au.edu.federation.itech3104.michaelwilson.camera.PerspectiveCamera;
import au.edu.federation.itech3104.michaelwilson.graphics.Mesh;
import au.edu.federation.itech3104.michaelwilson.graphics.data.BufferUsageHint;
import au.edu.federation.itech3104.michaelwilson.graphics.data.VertexBufferLayout;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;
import au.edu.federation.itech3104.michaelwilson.lighting.DirectionalLight;
import au.edu.federation.itech3104.michaelwilson.lighting.Light;
import au.edu.federation.itech3104.michaelwilson.lighting.PointLight;
import au.edu.federation.itech3104.michaelwilson.lighting.SpotLight;
import au.edu.federation.itech3104.michaelwilson.math.Mat4f;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;
import au.edu.federation.itech3104.michaelwilson.model.loader.OBJLoader;
import au.edu.federation.itech3104.michaelwilson.model.loader.RawMesh;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class Main extends Engine {

	private static final String WINDOW_TITLE = "Project";
	private static final int WINDOW_WIDTH = 900;
	private static final int WINDOW_HEIGHT = WINDOW_WIDTH / 16 * 10; // 16:10 aspect ratio

	private static final float NEAR_Z = 0.1f;
	private static final float FAR_Z = 100.0f;
	private static final float FOV = 90f;

	private final CameraController cameraController;

	private final Vec3f[] cube_positions = { new Vec3f(0.0f, 0.0f, 0.0f), new Vec3f(2.0f, 5.0f, -15.0f), new Vec3f(-1.5f, -2.2f, -2.5f),
			new Vec3f(-3.8f, -2.0f, -12.3f), new Vec3f(2.4f, -0.4f, -3.5f), new Vec3f(-1.7f, 3.0f, -7.5f), new Vec3f(1.3f, -2.0f, -2.5f),
			new Vec3f(1.5f, 2.0f, -2.5f), new Vec3f(1.5f, 0.2f, -1.5f), new Vec3f(-1.3f, 1.0f, -1.5f) };

	private final Vec3f[] light_positions = { new Vec3f(0.7f, 0.2f, 2.0f), new Vec3f(2.3f, -3.3f, -4.0f), new Vec3f(-4.0f, 2.0f, -12.0f),
			new Vec3f(0.0f, 0.0f, -3.0f) };

	Material material2;

	public Main() throws IOException {
		super(WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, new PerspectiveCamera(FOV, NEAR_Z, FAR_Z));

		cameraController = new CameraController(camera);

		resourceManager.loadShader("standard", "./shaders/standard.vs", "./shaders/standard.fs");
		resourceManager.loadShader("light_cube", "./shaders/light_cube.vs", "./shaders/light_cube.fs");

		resourceManager.loadTexture("container2", "./images/container2.png");
		resourceManager.loadTexture("container2_specular", "./images/container2_specular.png");

		Material material = resourceManager.loadMaterial("basic_mat", "standard");
		material.setLightingSupported(true);

		material.addTexture("material.diffuse", resourceManager.getTexture("container2"));
		material.addTexture("material.specular", resourceManager.getTexture("container2_specular"));
		material.addAttribute("material.shininess", 512.0f);

		material2 = resourceManager.loadMaterial("light_source", "light_cube");

		// @formatter:off
				
		// Create some cubes.
		for (Vec3f pos : cube_positions) {
			Mesh mesh = new Mesh(ShapeUtil.CUBE_VERTICES_NOT_INDEXED, 36, BufferUsageHint.STATIC_DRAW, VertexBufferLayout.Float3_3_2, material);
			mesh.localMatrix.translate(pos);
	
			mesh.setParent(root);	
			mesh.updateGlobalMatrix();
		}
		
		// Point Lights
		for (Vec3f pos : light_positions) {
		
			Light light = new PointLight(
					new Vec3f(0.05f, 0.05f, 0.05f),
					new Vec3f(0.8f, 0.8f, 0.8f), 
					new Vec3f(1.0f, 1.0f, 1.0f),
					1.0f, 
					0.09f,
					0.032f);
			
			light.localMatrix.translate(pos);
			light.setParent(root);
			light.updateGlobalMatrix();
			
			Mesh mesh = new Mesh(ShapeUtil.CUBE_VERTICES_NOT_INDEXED, 36, BufferUsageHint.STATIC_DRAW, VertexBufferLayout.Float3_3_2, material2);
			mesh.localMatrix.translate(pos);
			mesh.localMatrix = mesh.localMatrix.scale(0.25f, 0.25f, 0.25f);
			mesh.setParent(root);
			mesh.updateGlobalMatrix();
		}

	
		new DirectionalLight(
				new Vec3f(-0.2f, -1.0f, -0.3f),
				new Vec3f(0.05f, 0.05f, 0.05f), 
				new Vec3f(0.4f, 0.4f, 0.4f),
				new Vec3f(0.5f, 0.5f, 0.5f))
		.setParent(root);
		
	
		// @formatter:on

		camera.getPosition().set(0, 0, 3);

		// RawMesh rawModel = OBJLoader.loadMesh("./models/Crate/Crate1_2.obj");

		// Mesh mesh = new Mesh(rawModel.getVertices(), rawModel.getIndices(),
		// BufferUsageHint.STATIC_DRAW, VertexBufferLayout.Float3_3_2, material);
		// mesh.setParent(root);
	}

	private SpotLight spotLight;

	@Override
	protected void update(float deltaTime) {
		cameraController.update(windowHandle, deltaTime);
		// System.out.println(camera.getPosition());
		if (glfwGetKey(windowHandle, GLFW_KEY_N) == GLFW_PRESS) {
			Mesh mesh = new Mesh(ShapeUtil.CUBE_VERTICES_NOT_INDEXED, 36, BufferUsageHint.STATIC_DRAW, VertexBufferLayout.Float3_3_2, material2);
			mesh.localMatrix.translate(camera.getPosition());
			mesh.setParent(root);

			SpotLight spotLight = new SpotLight(camera.getFront().normalised(), new Vec3f(0.0f, 0.0f, 0.0f), new Vec3f(1.0f, 1.0f, 1.0f),
					new Vec3f(1.0f, 1.0f, 1.0f), 1.0f, 0.09f, 0.032f, (float) Math.cos(Math.toRadians(12.5f)),
					(float) Math.cos(Math.toRadians(15.0f)));

			spotLight.setParent(mesh);
			mesh.localMatrix = mesh.localMatrix.scale(0.25f, 0.25f, 0.25f);
			mesh.updateGlobalMatrix();
		}

		// spotLight.getDirection().set(camera.getFront());
		// spotLight.localMatrix.setOrigin(camera.getPosition());
	}

	public static void main(String[] args) throws Exception {

		try (Main main = new Main()) {

			main.run();

		} catch (Exception e) {
			System.err.println(e);
			throw e;
		}

	}

}
