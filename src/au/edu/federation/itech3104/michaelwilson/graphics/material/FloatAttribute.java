package au.edu.federation.itech3104.michaelwilson.graphics.material;

import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;

public class FloatAttribute extends MaterialAttribute {

	public FloatAttribute(String name, float value) {
		super(name);
		set(value);
	}

	@Override
	public void setUniform(ShaderProgram shaderProgram) {
		shaderProgram.setUniform(getName(), (float) get());
	}

	@Override
	public MaterialAttribute copy() {
		return new FloatAttribute(getName(), (float) get());
	}

}