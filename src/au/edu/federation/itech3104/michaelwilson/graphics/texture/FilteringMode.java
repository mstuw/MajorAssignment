package au.edu.federation.itech3104.michaelwilson.graphics.texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;

public enum FilteringMode {

	NEAREST(GL_NEAREST), LINEAR(GL_LINEAR),

	NEAREST_MIPMAP_NEAREST(GL_NEAREST_MIPMAP_NEAREST), LINEAR_MIPMAP_LINEAR(GL_LINEAR_MIPMAP_LINEAR), NEAREST_MIPMAP_LINEAR(GL_NEAREST_MIPMAP_LINEAR),
	LINEAR_MIPMAP_NEAREST(GL_LINEAR_MIPMAP_NEAREST);

	private final int mode;

	FilteringMode(int mode) {
		this.mode = mode;
	}

	public int getMode() {
		return mode;
	}

}