package au.edu.federation.itech3104.michaelwilson.graphics.material;

import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.math.Mat4f;

public class Mat4fAttribute extends MaterialAttribute {

	public Mat4fAttribute(String name, Mat4f value) {
		super(name);
		set(value);
	}

	@Override
	public void setUniform(ShaderProgram shaderProgram) {
		shaderProgram.setUniform(getName(), (Mat4f) get());
	}

	@Override
	public MaterialAttribute copy() {
		return new Mat4fAttribute(getName(), (Mat4f) get());
	}
	
}