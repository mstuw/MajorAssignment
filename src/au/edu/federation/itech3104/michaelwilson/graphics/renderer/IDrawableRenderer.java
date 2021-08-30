package au.edu.federation.itech3104.michaelwilson.graphics.renderer;

import au.edu.federation.itech3104.michaelwilson.math.Mat4f;

public interface IDrawableRenderer {

	void render(IDrawable drawable, Mat4f modelMatrix);

}