package au.edu.federation.itech3104.michaelwilson.graphics.data;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.FloatBuffer;

import au.edu.federation.itech3104.michaelwilson.graphics.IBindable;
import au.edu.federation.itech3104.michaelwilson.graphics.BufferUtil;
import au.edu.federation.itech3104.michaelwilson.graphics.IDisposable;

public final class VertexBuffer implements IBindable, IDisposable {

	private final int id;
	private boolean isDisposed;

	private final VertexBufferLayout layout;

	public VertexBuffer(float[] data, BufferUsageHint usageHint, VertexBufferLayout layout) {
		this(BufferUtil.createFloatBuffer(data), usageHint, layout);
	}

	public VertexBuffer(FloatBuffer buffer, BufferUsageHint usageHint, VertexBufferLayout layout) {
		this.layout = layout;

		id = glGenBuffers();

		bind();
		glBufferData(GL_ARRAY_BUFFER, buffer, usageHint.getHint()); // Set VBO data.
		unbind();
	}

	@Override
	public void bind() {
		if (isDisposed)
			return;
		glBindBuffer(GL_ARRAY_BUFFER, id);
	}

	@Override
	public void unbind() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	@Override
	public int getId() {
		return id;
	}

	public VertexBufferLayout getLayout() {
		return layout;
	}

	@Override
	public void dispose() {
		if (isDisposed)
			return;
		
		System.out.println("Disposing VBO...");
		
		unbind();
		glDeleteBuffers(id);

		isDisposed = true;
	}

}