package au.edu.federation.itech3104.michaelwilson.camera;

import au.edu.federation.itech3104.michaelwilson.math.Mat4f;

/**
 * Represents a perspective camera created using a perspective projection
 * matrix. This class provides access to both near and far clipping planes, and
 * field of view.
 */
public class PerspectiveCamera extends Camera {

	private float fov;
	private float zNear, zFar;

	// Previous width/height used for setFov, setZ methods.
	private int previousWidth = 800; // Dummy default values.
	private int previousHeight = 600;

	public PerspectiveCamera(float fov, float zNear, float zFar) {
		this.fov = fov;
		this.zNear = zNear;
		this.zFar = zFar;
	}

	@Override
	protected Mat4f createProjectionMatrix(int width, int height) {
		previousWidth = width;
		previousHeight = height;
		return Mat4f.createPerspectiveProjectionMatrix(fov, (float) width / (float) height, zNear, zFar);
	}

	public float getFov() {
		return fov;
	}

	public void setFov(float fov) {
		this.fov = fov;
		updateMatrices(previousWidth, previousHeight);
	}

	public float getZNear() {
		return zNear;
	}

	public void setZNear(float zNear) {
		this.zNear = zNear;
		updateMatrices(previousWidth, previousHeight);
	}

	public float getZFar() {
		return zFar;
	}

	public void setZFar(float zFar) {
		this.zFar = zFar;
		updateMatrices(previousWidth, previousHeight);
	}

}
