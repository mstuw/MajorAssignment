package au.edu.federation.itech3104.michaelwilson.model;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3104.michaelwilson.graph.Transform;
import au.edu.federation.itech3104.michaelwilson.graphics.IDisposable;
import au.edu.federation.itech3104.michaelwilson.graphics.Mesh;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.IDrawable;
import au.edu.federation.itech3104.michaelwilson.graphics.renderer.IDrawableRenderer;

public class Model extends Transform implements IDrawable, IDisposable {

	private final List<Mesh> meshes;

	public Model(List<Mesh> meshes) {
		this.meshes = new ArrayList<>(meshes);
	}

	@Override
	public void draw(IDrawableRenderer renderer) {
		for (Mesh mesh : meshes)
			renderer.render(mesh, getGlobalMatrix());
	}

	@Override
	public Material getMaterial() {
		return null;
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}

	@Override
	public void dispose() {
		for (Mesh mesh : meshes)
			mesh.dispose();
		meshes.clear();
	}

}
