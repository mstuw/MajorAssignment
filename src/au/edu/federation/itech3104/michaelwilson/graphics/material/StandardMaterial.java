package au.edu.federation.itech3104.michaelwilson.graphics.material;

import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.graphics.texture.Texture2D;

public class StandardMaterial extends Material {

	private Texture2D diffuseTexture;
	private Texture2D specularTexture;
	private float shininess;

	public StandardMaterial(StandardMaterial material) {
		super(material);
		this.shininess = material.shininess;
		this.diffuseTexture = material.diffuseTexture;
		this.specularTexture = material.specularTexture;
	}

	@Override
	public StandardMaterial copy() {
		return new StandardMaterial(this);
	}

	public StandardMaterial(ShaderProgram shaderProgram) {
		super(shaderProgram);
		setLightingSupported(true);
		setShininess(32.0f);
	}

	public void setShininess(float shininess) {
		this.shininess = shininess;
		addAttribute("material.shininess", shininess);
	}

	// TODO: Since texture unit is set by the texture list order. Setting specular before diffuse could break the shader.
	public void setDiffuseTexture(Texture2D texture) {
		diffuseTexture = texture;
		setTexture("material.diffuse", texture);
	}

	public void setSpecularTexture(Texture2D texture) {
		diffuseTexture = texture;
		setTexture("material.specular", texture);
	}

	public float getShininess() {
		return shininess;
	}

	public Texture2D getDiffuseTexture() {
		return diffuseTexture;
	}

	public Texture2D getSpecularTexture() {
		return specularTexture;
	}

}
