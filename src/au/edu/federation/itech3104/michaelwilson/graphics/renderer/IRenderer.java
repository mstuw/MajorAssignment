package au.edu.federation.itech3104.michaelwilson.graphics.renderer;

import au.edu.federation.itech3104.michaelwilson.camera.Camera;
import au.edu.federation.itech3104.michaelwilson.graph.Transform;
import au.edu.federation.itech3104.michaelwilson.graphics.IDrawable;
import au.edu.federation.itech3104.michaelwilson.math.Mat4f;

public interface IRenderer {

	public void draw(IDrawable drawable, Mat4f modelMatrix);

	public void draw(Transform transform);

	public void setCamera(Camera camera);

}
