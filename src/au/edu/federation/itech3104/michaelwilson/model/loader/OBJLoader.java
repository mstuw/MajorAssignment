package au.edu.federation.itech3104.michaelwilson.model.loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3104.michaelwilson.math.Vec3f;

// TODO: Add support for multiple sub-meshes (objects?)
// TODO: Add support for using MTL files.
public final class OBJLoader {

	private OBJLoader() {
	}

	/**
	 * Loads a OBJ file from the file system and returns a {@link RawMesh} object.
	 * Currently this method only supports a single mesh/object within the OBJ file.
	 * The OBJ file must use both normals and texture coordinates.
	 */
	public static RawMesh loadMesh(String filepath) throws FileNotFoundException, IOException {
		List<Vert> vertices = new ArrayList<Vert>();
		List<UV> textures = new ArrayList<UV>();
		List<Vec3f> normals = new ArrayList<Vec3f>();

		List<Integer> indices = new ArrayList<Integer>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {

			String line;
			while ((line = reader.readLine()) != null) {

				String[] tokens = line.split(" ");

				if (line.startsWith("v ")) { // vertices
					Vec3f position = new Vec3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
					vertices.add(new Vert(vertices.size(), position));

				} else if (line.startsWith("vt ")) { // textures
					UV uv = new UV(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
					textures.add(uv);

				} else if (line.startsWith("vn ")) { // normals
					Vec3f normal = new Vec3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
					normals.add(normal);

				} else if (line.startsWith("f ")) { // faces
					processFace(tokens, vertices, indices);

				} else if (line.startsWith("mtllib ")) { // Material Template Library file

				} else if (line.startsWith("usemtrl ")) { // Use a mtl definition

				} else if (line.startsWith("o ")) { // named objects

				} else if (line.startsWith("g ")) { // polygon groups

				} else if (line.startsWith("s ")) { // smooth groups

				}

			}

		}

		RawMesh model = new RawMesh();

		// Build our Vertex objects based on the Vert objects.
		for (Vert vertex : vertices) {

			Vec3f position = vertex.getPosition();
			Vec3f normal = normals.get(vertex.getNormalIndex());
			UV texture = textures.get(vertex.getTextureIndex());

			model.vertices.add(new Vertex(position, normal, texture));
		}

		for (Integer index : indices)
			model.indices.add(index);

		return model;
	}

	// f 8/1 7/2 6/3 5/4
	private static void processFace(String[] tokens, List<Vert> vertices, List<Integer> indices) {
		if (tokens.length == 5)
			throw new RuntimeException("Failed to load OBJ file! Faces must be made from triangles and not quads!");

		String[] vertex1 = tokens[1].split("/");
		String[] vertex2 = tokens[2].split("/");
		String[] vertex3 = tokens[3].split("/");

		if (vertex1.length != 3)
			throw new RuntimeException("Failed to load OBJ file! Models must have normals and texture coordinates!");

		if (vertex1.length != vertex2.length || vertex2.length != vertex3.length)
			throw new RuntimeException("Failed to load OBJ file! All face defintions must be consistent!");

		processVertex(vertex1, vertices, indices);
		processVertex(vertex2, vertices, indices);
		processVertex(vertex3, vertices, indices);
	}

	private static void processVertex(String[] vertexData, List<Vert> vertices, List<Integer> indices) {
		int index = Integer.parseInt(vertexData[0]) - 1;
		int textureIndex = Integer.parseInt(vertexData[1]) - 1;
		int normalIndex = Integer.parseInt(vertexData[2]) - 1;

		Vert vertex = vertices.get(index);

		if (!vertex.isSet()) {
			vertex.set(normalIndex, textureIndex);
			indices.add(index);

		} else {
			processSetVertex(vertex, normalIndex, textureIndex, vertices, indices);
		}

	}

	private static void processSetVertex(Vert vertex, int normalIndex, int textureIndex, List<Vert> vertices, List<Integer> indices) {

		// Face reusing the same vertex.
		if (vertex.getNormalIndex() == normalIndex && vertex.getTextureIndex() == textureIndex) {
			indices.add(vertex.getIndex());

		} else { // Face reusing the same vertex, but has a different normal and/or texture.

			// If a vertex has already been duplicated for this position, recursively check
			// it.
			// Otherwise create a new duplicate vertex of this position with the new normal
			// and texture values.
			Vert duplicateVertex = vertex.getDuplicateVertex();
			if (duplicateVertex != null) {
				processSetVertex(duplicateVertex, normalIndex, textureIndex, vertices, indices);

			} else {
				Vert newDuplicateVertex = new Vert(vertices.size(), vertex.getPosition());
				newDuplicateVertex.set(normalIndex, textureIndex);

				vertex.setDuplicateVertex(newDuplicateVertex);

				vertices.add(newDuplicateVertex);
				indices.add(newDuplicateVertex.getIndex());

			}

		}

	}

	private static final class Vert {
		private final int index;

		private final Vec3f position;

		private int normalIndex;
		private int textureIndex; // uv

		private boolean isSet;

		private Vert duplicateVertex; // a vertex that is based on this one and has the same position.

		public Vert(int index, Vec3f position) {
			this.index = index;
			this.position = position;
		}

		public void set(int normalIndex, int textureIndex) {
			this.normalIndex = normalIndex;
			this.textureIndex = textureIndex;
			isSet = true;
		}

		public boolean isSet() {
			return isSet;
		}

		public int getIndex() {
			return index;
		}

		public int getNormalIndex() {
			return normalIndex;
		}

		public int getTextureIndex() {
			return textureIndex;
		}

		public void setDuplicateVertex(Vert duplicateVertex) {
			this.duplicateVertex = duplicateVertex;
		}

		public Vert getDuplicateVertex() {
			return duplicateVertex;
		}

		public Vec3f getPosition() {
			return position;
		}

	}

}
