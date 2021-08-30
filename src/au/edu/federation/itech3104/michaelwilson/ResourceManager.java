package au.edu.federation.itech3104.michaelwilson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import au.edu.federation.itech3104.michaelwilson.graphics.IDisposable;
import au.edu.federation.itech3104.michaelwilson.graphics.Mesh;
import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;
import au.edu.federation.itech3104.michaelwilson.graphics.data.BufferUsageHint;
import au.edu.federation.itech3104.michaelwilson.graphics.data.VertexBufferLayout;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;
import au.edu.federation.itech3104.michaelwilson.graphics.texture.Texture2D;

/**
 * Provides storage for resources (such as {@link Texture2D textures} and
 * {@link ShaderProgram shaders}). Implements the {@link IDisposable} interface
 * for disposing of all loaded resources.
 * 
 * @see #dispose()
 */
public class ResourceManager implements IDisposable {

	private final Map<String, ShaderProgram> shaders = new HashMap<String, ShaderProgram>();
	private final Map<String, Texture2D> textures = new HashMap<String, Texture2D>();
	private final Map<String, Mesh> meshes = new HashMap<String, Mesh>();

	private final Map<String, Material> materials = new HashMap<String, Material>();

	public ResourceManager() {

	}

	/**
	 * Create a Mesh object and store it in this resource manager, so it can be
	 * retrieved and disposed of automatically. This method is equivalent to the
	 * constructor
	 * {@link Mesh#Mesh(float[], int[], BufferUsageHint, VertexBufferLayout, Material)}.
	 *
	 * @param material the material name, see {@link #getMaterial(String)}
	 * @return the mesh, or null if the mesh name is already used.
	 */
	public Mesh addMesh(String name, float[] vertices, int[] indices, BufferUsageHint usageHint, VertexBufferLayout layout, String material) {
		return addMesh(name, vertices, indices, usageHint, layout, getMaterial(material));
	}

	/**
	 * Create a Mesh object and store it in this resource manager, so it can be
	 * retrieved and disposed of automatically. This method is equivalent to the
	 * constructor
	 * {@link Mesh#Mesh(float[], int[], BufferUsageHint, VertexBufferLayout, Material)}.
	 * 
	 * @return the mesh, or null if the mesh name is already used.
	 */
	public Mesh addMesh(String name, float[] vertices, int[] indices, BufferUsageHint usageHint, VertexBufferLayout layout, Material material) {
		if (meshes.containsKey(name))
			return null;

		Mesh mesh = new Mesh(vertices, indices, usageHint, layout, material);
		meshes.put(name, mesh);
		return mesh;
	}

	/**
	 * Create a Mesh object and store it in this resource manager, so it can be
	 * retrieved and disposed of automatically. This method is equivalent to the
	 * constructor
	 * {@link Mesh#Mesh(float[], int, BufferUsageHint, VertexBufferLayout, Material)}.
	 * 
	 * @param material the material name, see {@link #getMaterial(String)}
	 * @return the mesh, or null if the mesh name is already used.
	 */
	public Mesh addMesh(String name, float[] vertices, int count, BufferUsageHint usageHint, VertexBufferLayout layout, String material) {
		return addMesh(name, vertices, count, usageHint, layout, getMaterial(material));
	}

	/**
	 * Add the provided mesh to this {@link ResourceManager}, so it can be retrieved
	 * and disposed of automatically.
	 * 
	 * @return the provided mesh, or null is the mesh name is already used.
	 */
	public Mesh addMesh(String name, Mesh mesh) {
		if (meshes.containsKey(name))
			return null;

		meshes.put(name, mesh);
		return mesh;
	}

	/**
	 * Create a Mesh object and store it in this resource manager, so it can be
	 * retrieved and disposed of automatically. This method is equivalent to the
	 * constructor
	 * {@link Mesh#Mesh(float[], int, BufferUsageHint, VertexBufferLayout, Material)}.
	 */
	public Mesh addMesh(String name, float[] vertices, int count, BufferUsageHint usageHint, VertexBufferLayout layout, Material material) {
		if (meshes.containsKey(name))
			return null;

		Mesh mesh = new Mesh(vertices, count, usageHint, layout, material);
		meshes.put(name, mesh);
		return mesh;
	}

	public Material loadMaterial(String name, String shaderName) {
		if (materials.containsKey(name))
			return null;

		ShaderProgram shader = getShader(shaderName);
		if (shader == null)
			throw new IllegalArgumentException("Unknown shader " + name + " when loading material!" + name);

		Material mat = new Material(shader);
		materials.put(name, mat);
		return mat;
	}

	public Material addMaterial(String name, Material material) {
		if (materials.containsKey(name))
			return null;

		materials.put(name, material);
		return material;
	}

	/**
	 * Load a {@link Texture2D} object from the file system into this
	 * {@link ResourceManager}.
	 * 
	 * @param name     the name of the texture resource
	 * @param filepath the filepath to the texture
	 * @return the texture object, or null if a texture is already loaded with the
	 *         given name.
	 */
	public Texture2D loadTexture(String name, String filepath) throws FileNotFoundException, IOException {
		if (textures.containsKey(name))
			return null;

		Texture2D texture = new Texture2D(filepath);
		textures.put(name, texture);
		return texture;
	}

	/**
	 * Loads a {@link ShaderProgram} from the provided strings into this
	 * {@link ResourceManager}.
	 * 
	 * @param name           the name of the shader program resource
	 * @param vertexSource   the vertex shader source code
	 * @param fragmentSource the fragment shader source code
	 * @return the shader program, or null if a shader is already loaded with the
	 *         given name
	 */
	public ShaderProgram loadShaderFromString(String name, String vertexSource, String fragmentSource) {
		if (shaders.containsKey(name))
			return null;

		ShaderProgram shader = new ShaderProgram(vertexSource, fragmentSource);
		shaders.put(name, shader);
		return shader;
	}

	/**
	 * Loads a {@link ShaderProgram} from the file system into this
	 * {@link ResourceManager}.
	 * 
	 * @param name                   the name of the shader program resource
	 * @param vertexShaderFilename   the filepath to the vertex shader
	 * @param fragmentShaderFilename the filepath to the fragment shader
	 * @return the shader program, or null if a shader is already loaded with the
	 *         given name
	 */
	public ShaderProgram loadShader(String name, String vertexShaderFilename, String fragmentShaderFilename) throws IOException {
		if (shaders.containsKey(name))
			return null;

		ShaderProgram shader = ShaderProgram.fromFile(vertexShaderFilename, fragmentShaderFilename);
		shaders.put(name, shader);
		return shader;
	}

	/**
	 * Return a loaded {@link Material} by the given name.
	 * 
	 * @param name the name of the material.
	 * @return a material, or null if the name isn't associated with anything.
	 */
	public Material getMaterial(String name) {
		return materials.get(name);
	}

	/**
	 * Return a loaded {@link ShaderProgram} by the given name.
	 * 
	 * @param name the name of the shader program.
	 * @return a shader program, or null if the name isn't associated with anything.
	 */
	public ShaderProgram getShader(String name) {
		return shaders.get(name);
	}

	/**
	 * Return a loaded {@link Texture2D} by the given name.
	 * 
	 * @param name the name of the texture.
	 * @return a texture, or null if the name isn't associated with anything.
	 */
	public Texture2D getTexture(String name) {
		return textures.get(name);
	}

	/**
	 * Return a loaded {@link Mesh} by the given name.
	 * 
	 * @param name the name of the mesh.
	 * @return a mesh, or null if the name isn't associated with anything.
	 */
	public Mesh getMesh(String name) {
		return meshes.get(name);
	}

	/**
	 * Returns an array of {@link Texture2D} objects gathered using the
	 * {@link #getTexture(String)} method. Will include any null values returned by
	 * {@link #getTexture(String)}.
	 * 
	 * @param names the names of each texture.
	 */
	public Texture2D[] getTextures(String... names) {
		Texture2D[] textures = new Texture2D[names.length];

		for (int i = 0; i < names.length; i++)
			textures[i] = getTexture(names[i]);

		return textures;
	}

	/**
	 * Dispose of all resources, loaded by methods of this {@link ResourceManager}.
	 * Can be called multiple times.
	 */
	@Override
	public void dispose() {
		for (IDisposable disposable : shaders.values()) // Dispose of all shaders.
			disposable.dispose();

		for (IDisposable disposable : textures.values()) // Dispose of all textures.
			disposable.dispose();

		for (IDisposable disposable : meshes.values()) // Dispose of all meshes.
			disposable.dispose();

		shaders.clear();
		textures.clear();
		meshes.clear();

		materials.clear();
	}

}