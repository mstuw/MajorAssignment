package au.edu.federation.itech3104.michaelwilson.model.loader;

import au.edu.federation.itech3104.michaelwilson.math.Vec3f;

public final class Vertex {
	public final Vec3f position;
	public final Vec3f normal;
	public final UV uv;

	protected Vertex(Vec3f position, Vec3f normal, UV uv) {
		this.position = position;
		this.normal = normal;
		this.uv = uv;
	}
}