package au.edu.federation.itech3104.michaelwilson.graphics.material;

import java.util.Objects;

import au.edu.federation.itech3104.michaelwilson.graphics.texture.Texture2D;

/**
 * Represents a named {@link Texture2D} object.
 */
public class TextureAttribute {
	private final String name;
	private final Texture2D texture;

	public TextureAttribute(String name, Texture2D texture) {
		this.name = name;
		this.texture = texture;
	}

	public String getName() {
		return name;
	}

	public Texture2D getTexture() {
		return texture;
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
		TextureAttribute other = (TextureAttribute) obj;
		return Objects.equals(name, other.name);
	}

}