package au.edu.federation.itech3104.michaelwilson.graph;

import au.edu.federation.itech3104.michaelwilson.math.Mat4f;

public class Transform extends Node<Transform> {

	/**
	 * The model matrix local to this {@link Transform}. Any modifications to this
	 * matrix requires the {@link #updateGlobalMatrix()} method to be called, so
	 * changes can be reflected in descendant {@link #globalMatrix matrices}.<br>
	 * <br>
	 * Note: The local matrix is public so we can easily use {@link Mat4f} methods
	 * that return a new instance.
	 */
	public Mat4f localMatrix = new Mat4f(1.0f);

	/**
	 * The global model matrix combining all ancestor global matrices.
	 * 
	 * @see #updateGlobalMatrix()
	 */
	private Mat4f globalMatrix = new Mat4f(1.0f);

	/**
	 * Calculates the global matrix for this node using the
	 * {@link #getGlobalMatrix() global} and {@link #localMatrix local} matrices of
	 * ancestor nodes. Must be called whenever the local matrix is modified.
	 */
	public void updateGlobalMatrix() {
		Mat4f parentMatrix = isRoot() ? new Mat4f(1.0f) : getParent().getGlobalMatrix();

		globalMatrix = parentMatrix.times(localMatrix);

		for (Transform child : this)
			child.updateGlobalMatrix();
	}

	@Override
	protected void onParentChanged() {
		super.onParentChanged();
		updateGlobalMatrix(); // Update the global matrix, since the parent has changed.
	}

	/**
	 * The global model matrix combining all ancestor {@link Transform#localMatrix
	 * model matrices}.
	 * 
	 * @see #updateGlobalMatrix()
	 */
	public Mat4f getGlobalMatrix() {
		return globalMatrix;
	}

}
