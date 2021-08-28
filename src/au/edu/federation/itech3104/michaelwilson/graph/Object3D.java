package au.edu.federation.itech3104.michaelwilson.graph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.federation.itech3104.michaelwilson.camera.Camera;
import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;
import au.edu.federation.itech3104.michaelwilson.graphics.material.MaterialAttribute;
import au.edu.federation.itech3104.michaelwilson.graphics.material.TextureAttribute;
import au.edu.federation.itech3104.michaelwilson.math.Mat4f;

public abstract class Object3D extends Node<Object3D> {
	/**
	 * The render stack used by the {@link #drawAll(Camera)} and
	 * {@link #drawNext(Deque)} method.
	 */
	private static final Deque<Object3D> renderStack = new ArrayDeque<>();

	/**
	 * The model matrix local to this {@link Object3D}. Any modifications to this
	 * matrix requires the {@link #updateGlobalMatrix()} method to be called, so
	 * changes can be reflected in descendant {@link #globalMatrix matrices}.<br>
	 * <br>
	 * Note: The local matrix is public so we can easily use {@link Mat4f} methods
	 * that return a new instance.
	 */
	public Mat4f localMatrix = new Mat4f(1.0f);

	/**
	 * The global model matrix combining all ancestor global matrices.
	 * 
	 * @see #updateGlobalMatrix()
	 */
	private Mat4f globalMatrix = new Mat4f(1.0f);

	public Object3D() {
	}

	/**
	 * Draw this object and all descendants.
	 * 
	 * @param shaderModifiers typically a collection of lighting objects.
	 */
	public void drawAll(Camera camera, ILightingTracker shaderModifiers) {
		renderStack.push(this);

		while (!renderStack.isEmpty()) {
			Object3D object3D = renderStack.pop();

			// TODO: if (object3D.isVisible())
			object3D.drawObject(camera, shaderModifiers);

			if (!object3D.drawNext(renderStack)) {
				for (Object3D child : object3D.getChildren())
					renderStack.push(child);
			}
		}
	}

	/**
	 * Update this object and all descendants. (Handle game logic)
	 */
	public void updateAll(float deltaTime) {
		update(deltaTime);

		for (Object3D child : getChildren())
			child.updateAll(deltaTime);
	}

	/**
	 * Handle game logic for this object.
	 */
	public abstract void update(float deltaTime);

	/**
	 * Draw this object.
	 */
	protected abstract void draw();

	/**
	 * Method for deciding what to render next. Primarily used by derived classes.
	 * Can be used to selectively draw child nodes.
	 * 
	 * @return true to exclude all child objects from being added to the render
	 *         queue.
	 */
	protected boolean drawNext(Deque<Object3D> renderStack) {
		return false;
	}

	/**
	 * Draw this object using {@link #getMaterial() material} and the camera
	 * provided. If the material is null this method does nothing.
	 */
	public void drawObject(Camera camera, ILightingTracker lightingTracker) {
		Material material = getMaterial();

		if (material != null) {
			ShaderProgram shader = material.getShaderProgram();
			shader.bind();
			{
				// Set the projection and view matrices.
				shader.setUniforms(camera);

				// Bind textures.
				int textureCount = material.getTextures().size();
				for (int i = 0; i < textureCount; i++) {
					TextureAttribute namedTexture = material.getTextures().get(i);

					// bind texture, set texture unit, and set sampler uniform.
					shader.setUniform(namedTexture.getName(), i);
					namedTexture.getTexture().bind(i);
				}

				// Set material defined uniform values.
				for (MaterialAttribute attribute : material.getAttributes())
					attribute.setUniform(shader);

				// Apply uniforms for lighting nodes.
				if (material.isLightingSupported() && lightingTracker != null) {
					lightIndices.clear();

					List<ILight> lights = lightingTracker.getLights();
					for (ILight light : lights) {
						// A separate index for each lighting type. (e.g. point, directional, and spot
						// light indices)
						int index = lightIndices.getOrDefault(light.getTypeName(), 0);

						light.apply(index, shader);

						lightIndices.put(light.getTypeName(), index + 1);
					}
				}

				// Set the model matrix.
				Mat4f modelMatrix = getGlobalMatrix();
				shader.setUniform("model", modelMatrix);

				// inversing is expensive, so only set the uniform if the shader supports
				// lighting.
				if (material.isLightingSupported())
					shader.setUniform("modelNormal", Mat4f.inverse(modelMatrix).transposed().toMat3f());

				// Draw the actual object.
				draw();

				// Unbind textures.
				for (int i = 0; i < textureCount; i++) {
					TextureAttribute namedTexture = material.getTextures().get(i);
					namedTexture.getTexture().unbind(i);
				}
			}
			shader.unbind();
		}
	}

	private final Map<String, Integer> lightIndices = new HashMap<>();

	/**
	 * Return the material used for drawing this object. Returning null will disable
	 * drawing of this object.
	 */
	public abstract Material getMaterial();

	/**
	 * Calculates the global matrix for this node using the
	 * {@link #getGlobalMatrix() global} and {@link #localMatrix local} matrices of
	 * ancestor nodes. Must be called whenever the local matrix is modified.
	 */
	public void updateGlobalMatrix() {
		Mat4f parentMatrix = isRoot() ? new Mat4f(1.0f) : getParent().globalMatrix;

		globalMatrix = parentMatrix.times(localMatrix);

		for (Object3D child : getChildren())
			child.updateGlobalMatrix();
	}

	@Override
	protected void onParentChanged() {
		super.onParentChanged();
		updateGlobalMatrix(); // Update the global matrix, since the parent has changed.
	}

	/**
	 * The global model matrix combining all ancestor {@link Object3D#localMatrix
	 * model matrices}.
	 * 
	 * @see #updateGlobalMatrix()
	 */
	public Mat4f getGlobalMatrix() {
		return globalMatrix;
	}

}
