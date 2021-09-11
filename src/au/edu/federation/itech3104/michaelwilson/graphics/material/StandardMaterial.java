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

	public void setTexture(Texture2D diffuse, Texture2D specular) {
		diffuseTexture = diffuse;
		specularTexture = specular;
		setTexture("material.diffuse", diffuse);
		setTexture("material.specular", specular);
	}

	public void setDiffuseTexture(Texture2D diffuse) {
		setTexture(diffuse, getSpecularTexture());
	}

	public void setSpecularTexture(Texture2D specular) {
		setTexture(getDiffuseTexture(), specular);
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
