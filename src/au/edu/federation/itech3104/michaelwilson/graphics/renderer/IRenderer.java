package au.edu.federation.itech3104.michaelwilson.graphics.renderer;

import au.edu.federation.itech3104.michaelwilson.camera.Camera;
import au.edu.federation.itech3104.michaelwilson.graph.Transform;

public interface IRenderer extends IDrawableRenderer {

	public void renderTree(Transform transform);

	public void setCamera(Camera camera);

}
