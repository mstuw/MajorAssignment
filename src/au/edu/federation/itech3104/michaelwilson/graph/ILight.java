package au.edu.federation.itech3104.michaelwilson.graph;

import au.edu.federation.itech3104.michaelwilson.graphics.ShaderProgram;

/**
 * 
 * Represents an implementation that requires access to the shader being used
 * during drawing, for each {@link Object3D} within the scene graph. <br>
 * <br>
 * Used for lighting objects within the scene graph that need access to the
 * current active shader and to be notified whenever a object in the graph is
 * being drawn.
 * 
 */
public interface ILight {

	/**
	 * Called when any {@link Object3D} node in the graph is being drawn. This
	 * method should only set uniforms within the {@link ShaderProgram} object.
	 * 
	 * @param index  the current index from the list of {@link ILight} objects. This
	 *               index is separate for each {@link #getTypeName()} group.
	 * @param shader the active shader.
	 */
	public void apply(int index, ShaderProgram shader);

	/**
	 * Returns a name for the lighting type this implementation represents. (e.g.
	 * pointLights or spotLights). This method is used to provide the correct value
	 * for the index parameter when the {@link #apply(int, ShaderProgram)} method is
	 * called.
	 */
	public String getTypeName();

}