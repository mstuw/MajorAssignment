package au.edu.federation.itech3104.michaelwilson.graphics.texture;

import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;

public enum WrapMode {

	REPEAT(GL_REPEAT), MIRRORED_REPEAT(GL_MIRRORED_REPEAT), CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE), CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER);

	private final int mode;

	WrapMode(int mode) {
		this.mode = mode;
	}

	public int getMode() {
		return mode;
	}

}