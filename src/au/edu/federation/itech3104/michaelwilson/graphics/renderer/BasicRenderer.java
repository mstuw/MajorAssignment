package au.edu.federation.itech3104.michaelwilson.graphics.renderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.federation.itech3104.michaelwilson.camera.Camera;
import au.edu.federation.itech3104.michaelwilson.graph.Transform;
import au.edu.federation.itech3104.michaelwilson.graphics.ILight;
import au.edu.federation.itech3104.michaelwilson.graphics.ILightingTracker;
import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;
import au.edu.federation.itech3104.michaelwilson.graphics.material.MaterialAttribute;
import au.edu.federation.itech3104.michaelwilson.graphics.material.TextureAttribute;
import au.edu.federation.itech3104.michaelwilson.math.Mat3f;
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
	public void renderTree(Transform transform) {
		// Set the lighting tracker if none if set. Lighting tracker is used for all
		// lights in the scene.
		if (lightingTracker == null && transform instanceof ILightingTracker)
			lightingTracker = (ILightingTracker) transform;

		if (transform instanceof IDrawable)
			render((IDrawable) transform, transform.getGlobalMatrix());

		for (Transform child : transform)
			renderTree(child);
	}

	@Override
	public void render(IDrawable drawable, Mat4f modelMatrix) {
		Material material = drawable.getMaterial();
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
				shader.setModelMatrix(modelMatrix);

				// Inversing is expensive, so only set the uniform if the shader supports
				// lighting.
				if (material.isLightingSupported()) {
					Mat4f normalizedMatrix = new Mat4f(modelMatrix); // Remove the scaling from the model matrix, so we can inverse without breaking
																		// lighting normals.

					float x = modelMatrix.getXBasis().length();
					normalizedMatrix.m00 /= x;
					normalizedMatrix.m01 /= x;
					normalizedMatrix.m02 /= x;

					float y = modelMatrix.getYBasis().length();
					normalizedMatrix.m10 /= y;
					normalizedMatrix.m11 /= y;
					normalizedMatrix.m12 /= y;

					float z = modelMatrix.getZBasis().length();
					normalizedMatrix.m20 /= z;
					normalizedMatrix.m21 /= z;
					normalizedMatrix.m22 /= z;

					shader.setUniform("modelNormal", Mat4f.inverse(normalizedMatrix).transpose().toMat3f());
				}

				// Draw the actual object.
				drawable.draw(this);

				// Unbind textures.
				for (int i = 0; i < textureCount; i++) {
					TextureAttribute namedTexture = material.getTextures().get(i);
					namedTexture.getTexture().unbind(i);
				}
			}
			shader.unbind();

		} else {
			drawable.draw(this);
		}
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
