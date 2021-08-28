package au.edu.federation.itech3104.michaelwilson.graphics.data;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;

public enum BufferUsageHint {

	//@formatter:off
	
	STATIC_DRAW(GL_STATIC_DRAW), 
	DYNAMIC_DRAW(GL_DYNAMIC_DRAW),
	STREAM_DRAW(GL_STREAM_DRAW);
	
	//@formatter:on

	private final int hint;

	BufferUsageHint(int hint) {
		this.hint = hint;
	}

	public int getHint() {
		return hint;
	}

}