package au.edu.federation.itech3104.michaelwilson.model.loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.federation.itech3104.michaelwilson.ResourceManager;
import au.edu.federation.itech3104.michaelwilson.graphics.Mesh;
import au.edu.federation.itech3104.michaelwilson.graphics.data.BufferUsageHint;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;
import au.edu.federation.itech3104.michaelwilson.model.Model;

public final class RawModel {

	private final Map<String, RawMesh> meshes = new HashMap<>();
	private final Map<Integer, String> indexLookup = new HashMap<>();

	public RawModel(List<RawMesh> meshes) {
		int i = 0;
		for (RawMesh rawMesh : meshes) {
			this.meshes.put(rawMesh.getName(), rawMesh);
			indexLookup.put(i++, rawMesh.getName());
		}
	}

	public List<Mesh> toMeshes(ResourceManager resourceManager, Material defaultMaterial, BufferUsageHint usageHint) {
		List<Mesh> meshList = new ArrayList<>();

		for (int i = 0; i < getMeshCount(); i++) {
			RawMesh rawMesh = getRawMesh(i);
			Material material = resourceManager.getMaterial(rawMesh.getMaterialName());

			if (material == null) {
				System.out.println(
						"Failed to find material '" + rawMesh.getMaterialName() + "' for model mesh '" + rawMesh.getName() + "' Using default...");
				material = defaultMaterial;
			}

			meshList.add(rawMesh.toMesh(material, usageHint));
		}

		return meshList;
	}

	public Collection<RawMesh> getRawMeshes() {
		return meshes.values();
	}

	public RawMesh getRawMesh(String name) {
		return meshes.get(name);
	}

	public RawMesh getRawMesh(int index) {
		return getRawMesh(indexLookup.get(index));
	}

	public int getMeshCount() {
		return meshes.size();
	}

}
