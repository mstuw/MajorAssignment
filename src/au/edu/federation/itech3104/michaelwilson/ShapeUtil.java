package au.edu.federation.itech3104.michaelwilson;

/**
 * Contains basic 3D shapes.
 */
public final class ShapeUtil {

	// @formatter:off
	private static final float t = 0.618f; // t = (sqrt(5.0) - 1.0) / 2.0
	public static final float[] ICOSAHEDRON_VERTICES = {
				 0.0f,  1.0f,	  t, 	
				 0.0f,  1.0f,	 -t, 	
				 1.0f,     t,  0.0f, 	 
				 1.0f,    -t,  0.0f, 
				 0.0f, -1.0f,    -t, 	
				 0.0f, -1.0f,     t, 	
				    t,  0.0f,  1.0f, 	
				   -t,  0.0f,  1.0f, 	
				    t,  0.0f, -1.0f, 	
				   -t,  0.0f, -1.0f, 	 
				-1.0f,     t,  0.0f, 	
				-1.0f,    -t,  0.0f, 
	};		
	public static final int[] ICOSAHEDRON_INDICES = {
				0,6,2,
				2,6,3,
				3,6,5,
				7,5,6,
				0,7,6,
				8,2,3,
				8,1,2,
				0,2,1,
				10,0,1,
				10,1,9,
				9,1,8,
				8,3,4,
				3,5,4,
				11,4,5,
				11,7,10,
				0,10,7,
				4,11,9,
				4,9,8,
				11,5,7,
				9,11,10
		
	};
	// @formatter:on

	//@formatter:off
	/** 
	 * Count: 36<br>
	 * VBL: 3_3_2 
	 */
	public static final float[] CUBE_VERTICES_NOT_INDEXED = {
			// positions          // normals           // uv
	        -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  0.0f,
	         0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  0.0f,
	         0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  1.0f,
	         0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  1.0f,
	        -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  1.0f,
	        -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  0.0f,

	        -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  0.0f,
	         0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  0.0f,
	         0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  1.0f,
	         0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  1.0f,
	        -0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  1.0f,
	        -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  0.0f,

	        -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  0.0f,
	        -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  1.0f,
	        -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
	        -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
	        -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  0.0f,
	        -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  0.0f,

	         0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  0.0f,
	         0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  1.0f,
	         0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
	         0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
	         0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  0.0f,
	         0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  0.0f,

	        -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  1.0f,
	         0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  1.0f,
	         0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  0.0f,
	         0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  0.0f,
	        -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  0.0f,
	        -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  1.0f,

	        -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  1.0f,
	         0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  1.0f,
	         0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  0.0f,
	         0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  0.0f,
	        -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  0.0f,
	        -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  1.0f
	};
	//@formatter:on

	// @formatter:off
	public static float[] getPlaneVertices(int tileU, int tileV) {
		return new float[] {
			// positions          // normals         // uvs
			 0.5f,  0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  1.0f * tileU, 1.0f * tileV,
	         0.5f,  0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  1.0f * tileU, 0.0f * tileV,
	        -0.5f,  0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f * tileU, 0.0f * tileV, 
	        -0.5f,  0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  0.0f * tileU, 1.0f * tileV
		};
	}
	
	public static final int[] PLANE_INDICES = {
			0, 1, 3, 
		    1, 2, 3  
	};
	//@formatter:on

	private ShapeUtil() {
	}

}
