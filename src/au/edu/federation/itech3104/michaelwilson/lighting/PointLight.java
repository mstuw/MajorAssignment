package au.edu.federation.itech3104.michaelwilson.lighting;

import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;

public class PointLight extends Light {

	private float constant;
	private float linear;
	private float quadratic;

	public PointLight(Vec3f ambient, Vec3f diffuse, Vec3f specular, float constant, float linear, float quadratic) {
		super(ambient, diffuse, specular);
		this.constant = constant;
		this.linear = linear;
		this.quadratic = quadratic;
	}

	@Override
	protected void set(String elementName, ShaderProgram shader) {	
		shader.setUniform(join(elementName, "constant"), constant);
		shader.setUniform(join(elementName, "linear"), linear);
		shader.setUniform(join(elementName, "quadratic"), quadratic);
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

	@Override
	public String getTypeName() {
		return "pointLights";
	}

}
