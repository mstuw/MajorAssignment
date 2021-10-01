package au.edu.federation.itech3104.michaelwilson.lighting;

import au.edu.federation.itech3104.michaelwilson.graph.Transform;
import au.edu.federation.itech3104.michaelwilson.graphics.ILight;
import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;

/**
 * A base class for all light sources. Provides ambient, diffuse, and specular
 * components. Includes the required {@link ILight implementation} needed to
 * update any {@link ShaderProgram} of this light source.
 */
public abstract class Light extends Transform implements ILight {
	private static final String UNIFORM_DELIMITER = ".";

	private final Vec3f ambient;
	private final Vec3f diffuse;
	private final Vec3f specular;

	private boolean enabled = true;

	public Light(Vec3f ambient, Vec3f diffuse, Vec3f specular) {
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
	}

	public abstract String getTypeName();

	@Override
	public void apply(int index, ShaderProgram shader) {
		// The GLSL struct array with indexer (e.g. pointLights[2])
		String elementName = String.format("%s[%d]", getTypeName(), index);

		shader.setUniform(join(elementName, "enabled"), enabled);

		shader.setUniform(join(elementName, "ambient"), ambient);
		shader.setUniform(join(elementName, "diffuse"), diffuse);
		shader.setUniform(join(elementName, "specular"), specular);

		set(elementName, shader);
	}

	protected abstract void set(String elementName, ShaderProgram shader);

	protected static String join(String... values) {
		return String.join(UNIFORM_DELIMITER, values);
	}

	public Vec3f getAmbient() {
		return ambient;
	}

	public Vec3f getDiffuse() {
		return diffuse;
	}

	public Vec3f getSpecular() {
		return specular;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
