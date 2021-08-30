package au.edu.federation.itech3104.michaelwilson.lighting;

import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;

/**
 * A directional light source, similar to sunlight. The light source is
 * considered to be infinitely far away, so all light rays are considered to be
 * parallel to one another.
 */
public class DirectionalLight extends Light {

	private final Vec3f direction;

	public DirectionalLight(Vec3f direction, Vec3f ambient, Vec3f diffuse, Vec3f specular) {
		super(ambient, diffuse, specular);
		this.direction = direction.normalised();
	}

	@Override
	protected void set(String elementName, ShaderProgram shader) {
		shader.setUniform(join(elementName, "direction"), direction);
	}

	public Vec3f getDirection() {
		return direction;
	}

	@Override
	public String getTypeName() {
		return "dirLights";
	}

}
