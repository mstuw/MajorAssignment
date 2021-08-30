package au.edu.federation.itech3104.michaelwilson.lighting;

import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;

/**
 * Represents a light source that has a position. Most light sources will have a
 * position.
 */
public abstract class PositionalLight extends Light {

	public PositionalLight(Vec3f ambient, Vec3f diffuse, Vec3f specular) {
		super(ambient, diffuse, specular);
	}

	@Override
	protected void set(String elementName, ShaderProgram shader) {
		shader.setUniform(join(elementName, "position"), getGlobalMatrix().getOrigin());
	}

}
