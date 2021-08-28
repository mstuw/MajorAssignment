package au.edu.federation.itech3104.michaelwilson.graphics.data;

import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.List;

import au.edu.federation.itech3104.michaelwilson.graphics.IBindable;
import au.edu.federation.itech3104.michaelwilson.graphics.IDisposable;
import au.edu.federation.itech3104.michaelwilson.graphics.data.VertexBufferLayout.VertexAttribute;

public final class VertexArray implements IBindable, IDisposable {

	private final int id;
	private boolean isDisposed;

	public VertexArray() {
		id = glGenVertexArrays();
	}

	/**
	 * Associate the provided vertex buffer object with this VAO.
	 */
	public void link(VertexBuffer vbo) {
		link(vbo, null);
	}

	/**
	 * Associate the provided vertex buffer object and element buffer object with
	 * this VAO.
	 * 
	 * @param vbo
	 * @param ebo optional, can be null.
	 */
	public void link(VertexBuffer vbo, ElementBuffer ebo) {
		bind();
		vbo.bind();
		if (ebo != null)
			ebo.bind();

		updateVao(vbo.getLayout());

		unbind();
		vbo.unbind();
		if (ebo != null)
			ebo.unbind();
	}

	private void updateVao(VertexBufferLayout layout) {
		
		// Setup vertex attribute pointers for OpenGL.
		
		List<VertexAttribute> attributes = layout.getAttributes(); // getAttributes returns a new object each time.

		int offset = 0;

		for (int index = 0; index < attributes.size(); index++) {
			VertexAttribute attribute = attributes.get(index);

			glVertexAttribPointer(index, attribute.getCount(), attribute.getType().getId(), false, layout.getStride(),
					offset);
			glEnableVertexAttribArray(index);

			offset += attribute.getCount() * attribute.getType().getBytes();
		}
	}

	@Override
	public void bind() {
		if (isDisposed)
			return;
		glBindVertexArray(id);
	}

	@Override
	public void unbind() {
		glBindVertexArray(0);
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void dispose() {
		if (isDisposed)
			return;
		
		System.out.println("Disposing VAO...");
		
		unbind();
		glDeleteVertexArrays(id);

		isDisposed = true;
	}

}