package au.edu.federation.itech3104.michaelwilson.model.loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3104.michaelwilson.math.Vec3f;

// OBJ File format specification referenced from
// - https://en.wikipedia.org/wiki/Wavefront_.obj_file
// - https://all3dp.com/1/obj-file-format-3d-printing-cad/#the-obj-file-format-specification-simplified

// TODO: Add support for using MTL files.
public final class OBJLoader {
	public static final OBJLoader INSTANCE = new OBJLoader();

	private String currentMaterialName = null;
	private String currentObjectName = null;

	private final List<Vert> vertices = new ArrayList<>();
	private final List<UV> textures = new ArrayList<>();
	private final List<Vec3f> normals = new ArrayList<>();

	private final List<Integer> indices = new ArrayList<>();

	public OBJLoader() {
	}

	/**
	 * Loads a OBJ file from the file system and returns a {@link RawMesh} object.
	 * Currently this method only supports a single mesh/object within the OBJ file.
	 * The OBJ file must use both normals and texture coordinates.
	 */
	public RawModel loadModel(String filepath) throws FileNotFoundException, IOException {
		List<RawMesh> meshes = new ArrayList<>();

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
					processFace(tokens);

				} else if (line.startsWith("mtllib ")) { // Material Template Library file
					// Not supported yet.

				} else if (line.startsWith("usemtl ")) { // Use a mtl definition
					// usemtl doesn't work like expected for OBJ files.
					// Currently the usemtl command will only work for each object and not
					// individual f face commands.
					String name = line.substring(7).trim();
					currentMaterialName = name;

				} else if (line.startsWith("o ")) { // named objects
					String name = line.substring(2).trim();

					if (currentObjectName != null) // Switching object names, build the current mesh.
						meshes.add(buildMesh());

					currentObjectName = name;

				} else if (line.startsWith("g ")) { // polygon groups
					// Not supported yet.
				} else if (line.startsWith("s ")) { // smooth groups
					// Not supported yet.
				}
			}
		}

		meshes.add(buildMesh());

		System.out.println("##### OBJ Load Summary #####");
		for (RawMesh m : meshes) {
			System.out.println("- Submesh: " + m.getName());
			System.out.println("  - material: " + m.getMaterialName());
			System.out.println("  - vertices: " + m.getVertices().size());
			System.out.println("  - indices:  " + m.getIndices().size() + "\n");
		}

		vertices.clear();
		textures.clear();
		normals.clear();
		indices.clear();

		currentMaterialName = null;
		currentObjectName = null;

		return new RawModel(meshes);
	}

	private RawMesh buildMesh() {
		RawMesh model = new RawMesh(currentObjectName, currentMaterialName);

		// Build our Vertex objects based on the Vert objects.
		for (Vert vertex : vertices) {
			Vec3f position = vertex.getPosition();
			Vec3f normal = normals.get(vertex.getNormalIndex());
			UV texture = textures.get(vertex.getTextureIndex());

			model.getVertices().add(new Vertex(position, normal, texture));
		}

		for (Integer index : indices)
			model.getIndices().add(index);

		// clear indices for next sub-mesh.
		indices.clear();

		return model;
	}

	// f 8/1/1 7/2/3 6/3/1
	private void processFace(String[] tokens) {
		if (tokens.length > 4)
			throw new RuntimeException("Failed to load OBJ file! Faces must be made from triangles!");

		String[] vertex1 = tokens[1].trim().split("/");
		String[] vertex2 = tokens[2].trim().split("/");
		String[] vertex3 = tokens[3].trim().split("/");

		if (vertex1.length != 3)
			throw new RuntimeException("Failed to load OBJ file! Models must have normals and texture coordinates!");

		if (vertex1.length != vertex2.length || vertex2.length != vertex3.length)
			throw new RuntimeException("Failed to load OBJ file! All face defintions must be consistent!");

		processVertex(vertex1);
		processVertex(vertex2);
		processVertex(vertex3);
	}

	private void processVertex(String[] vertexData) {
		int index = Integer.parseInt(vertexData[0].trim()) - 1;
		int textureIndex = Integer.parseInt(vertexData[1].trim()) - 1;
		int normalIndex = Integer.parseInt(vertexData[2].trim()) - 1;

		Vert vertex = vertices.get(index);

		if (!vertex.isSet()) {
			vertex.set(normalIndex, textureIndex);
			indices.add(index);

		} else { // the vertex is already set, reuse the same vertex position, normals and textures might be different.
			processSetVertex(vertex, normalIndex, textureIndex);
		}

	}

	private void processSetVertex(Vert vertex, int normalIndex, int textureIndex) {

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
				processSetVertex(duplicateVertex, normalIndex, textureIndex);

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
		private final int index; // the index in the vertices list.

		private final Vec3f position;

		private int normalIndex;
		private int textureIndex; // uv

		private boolean isSet; // true if this vert has been used in a face already.

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
