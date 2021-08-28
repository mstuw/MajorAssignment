package au.edu.federation.itech3104.michaelwilson.graphics.texture;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import au.edu.federation.itech3104.michaelwilson.graphics.IBindable;
import au.edu.federation.itech3104.michaelwilson.graphics.BufferUtil;
import au.edu.federation.itech3104.michaelwilson.graphics.IDisposable;
import de.matthiasmann.twl.utils.PNGDecoder;

public class Texture2D implements IBindable, IDisposable {

	private final int id;
	private boolean isDisposed;

	public Texture2D(String filepath) throws FileNotFoundException, IOException {
		id = glGenTextures();

		bind();

		try (InputStream is = new FileInputStream(filepath)) {
			PNGDecoder decoder = new PNGDecoder(is);

			// Load image into byte buffer.
			ByteBuffer buffer = BufferUtil.createEmptyByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
			// Decode flipped to account for zero of y-axis being at the bottom instead of
			// the top.
			decoder.decodeFlipped(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
			buffer.flip();

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		}

		setWrapS(WrapMode.REPEAT);
		setWrapT(WrapMode.REPEAT);

		setMinFilter(FilteringMode.LINEAR);
		setMagFilter(FilteringMode.LINEAR);

		glGenerateMipmap(GL_TEXTURE_2D);

		unbind();
	}

	public void bind(int unit) {
		if (unit > 31)
			throw new IllegalArgumentException("Texture unit must be less than 32");

		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void unbind(int unit) {
		if (unit > 31)
			throw new IllegalArgumentException("Texture unit must be less than 32");

		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	/**
	 * Bind this texture to the GL_TEXTURE0 unit.
	 */
	@Override
	public void bind() {
		bind(0);
	}

	/**
	 * Unbind texture unit GL_TEXTURE0 .
	 */
	@Override
	public void unbind() {
		unbind(0);
	}

	public Texture2D setWrapS(WrapMode mode) {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, mode.getMode());
		return this;
	}

	public Texture2D setWrapT(WrapMode mode) {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, mode.getMode());
		return this;
	}

	public Texture2D setMinFilter(FilteringMode mode) {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, mode.getMode());
		return this;
	}

	public Texture2D setMagFilter(FilteringMode mode) {
		if (mode != FilteringMode.LINEAR && mode != FilteringMode.NEAREST)
			throw new IllegalArgumentException("Magnification filter is incompatible with mipmap options!");

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mode.getMode());

		return this;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void dispose() {
		if (isDisposed)
			return;

		System.out.println("Disposing Texture2D...");

		unbind();
		glDeleteTextures(id);

		isDisposed = true;
	}

}
