package au.edu.federation.itech3104.michaelwilson.graph;

import java.util.ArrayList;
import java.util.List;

public class RootNode extends Transform implements ILightingTracker {

	private final List<ILight> shaderModifier = new ArrayList<>();

	public RootNode() {

	}

	@Override
	protected void onTreeChanged(Object3D node, boolean added) {
		super.onTreeChanged(node, added);

		if (node instanceof ILight) {
			ILight sm = (ILight) node;
			if (added) {
				shaderModifier.add(sm);
				System.out.println("Added graph deteached node: " + node);
			} else {
				shaderModifier.remove(sm);
				System.out.println("Removed graph deteached node: " + node);
			}
		}
	}

	@Override
	public List<ILight> getLights() {
		return shaderModifier;
	}

}
