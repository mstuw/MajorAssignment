package au.edu.federation.itech3104.michaelwilson.graphics.material;

import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;

public class Vec3fAttribute extends MaterialAttribute {

	public Vec3fAttribute(String name, Vec3f value) {
		super(name);
		set(value);
	}

	@Override
	public void setUniform(ShaderProgram shaderProgram) {
		shaderProgram.setUniform(getName(), (Vec3f) get());
	}

	@Override
	public MaterialAttribute copy() {
		return new Vec3fAttribute(getName(), (Vec3f) get());
	}

}