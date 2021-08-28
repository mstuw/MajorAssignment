package au.edu.federation.itech3104.michaelwilson.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

// Most methods originally from Utils.java from lecture material.
public final class BufferUtil {

	public static ByteBuffer createEmptyByteBuffer(int count) {
		return ByteBuffer.allocateDirect(count * Byte.BYTES).order(ByteOrder.nativeOrder());
	}

	public static IntBuffer createEmptyIntBuffer(int count) {
		return ByteBuffer.allocateDirect(count * Integer.BYTES).order(ByteOrder.nativeOrder()).asIntBuffer();
	}

	public static FloatBuffer createEmptyFloatBuffer(int count) {
		return ByteBuffer.allocateDirect(count * Float.BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
	}

	public static DoubleBuffer createEmptyDoubleBuffer(int count) {
		return ByteBuffer.allocateDirect(count * Double.BYTES).order(ByteOrder.nativeOrder()).asDoubleBuffer();
	}

	public static IntBuffer createIntBuffer(int[] data) {
		return createEmptyIntBuffer(data.length).put(data).flip();
	}

	public static ByteBuffer createByteBuffer(byte[] data) {
		return createEmptyByteBuffer(data.length).put(data).flip();
	}

	public static FloatBuffer createFloatBuffer(float[] data) {
		return createEmptyFloatBuffer(data.length).put(data).flip();
	}

}