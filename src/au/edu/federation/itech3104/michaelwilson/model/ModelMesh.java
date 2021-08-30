package au.edu.federation.itech3104.michaelwilson.model;

import au.edu.federation.itech3104.michaelwilson.graph.Transform;
import au.edu.federation.itech3104.michaelwilson.graphics.IDisposable;
import au.edu.federation.itech3104.michaelwilson.graphics.Mesh;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.IDrawable;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.IDrawableRenderer;

public class ModelMesh extends Transform implements IDrawable, IDisposable {

	private final Mesh mesh;

	public ModelMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	@Override
	public void draw(IDrawableRenderer renderer) {
		mesh.draw(renderer);
	}

	@Override
	public Material getMaterial() {
		return mesh.getMaterial();
	}

	@Override
	public void dispose() {
		mesh.dispose();
	}

}
