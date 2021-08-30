package au.edu.federation.itech3104.michaelwilson.model;

import au.edu.federation.itech3104.michaelwilson.graph.Transform;
import au.edu.federation.itech3104.michaelwilson.graphics.IDisposable;
import au.edu.federation.itech3104.michaelwilson.graphics.Mesh;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.IDrawable;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.IDrawableRenderer;

/**
 * This class represents a single {@link Mesh} object within a scene graph. It
 * extends the {@link Transform} class allowing it to have ancestors and
 * descendants.
 * 
 * @see Transform
 * @see IDrawable
 */
public class ModelMesh extends Transform implements IDrawable, IDisposable {

	private final Mesh mesh;

	public ModelMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	@Override
	public void draw(IDrawableRenderer renderer) {
		mesh.draw(renderer);
	}

	/**
	 * Returns the {@link Material} of the {@link Mesh}.
	 */
	@Override
	public Material getMaterial() {
		return mesh.getMaterial();
	}

	@Override
	public void dispose() {
		mesh.dispose();
	}

	/**
	 * Returns the {@link Mesh} of this {@link ModelMesh}.
	 */
	public Mesh getMesh() {
		return mesh;
	}

}
