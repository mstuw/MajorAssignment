package au.edu.federation.itech3104.michaelwilson.graphics;

import au.edu.federation.itech3104.michaelwilson.graph.Transform;

/**
 * 
 * Represents an implementation that requires access to the shader being used
 * during drawing, for each {@link Transform} within the scene graph. <br>
 * <br>
 * Used for lighting objects within the scene graph that need access to the
 * current active shader and to be notified whenever a object in the graph is
 * being drawn.
 * 
 */
public interface ILight {

	/**
	 * Called when any {@link Transform} node in the graph is being drawn. This
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