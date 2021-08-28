package au.edu.federation.itech3104.michaelwilson.camera;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetInputMode;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;

import java.nio.DoubleBuffer;

import au.edu.federation.itech3104.michaelwilson.graphics.BufferUtil;

// This class makes a camera user controllable. Allowing the user to control the camera like a spectator camera.
public class CameraController {

	private Camera camera;
	private float movementSpeed = 2f;

	private float mouseSensitivity = 0.25f;

	private final DoubleBuffer xbuf = BufferUtil.createEmptyDoubleBuffer(1);
	private final DoubleBuffer ybuf = BufferUtil.createEmptyDoubleBuffer(1);

	private float currentYaw = 270;
	private float currentPitch;

	// Cursor position
	private int mx, my, previousMx, previousMy;

	public CameraController(Camera camera) {
		this.camera = camera;
	}

	public void update(long windowHandle, float deltaTime) {
		if (camera == null)
			return;

		float velocity = movementSpeed * deltaTime;

		// Add/Subtract directly from the position vector.
		// getFront() and getRight() should both already be normalized.

		// Forwards/Backwards
		if (glfwGetKey(windowHandle, GLFW_KEY_W) == GLFW_PRESS || glfwGetKey(windowHandle, GLFW_KEY_UP) == GLFW_PRESS) {
			camera.getPosition().add(camera.getFront().times(velocity));

		} else if (glfwGetKey(windowHandle, GLFW_KEY_S) == GLFW_PRESS || glfwGetKey(windowHandle, GLFW_KEY_DOWN) == GLFW_PRESS) {
			camera.getPosition().subtract(camera.getFront().times(velocity));

		}

		// Left/Right
		if (glfwGetKey(windowHandle, GLFW_KEY_A) == GLFW_PRESS || glfwGetKey(windowHandle, GLFW_KEY_LEFT) == GLFW_PRESS) {
			camera.getPosition().subtract(camera.getRight().times(velocity));

		} else if (glfwGetKey(windowHandle, GLFW_KEY_D) == GLFW_PRESS || glfwGetKey(windowHandle, GLFW_KEY_RIGHT) == GLFW_PRESS) {
			camera.getPosition().add(camera.getRight().times(velocity));

		}

		// Up/Down
		if (glfwGetKey(windowHandle, GLFW_KEY_E) == GLFW_PRESS) {
			camera.getPosition().add(camera.getUp().times(velocity));

		} else if (glfwGetKey(windowHandle, GLFW_KEY_Q) == GLFW_PRESS) {
			camera.getPosition().subtract(camera.getUp().times(velocity));

		}

		// Handle looking.
		glfwGetCursorPos(windowHandle, xbuf, ybuf);

		previousMx = mx;
		previousMy = my;
		mx = (int) Math.floor(xbuf.get(0));
		my = (int) Math.floor(ybuf.get(0));

		if (isCursorHidden(windowHandle)) { // Only look around when the cursor is hidden.
			int deltaX = mx - previousMx;
			int deltaY = my - previousMy;

			if (deltaX != 0 || deltaY != 0) {
				currentPitch -= (float) deltaY * mouseSensitivity;
				currentYaw += (float) deltaX * mouseSensitivity;

				currentPitch = Math.min(Math.max(currentPitch, -80), 80); // limit pitch to +/-80 degrees.
				camera.lookAt(currentPitch, currentYaw);
			}

		} else if (glfwGetMouseButton(windowHandle, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) { // Hide the cursor if we
																								// click with LMB
			setCursorHidden(windowHandle, true);
		}

		if (glfwGetKey(windowHandle, GLFW_KEY_ESCAPE) == GLFW_PRESS) // Show the cursor if we press the escape key.
			setCursorHidden(windowHandle, false);

	}

	public static boolean isCursorHidden(long windowHandle) {
		return glfwGetInputMode(windowHandle, GLFW_CURSOR) == GLFW_CURSOR_DISABLED;
	}

	public static void setCursorHidden(long windowHandle, boolean isHidden) {
		glfwSetInputMode(windowHandle, GLFW_CURSOR, isHidden ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
	}

	public float getMovementSpeed() {
		return movementSpeed;
	}

	public void setMovementSpeed(float movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public float getMouseSensitivity() {
		return mouseSensitivity;
	}

	public void setMouseSensitivity(float mouseSensitivity) {
		this.mouseSensitivity = mouseSensitivity;
	}

}
