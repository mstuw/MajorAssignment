package au.edu.federation.itech3104.michaelwilson.lighting;

import au.edu.federation.itech3104.michaelwilson.graph.ILight;
import au.edu.federation.itech3104.michaelwilson.graph.Object3D;
import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;

public abstract class Light extends Object3D implements ILight {
	private static final String UNIFORM_DELIMITER = ".";

	private final Vec3f ambient;
	private final Vec3f diffuse;
	private final Vec3f specular;

	public Light(Vec3f ambient, Vec3f diffuse, Vec3f specular) {
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
	}

	public abstract String getTypeName();

	@Override
	public void update(float deltaTime) {

	}

	@Override
	public void apply(int index, ShaderProgram shader) {
		// The GLSL struct array with indexer (e.g. pointLights[2])
		String elementName = String.format("%s[%d]", getTypeName(), index);

		shader.setUniform(join(elementName, "enabled"), true);
		shader.setUniform(join(elementName, "position"), getGlobalMatrix().getOrigin());

		shader.setUniform(join(elementName, "ambient"), ambient);
		shader.setUniform(join(elementName, "diffuse"), diffuse);
		shader.setUniform(join(elementName, "specular"), specular);

		set(elementName, shader);
	}

	protected abstract void set(String elementName, ShaderProgram shader);

	@Override
	protected void draw() {

	}

	@Override
	public Material getMaterial() {
		return null;
	}

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

}
