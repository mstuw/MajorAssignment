package au.edu.federation.itech3104.michaelwilson.graphics.data;

import java.util.ArrayList;
import java.util.List;

// Represents the structure of the Vertex Buffer Object (defines how glVertexAttribPointer() should be configured)
public final class VertexBufferLayout {

	// Pre-made constants for common layouts.
	public static final VertexBufferLayout Float3 = new VertexBufferLayout().add(VertexAttributeType.FLOAT, 3);
	public static final VertexBufferLayout Float3_3 = new VertexBufferLayout().add(VertexAttributeType.FLOAT, 3)
			.add(VertexAttributeType.FLOAT, 3);
	public static final VertexBufferLayout Float3_2 = new VertexBufferLayout().add(VertexAttributeType.FLOAT, 3)
			.add(VertexAttributeType.FLOAT, 2);
	public static final VertexBufferLayout Float3_3_2 = new VertexBufferLayout().add(VertexAttributeType.FLOAT, 3)
			.add(VertexAttributeType.FLOAT, 3).add(VertexAttributeType.FLOAT, 2);

	private final List<VertexAttribute> attributes = new ArrayList<VertexAttribute>();

	private int stride;

	public VertexBufferLayout() {
	}

	public VertexBufferLayout add(VertexAttributeType type, int count) {
		attributes.add(new VertexAttribute(type, count));

		stride += count * type.getBytes();

		return this;
	}

	public List<VertexAttribute> getAttributes() {
		return new ArrayList<>(attributes);
	}

	public int getStride() {
		return stride;
	}

	public static class VertexAttribute {
		private final VertexAttributeType type;
		private final int count;

		public VertexAttribute(VertexAttributeType type, int count) {
			super();
			this.type = type;
			this.count = count;
		}

		public VertexAttributeType getType() {
			return type;
		}

		public int getCount() {
			return count;
		}

	}

}