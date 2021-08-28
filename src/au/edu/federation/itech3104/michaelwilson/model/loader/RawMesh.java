package au.edu.federation.itech3104.michaelwilson.model.loader;

import java.util.ArrayList;
import java.util.List;

public final class RawMesh {
	public final List<Vertex> vertices = new ArrayList<>();
	public final List<Integer> indices = new ArrayList<>();

	protected RawMesh() {
	}

	public int[] getIndices() {
		int[] arr = new int[indices.size()];

		for (int i = 0; i < arr.length; i++)
			arr[i] = indices.get(i);

		return arr;
	}

	public float[] getVertices() {

		float[] arr = new float[vertices.size() * 8]; // vec3, vec3, vec2

		for (int i = 0; i < vertices.size(); i++) {
			Vertex vertex = vertices.get(i);

			arr[i * 8 + 0] = vertex.position.x;
			arr[i * 8 + 1] = vertex.position.y;
			arr[i * 8 + 2] = vertex.position.z;

			arr[i * 8 + 3] = vertex.normal.x;
			arr[i * 8 + 4] = vertex.normal.y;
			arr[i * 8 + 5] = vertex.normal.z;

			arr[i * 8 + 6] = vertex.uv.x;
			arr[i * 8 + 7] = vertex.uv.y;

		}

		return arr;

	}

}