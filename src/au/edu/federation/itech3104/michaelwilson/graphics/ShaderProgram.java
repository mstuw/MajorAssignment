package au.edu.federation.itech3104.michaelwilson.graphics;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform3i;
import static org.lwjgl.opengl.GL20.glUniformMatrix3;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import au.edu.federation.itech3104.michaelwilson.camera.Camera;
import au.edu.federation.itech3104.michaelwilson.math.Mat3f;
import au.edu.federation.itech3104.michaelwilson.math.Mat4f;
import au.edu.federation.itech3104.michaelwilson.math.Vec3f;
import au.edu.federation.itech3104.michaelwilson.math.Vec3i;

public class ShaderProgram implements IBindable, IDisposable {
	private static final boolean DEBUG = false;

	// It might be better to make these static, reducing memory used per shader
	// object,
	private final FloatBuffer matrixBuffer4x4 = BufferUtil.createEmptyFloatBuffer(4 * 4); // 4x4 matrix
	private final FloatBuffer matrixBuffer3x3 = BufferUtil.createEmptyFloatBuffer(3 * 3); // 3x3 matrix

	private final int id;
	private boolean isDisposed;

	private final Map<String, Integer> cachedUniformLocations = new HashMap<String, Integer>();

	public ShaderProgram(String vertexSource, String fragmentSource) {
		id = glCreateProgram();
		if (id == 0)
			throw new RuntimeException("Failed to create shader program!");

		int vertexShaderId = compileShader(ShaderType.VERTEX_SHADER, vertexSource);
		int fragmentShaderId = compileShader(ShaderType.FRAGMENT_SHADER, fragmentSource);

		// Attach shaders to shader program.
		glAttachShader(id, vertexShaderId);
		glAttachShader(id, fragmentShaderId);

		// Link shaders to shader program.
		glLinkProgram(id);
		if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) // Check linking status
			throw new RuntimeException("Could not link shader program: " + glGetProgramInfoLog(id, 1000));

		// Delete/detach shaders as we no longer need them.
		glDetachShader(id, vertexShaderId);
		glDetachShader(id, fragmentShaderId);
		glDeleteShader(vertexShaderId);
		glDeleteShader(fragmentShaderId);

		// Validate shader program.
		glValidateProgram(id);
		if (glGetProgrami(id, GL_VALIDATE_STATUS) == GL_FALSE)
			throw new RuntimeException("Could not validate shader program: " + glGetProgramInfoLog(id, 1000));
	}

	// Helper method for loading shader programs from file.
	public static ShaderProgram fromFile(String vertexShaderFilename, String fragmentShaderFilename) throws IOException {
		String vertexShader = new String(Files.readAllBytes(Paths.get(vertexShaderFilename)));
		String fragmentShader = new String(Files.readAllBytes(Paths.get(fragmentShaderFilename)));

		return new ShaderProgram(vertexShader, fragmentShader);
	}

	// Compile the shader source code and return the shader id.
	private static int compileShader(ShaderType type, String shaderContent) {
		int id = glCreateShader(type.getId());
		if (id == 0)
			throw new RuntimeException("Failed to create shader!");

		glShaderSource(id, shaderContent); // link source code to shader.
		glCompileShader(id); // compile shader source code

		// Check if shader compiled successfully.
		if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE)
			throw new RuntimeException("Failed to compile shader: " + glGetShaderInfoLog(id, 1000));

		return id;
	}

	@Override
	public void bind() {
		if (isDisposed)
			return;
		glUseProgram(id);
	}

	@Override
	public void unbind() {
		glUseProgram(0);
	}

	public void setUniform(String name, int value) {
		int location = getUniformLocation(name);
		glUniform1i(location, value);

		if (DEBUG)
			System.out.println(String.format("%s = %d", name, value));
	}

	public void setUniform(String name, float value) {
		int location = getUniformLocation(name);
		glUniform1f(location, value);

		if (DEBUG)
			System.out.println(String.format("%s = %.2f", name, value));
	}

	public void setUniform(String name, boolean value) {
		int location = getUniformLocation(name);
		glUniform1i(location, value ? 1 : 0);

		if (DEBUG)
			System.out.println(String.format("%s = %s", name, value + ""));
	}

	public void setUniform(String name, Vec3f value) {
		setUniform(name, value.x, value.y, value.z);
	}

	public void setUniform(String name, float x, float y, float z) {
		int location = getUniformLocation(name);
		glUniform3f(location, x, y, z);

		if (DEBUG)
			System.out.println(String.format("%s = [%.2f,%.2f,%.2f]", name, x, y, z));
	}

	public void setUniform(String name, Vec3i value) {
		int location = getUniformLocation(name);
		glUniform3i(location, value.x, value.y, value.z);

		if (DEBUG)
			System.out.println(String.format("%s = [%d,%d,%d]", name, value.x, value.y, value.z));
	}

	public void setUniform(String name, Mat4f value) {
		int location = getUniformLocation(name);

		matrixBuffer4x4.put(value.toArray());
		matrixBuffer4x4.flip();

		glUniformMatrix4(location, false, matrixBuffer4x4);

		if (DEBUG)
			System.out.println(String.format("%s = (mat4)", name));
	}

	public void setUniform(String name, Mat3f value) {
		int location = getUniformLocation(name);

		matrixBuffer3x3.put(value.toArray());
		matrixBuffer3x3.flip();

		glUniformMatrix3(location, false, matrixBuffer3x3);

		if (DEBUG)
			System.out.println(String.format("%s = (mat3)", name));
	}

	/**
	 * Set the uniforms for the camera position, projection and view matrices.
	 */
	public void setUniforms(Camera camera) {
		setUniform("projection", camera.getProjectionMatrix());
		setUniform("view", camera.getViewMatrix());
		setUniform("viewPos", camera.getPosition());
	}

	/**
	 * Return the cached uniform location or get uniform location if it wasn't
	 * already cached.
	 */
	public int getUniformLocation(String uniformName) {
		if (!cachedUniformLocations.containsKey(uniformName)) {

			int location = glGetUniformLocation(id, uniformName);
			if (location == -1)
				System.err.println("Uniform '" + uniformName + "' doesn't exist!");

			// Cache uniform, since glGetUniformLocation isn't cheap, and we could be
			// setting uniforms every render/update.
			cachedUniformLocations.put(uniformName, location);

			return location;
		}

		return cachedUniformLocations.get(uniformName);
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void dispose() {
		if (isDisposed)
			return;

		System.out.println("Disposing ShaderProgram...");

		unbind();
		glDeleteProgram(id);

		isDisposed = true;
	}

	public static enum ShaderType {
		VERTEX_SHADER(GL_VERTEX_SHADER), FRAGMENT_SHADER(GL_FRAGMENT_SHADER);

		private final int id;

		ShaderType(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

	}

}