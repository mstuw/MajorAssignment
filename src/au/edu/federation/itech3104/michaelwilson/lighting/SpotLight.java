package au.edu.federation.itech3104.michaelwilson.lighting;

import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;

/**
 * Represents a light source that has a position and a direction. The light will
 * travel in a cone towards the direction. See the {@link #setCutOff(float)} for
 * the cone angle. This light source is similar to the {@link DirectionalLight}
 * without having an "infinite" source distance. (e.g. flashlight/headlights)
 *
 */
public class SpotLight extends PositionalLight {

	private Vec3f direction;
	private float constant;
	private float linear;
	private float quadratic;
	private float cutOff;
	private float outerCutOff;

	public SpotLight(Vec3f direction, Vec3f ambient, Vec3f diffuse, Vec3f specular, float constant, float linear, float quadratic, float cutOff,
			float outerCutOff) {
		super(ambient, diffuse, specular);
		this.direction = direction;
		this.constant = constant;
		this.linear = linear;
		this.quadratic = quadratic;
		this.cutOff = cutOff;
		this.outerCutOff = outerCutOff;
	}

	@Override
	public String getTypeName() {
		return "spotLights";
	}

	@Override
	protected void set(String elementName, ShaderProgram shader) {
		super.set(elementName, shader);
		shader.setUniform(join(elementName, "direction"), direction);
		shader.setUniform(join(elementName, "constant"), constant);
		shader.setUniform(join(elementName, "linear"), linear);
		shader.setUniform(join(elementName, "quadratic"), quadratic);
		shader.setUniform(join(elementName, "cutOff"), cutOff);
		shader.setUniform(join(elementName, "outerCutOff"), outerCutOff);
	}

	public Vec3f getDirection() {
		return direction;
	}

	public float getConstant() {
		return constant;
	}

	public void setConstant(float constant) {
		this.constant = constant;
	}

	public float getLinear() {
		return linear;
	}

	public void setLinear(float linear) {
		this.linear = linear;
	}

	public float getQuadratic() {
		return quadratic;
	}

	public void setQuadratic(float quadratic) {
		this.quadratic = quadratic;
	}

	/**
	 * Returns the cutoff angle (radians) of the inner cone.
	 */
	public float getCutOff() {
		return cutOff;
	}

	/**
	 * Represents the cutoff angle (radians) of the inner cone.
	 */
	public void setCutOff(float cutOff) {
		this.cutOff = cutOff;
	}

	/**
	 * Returns the cutoff angle (radians) of the outer cone.
	 */
	public float getOuterCutOff() {
		return outerCutOff;
	}

	/**
	 * Represents the cutoff angle (radians) of the outer cone. Light will be
	 * smoothly dimmed between the inner and outer cutoff.
	 */
	public void setOuterCutOff(float outerCutOff) {
		this.outerCutOff = outerCutOff;
	}

}
