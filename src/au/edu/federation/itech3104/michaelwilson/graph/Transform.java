package au.edu.federation.itech3104.michaelwilson.graph;

import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;

/**
 * An empty node that doesn't support drawing. Used to group and move objects
 * together.
 */
public class Transform extends Object3D {

	public Transform() {
	}

	@Override
	public void update(float deltaTime) {

	}

	@Override
	protected void draw() {
	}

	@Override
	public Material getMaterial() {
		return null;
	}

}
