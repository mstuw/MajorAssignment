package au.edu.federation.itech3104.michaelwilson.math;


import java.text.DecimalFormat;
import java.util.Random;

// Original source from Utils.java provided by lecture resources.
public class UtilMath {
	public static final float DEGS_TO_RADS = (float) Math.PI / 180.0f;
	public static final float RADS_TO_DEGS = 180.0f / (float) Math.PI;

	public static final Vec3f X_AXIS = new Vec3f(1.0f, 0.0f, 0.0f);
	public static final Vec3f Y_AXIS = new Vec3f(0.0f, 1.0f, 0.0f);
	public static final Vec3f Z_AXIS = new Vec3f(0.0f, 0.0f, 1.0f);

	// Define a private static DecimalFormat to be used by our toString() method.
	// Note: '0' means put a 0 there if it's zero, '#' means omit if zero.
	public static DecimalFormat df = new DecimalFormat("0.000");

	public static String newLine = System.lineSeparator();

	/**
	 * A Random object used to generate random numbers in the randRange methods.
	 * 
	 * @see #randRange(float, float)
	 * @see #randRange(int, int)
	 */
	public static final Random random = new Random();

	/**
	 * Return a random floating point value between the half-open range [min..max).
	 * <p>
	 * This means that, for example, a call to
	 * {@code randRange(-5.0f, 5.0f) may return a value between -5.0f up to a maximum of 4.999999f. @param
	 * min The minimum value
	 * 
	 * @param max The maximum value
	 * @return A random float within the range specified.
	 */
	public static float randRange(float min, float max) {
		return random.nextFloat() * (max - min) + min;
	}

	/**
	 * Return a random integer value between the half-open range (min..max]
	 * <p>
	 * This means that, for example, a call to
	 * {@code randRange(-5, 5) will return a value between -5 up to a maximum of +5. @param
	 * min The minimum value
	 * 
	 * @param max The maximum value
	 * @return A random int within the range specified.
	 */
	public static int randRange(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}

	/**
	 * Return the co-tangent of an angle specified in radians.
	 * <p>
	 * The co-tangent is simply 1.0f / tan(angleRads).
	 * 
	 * @param angleRads The angle specified in radians to return the co-tangent of.
	 * @return The co-tangent of the specified angle.
	 */
	public static float cot(float angleRads) {
		return (float) (1.0f / Math.tan(angleRads));
	}

	/**
	 * Convert an angle specified in radians into degrees and return it.
	 * 
	 * @param angleRads The angle to convert specified in radians.
	 * @return The converted angle in degrees.
	 */
	public static float radiansToDegrees(float angleRads) {
		return angleRads * RADS_TO_DEGS;
	}

	/**
	 * Convert an angle specified in degrees into radians and return it.
	 * 
	 * @param angleDegs The angle to convert specified in degrees.
	 * @return The converted angle in radians.
	 */
	public static float degreesToRadians(float degrees) {
		return degrees * DEGS_TO_RADS;
	}

} // End of Utils class
