package au.edu.federation.itech3104.michaelwilson.graphics.renderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.federation.itech3104.michaelwilson.camera.Camera;
import au.edu.federation.itech3104.michaelwilson.graph.Transform;
import au.edu.federation.itech3104.michaelwilson.graphics.IDrawable;
import au.edu.federation.itech3104.michaelwilson.graphics.ILight;
import au.edu.federation.itech3104.michaelwilson.graphics.ILightingTracker;
import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;
import au.edu.federation.itech3104.michaelwilson.graphics.material.MaterialAttribute;
import au.edu.federation.itech3104.michaelwilson.graphics.material.TextureAttribute;
import au.edu.federation.itech3104.michaelwilson.math.Mat4f;

/**
 * A basic forward renderer for rendering {@link IDrawable} objects.
 */
public class BasicRenderer implements IRenderer {
	private final Map<String, Integer> lightIndices = new HashMap<>();

	private Camera camera;

	private ILightingTracker lightingTracker;

	public BasicRenderer(Camera camera) {
		this.camera = camera;
	}

	@Override
	public void draw(Transform transform) {
		// Set the lighting tracker if none if set. Lighting tracker is used for all
		// lights in the scene.
		if (lightingTracker == null && transform instanceof ILightingTracker)
			lightingTracker = (ILightingTracker) transform;

		if (transform instanceof IDrawable)
			draw((IDrawable) transform, transform.getGlobalMatrix());

		for (Transform child : transform)
			draw(child);
	}

	@Override
	public void draw(IDrawable drawable, Mat4f modelMatrix) {
		Material material = drawable.getMaterial();

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
			shader.setModelMatrix(modelMatrix);

			// inversing is expensive, so only set the uniform if the shader supports
			// lighting.
			if (material.isLightingSupported())
				shader.setUniform("modelNormal", Mat4f.inverse(modelMatrix).transposed().toMat3f());

			// Draw the actual object.
			drawable.draw();

			// Unbind textures.
			for (int i = 0; i < textureCount; i++) {
				TextureAttribute namedTexture = material.getTextures().get(i);
				namedTexture.getTexture().unbind(i);
			}
		}
		shader.unbind();
	}

	public ILightingTracker getLightingTracker() {
		return lightingTracker;
	}

	public void setLightingTracker(ILightingTracker lightingTracker) {
		this.lightingTracker = lightingTracker;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public Camera getCamera() {
		return camera;
	}

}
