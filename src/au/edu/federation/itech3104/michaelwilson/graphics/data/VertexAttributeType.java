package au.edu.federation.itech3104.michaelwilson.graphics.data;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;

public enum VertexAttributeType {

	FLOAT(GL_FLOAT, Float.BYTES), BYTE(GL_UNSIGNED_BYTE, Byte.BYTES);

	private final int id;
	private final int bytes;

	VertexAttributeType(int id, int bytes) {
		this.id = id;
		this.bytes = bytes;
	}

	public int getId() {
		return id;
	}

	public int getBytes() {
		return bytes;
	}

}