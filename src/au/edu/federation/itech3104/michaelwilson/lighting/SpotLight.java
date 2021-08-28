package au.edu.federation.itech3104.michaelwilson.lighting;

import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;

public class SpotLight extends DirectionalLight {

	private float constant;
	private float linear;
	private float quadratic;
	private float cutOff;
	private float outerCutOff;

	public SpotLight(Vec3f direction, Vec3f ambient, Vec3f diffuse, Vec3f specular, float constant, float linear, float quadratic, float cutOff,
			float outerCutOff) {
		super(direction, ambient, diffuse, specular);
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
		shader.setUniform(join(elementName, "constant"), constant);
		shader.setUniform(join(elementName, "linear"), linear);
		shader.setUniform(join(elementName, "quadratic"), quadratic);
		shader.setUniform(join(elementName, "cutOff"), cutOff);
		shader.setUniform(join(elementName, "outerCutOff"), outerCutOff);
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

	public float getCutOff() {
		return cutOff;
	}

	public void setCutOff(float cutOff) {
		this.cutOff = cutOff;
	}

	public float getOuterCutOff() {
		return outerCutOff;
	}

	public void setOuterCutOff(float outerCutOff) {
		this.outerCutOff = outerCutOff;
	}

}
