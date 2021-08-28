package au.edu.federation.itech3104.michaelwilson.graph;

import java.util.Deque;
import au.edu.federation.itech3104.michaelwilson.graphics.material.Material;

public class Switcher extends Object3D {

	private Object3D activeChild;
	private int index;

	@Override
	protected boolean drawNext(Deque<Object3D> renderStack) {
		if (activeChild != null)
			renderStack.push(activeChild);
		return true;
	}

	@Override
	protected void onChildrenChanged(Object3D node, boolean added) {
		super.onChildrenChanged(node, added);

		if (activeChild == null && added)
			setActive(node);
	}

	@Override
	public void update(float deltaTime) {

	}

	@Override
	protected void draw() {
	}

	public void nextActive() {
		index = ++index % getChildCount();
		setActive(index);
	}

	public void setActive(int index) {
		if (index < 0 || index >= getChildCount())
			return;
		this.index = index;
		setActive(getChildren().get(index));
	}

	public void setActive(Object3D object) {
		activeChild = object;
		index = getChildren().indexOf(object);
	}

	@Override
	public Material getMaterial() {
		return null;
	}

}
