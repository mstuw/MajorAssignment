package au.edu.federation.itech3104.michaelwilson.graphics.material;

import java.util.Objects;

import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;

/**
 * A {@link MaterialAttribute} is an object that holds a name and value.
 * Representing a uniform value for a {@link ShaderProgram}. <br>
 * Its expected to implement getValue() and setValue() in the derived classes
 * using the proper type of that attribute. (e.g. See
 * {@link FloatAttribute#getValue()} and {@link FloatAttribute#setValue(float)})
 */
public abstract class MaterialAttribute {

	private final String name;
	private Object value;

	public MaterialAttribute(String name) {
		this.name = name;
	}

	public abstract MaterialAttribute copy();

	/**
	 * Apply this attribute to the given shader program by setting the required
	 * uniforms.
	 */
	public abstract void setUniform(ShaderProgram shaderProgram);

	/**
	 * Generic method to set this attribute's value. Note: This method is unchecked,
	 * ensure the object type matches the attribute type.
	 */
	public void set(Object value) {
		this.value = value;
	}

	/**
	 * Generic method to get this attribute's value.
	 */
	public Object get() {
		return value;
	}

	public String getName() {
		return name;
	}

	/**
	 * Cast this generic attribute into a concrete attribute.
	 */
	@SuppressWarnings("unchecked")
	public <T extends MaterialAttribute> T as(Class<T> c) {
		return (T) this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MaterialAttribute other = (MaterialAttribute) obj;
		return Objects.equals(name, other.name);
	}

}
