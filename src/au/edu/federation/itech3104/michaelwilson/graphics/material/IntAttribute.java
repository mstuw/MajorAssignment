package au.edu.federation.itech3104.michaelwilson.graphics.material;

import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;

public class IntAttribute extends MaterialAttribute {

	public IntAttribute(String name, int value) {
		super(name);
		set(value);
	}

	@Override
	public void setUniform(ShaderProgram shaderProgram) {
		shaderProgram.setUniform(getName(), (int) get());
	}

	@Override
	public MaterialAttribute copy() {
		return new IntAttribute(getName(), (int) get());
	}
}