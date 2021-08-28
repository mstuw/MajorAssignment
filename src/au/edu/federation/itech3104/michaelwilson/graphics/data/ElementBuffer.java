package au.edu.federation.itech3104.michaelwilson.graphics.data;

import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.IntBuffer;

import au.edu.federation.itech3104.michaelwilson.graphics.IBindable;
import au.edu.federation.itech3104.michaelwilson.graphics.BufferUtil;
import au.edu.federation.itech3104.michaelwilson.graphics.IDisposable;

public final class ElementBuffer implements IBindable, IDisposable {

	private final int id;
	private boolean isDisposed;
	
	public ElementBuffer(int[] data, BufferUsageHint usageHint) {
		this(BufferUtil.createIntBuffer(data), usageHint);
	}

	public ElementBuffer(IntBuffer buffer, BufferUsageHint usageHint) {
		id = glGenBuffers();

		bind();
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, usageHint.getHint()); // Set EBO data.
		unbind();
	}

	@Override
	public void bind() {
		if (isDisposed)
			return;
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
	}

	@Override
	public void unbind() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void dispose() {
		if (isDisposed)
			return;

		System.out.println("Disposing EBO...");
				
		unbind();
		glDeleteBuffers(id);

		isDisposed = true;
	}

}