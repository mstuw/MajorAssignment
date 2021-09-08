package au.edu.federation.itech3104.michaelwilson.graphics.material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.graphics.texture.Texture2D;
import au.edu.federation.itech3104.michaelwilson.math.Mat4f;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;

/**
 * Represents a shader program and all uniforms required for the shader to
 * function correctly, excluding uniforms such as the MVP matrices and
 * cameraPosition.
 */
public class Material {

	private final ShaderProgram shaderProgram;

	private final Map<String, MaterialAttribute> attributes = new HashMap<>();
	private final List<TextureAttribute> textures = new ArrayList<>();

	private boolean isLightingSupported;

	/**
	 * Create a new Material. This Material will not take ownership of the provided
	 * shader program.
	 */
	public Material(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
	}

	public Material(Material material) {
		this.shaderProgram = material.shaderProgram;
		this.isLightingSupported = material.isLightingSupported;

		for (Map.Entry<String, MaterialAttribute> entry : material.attributes.entrySet())
			attributes.put(entry.getKey(), entry.getValue().copy());

		for (TextureAttribute texture : material.textures)
			textures.add(texture);
	}

	public Material copy() {
		return new Material(this);
	}

	public Material addAttribute(String name, Mat4f value) {
		attributes.put(name, new Mat4fAttribute(name, value));
		return this;
	}

	public Material addAttribute(String name, Vec3f value) {
		attributes.put(name, new Vec3fAttribute(name, value));
		return this;
	}

	public Material addAttribute(String name, int value) {
		attributes.put(name, new IntAttribute(name, value));
		return this;
	}

	public Material addAttribute(String name, float value) {
		attributes.put(name, new FloatAttribute(name, value));
		return this;
	}

	public Material setTexture(String name, Texture2D texture) {
		textures.removeIf(t -> t.getName() == name);

		if (texture != null)
			textures.add(new TextureAttribute(name, texture));

		return this;
	}

	public MaterialAttribute getAttribute(String name) {
		return attributes.get(name);
	}

	public Collection<MaterialAttribute> getAttributes() {
		return attributes.values();
	}

	public List<TextureAttribute> getTextures() {
		return textures;
	}

	public ShaderProgram getShaderProgram() {
		return shaderProgram;
	}

	public boolean isLightingSupported() {
		return isLightingSupported;
	}

	public void setLightingSupported(boolean isLightingSupported) {
		this.isLightingSupported = isLightingSupported;
	}

}
