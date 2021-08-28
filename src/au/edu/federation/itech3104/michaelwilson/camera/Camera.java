package au.edu.federation.itech3104.michaelwilson.camera;

import au.edu.federation.itech3104.michaelwilson.math.Mat4f;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;

/**
 * Represents an abstract camera. Provides access to both the projection and
 * view matrix objects.
 */
public abstract class Camera {
	public static final Vec3f UP = new Vec3f(0f, 1f, 0f);

	private static final float DEGS_TO_RADS = (float) Math.PI / 180.0f;

	private Mat4f projectionMatrix;

	private final Vec3f position = new Vec3f();
	
	private final Vec3f front = new Vec3f(0, 0, -1f);
	private Vec3f up;
	private Vec3f right;

	public Camera() {

	}

	/**
	 * Setup the camera, should be called at least once before camera can be used.
	 * 
	 * @param width  width of the window, used for aspect ratio
	 * @param height height of the window, used for aspect ratio
	 */
	public void init(int width, int height) {
		updateMatrices(width, height);
		update();
	}

	/**
	 * Update camera vectors. Should be called whenever the front vector is
	 * modified.
	 * 
	 * @see {@link #getFront()}
	 */
	public void update() {
		front.normalise();
		right = front.cross(UP).normalise();
		up = right.cross(front).normalise();
	}

	/**
	 * Make this camera look in the direction of the given pitch & yaw values.
	 * 
	 * @param pitch degrees
	 * @param yaw   degrees
	 */
	public void lookAt(float pitch, float yaw) {
		front.x = (float) Math.cos(yaw * DEGS_TO_RADS) * (float) Math.cos(pitch * DEGS_TO_RADS);
		front.y = (float) Math.sin(pitch * DEGS_TO_RADS);
		front.z = (float) Math.sin(yaw * DEGS_TO_RADS) * (float) Math.cos(pitch * DEGS_TO_RADS);
		update();
	}

	/**
	 * Updates any matrices needed for the camera to function correctly. Should be
	 * called when the window resizes.
	 * 
	 * @param width  width of the window, used for aspect ratio
	 * @param height height of the window, used for aspect ratio
	 */
	public void updateMatrices(int width, int height) {
		projectionMatrix = createProjectionMatrix(width, height);
	}

	protected abstract Mat4f createProjectionMatrix(int width, int height);

	public Mat4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public Mat4f getViewMatrix() {
		return Mat4f.lookAt(position, position.plus(front), up);
	}

	public Vec3f getPosition() {
		return position;
	}

	public Vec3f getFront() {
		return front;
	}

	public Vec3f getUp() {
		return up;
	}

	public Vec3f getRight() {
		return right;
	}

}
