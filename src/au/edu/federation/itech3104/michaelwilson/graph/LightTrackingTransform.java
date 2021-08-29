package au.edu.federation.itech3104.michaelwilson.graph;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3104.michaelwilson.graphics.ILight;
import au.edu.federation.itech3104.michaelwilson.graphics.ILightingTracker;

/**
 * This class acts like a normal {@link Transform} object, but also keeps track
 * of any {@link ILight} nodes (aka. Lighting Objects) added the the tree.
 * 
 * @see #getLights()
 */
public class LightTrackingTransform extends Transform implements ILightingTracker {

	private final List<ILight> lights = new ArrayList<>();

	public LightTrackingTransform() {

	}

	@Override
	protected void onTreeChanged(Transform node, boolean added) {
		super.onTreeChanged(node, added);

		if (node instanceof ILight) {
			ILight light = (ILight) node;
			if (added) {
				lights.add(light);
				System.out.println("Added tree deteached node: " + node);
			} else {
				lights.remove(light);
				System.out.println("Removed tree deteached node: " + node);
			}
		}
	}

	@Override
	public List<ILight> getLights() {
		return lights;
	}

}
