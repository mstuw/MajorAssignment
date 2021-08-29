package au.edu.federation.itech3104.michaelwilson.graphics;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;

import au.edu.federation.itech3104.michaelwilson.graphics.data.BufferUsageHint;
import au.edu.federation.itech3104.michaelwilson.graphics.data.ElementBuffer;
import au.edu.federation.itech3104.michaelwilson.graphics.data.VertexArray;
import au.edu.federation.itech3104.michaelwilson.graphics.data.VertexBuffer;
import au.edu.federation.itech3104.michaelwilson.graphics.data.VertexBufferLayout;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;

public class Mesh implements IDisposable, IDrawable {

	public final VertexArray vao;
	private final VertexBuffer vbo;
	private ElementBuffer ebo;

	private final int count;
	private final boolean isIndexed;

	private final Material material;

	public Mesh(float[] vertices, int count, BufferUsageHint usageHint, VertexBufferLayout layout, Material material) {
		this.material = material;
		this.count = count;
		this.isIndexed = false;

		vbo = new VertexBuffer(vertices, usageHint, layout);

		vao = new VertexArray();
		vao.link(vbo);
	}

	public Mesh(float[] vertices, int[] indices, BufferUsageHint usageHint, VertexBufferLayout layout, Material material) {
		this.material = material;
		this.count = indices.length;
		this.isIndexed = true;

		vbo = new VertexBuffer(vertices, usageHint, layout);
		ebo = new ElementBuffer(indices, usageHint);

		vao = new VertexArray();
		vao.link(vbo, ebo);
	}

	/**
	 * Draw mesh.
	 */
	@Override
	public void draw() {
		vao.bind();
		{
			if (isIndexed) {
				glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
			} else {
				glDrawArrays(GL_TRIANGLES, 0, count);
			}
		}
		vao.unbind();
	}

	public void dispose() {
		System.out.println("Disposing mesh...");

		vao.dispose();
		vbo.dispose();

		if (ebo != null)
			ebo.dispose();
	}

	@Override
	public Material getMaterial() {
		return material;
	}

}
