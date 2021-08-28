package au.edu.federation.itech3104.michaelwilson.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import au.edu.federation.itech3104.michaelwilson.graphics.IDisposable;

/**
 * Represents a tree structure, each node has multiple child nodes and one
 * parent node.
 */
public abstract class Node<T extends Node<T>> implements Iterable<T> {

	private T parent;

	private final List<T> children = new ArrayList<>();

	public Node() {

	}

	/**
	 * Invoked when a child node is added or removed.
	 */
	protected void onChildrenChanged(T node, boolean added) {
	}

	/**
	 * Invoked when the parent node changes.
	 */
	protected void onParentChanged() {
	}

	/**
	 * Invoked when a node is added/removed anywhere from the tree.
	 */
	protected void onTreeChanged(T node, boolean added) {
	}

	public List<T> getChildren() {
		return children;
	}

	public T getParent() {
		return parent;
	}

	protected void notifyTreeChanged(T node, boolean added) {
		onTreeChanged(node, added);

		notifyDescendantsTreeChanged(node, added);
		notifyAncestorsTreeChanged(node, added);
	}

	protected void notifyAncestorsTreeChanged(T node, boolean added) {
		if (isRoot())
			return;

		parent.onTreeChanged(node, added);
		parent.notifyAncestorsTreeChanged(node, added);
	}

	protected void notifyDescendantsTreeChanged(T node, boolean added) {
		for (T child : children) {
			child.onTreeChanged(node, added);
			child.notifyDescendantsTreeChanged(node, added);
		}
	}

	/**
	 * Set the parent node. Use null to make a root node. Updates the child node
	 * list {@link #getChildren()} and calls the relevant event methods.
	 */
	public void setParent(T value) {

		if (parent != null && parent != value) {
			@SuppressWarnings("unchecked")
			T node = (T) this;
			if (parent.getChildren().remove(node)) {
				parent.onChildrenChanged(node, false);
				parent.notifyTreeChanged(node, false);
			}
		}

		if (value != null && value != parent) {
			@SuppressWarnings("unchecked")
			T node = (T) this;
			value.getChildren().add(node);
			value.onChildrenChanged(node, true);
			value.notifyTreeChanged(node, true);
		}

		parent = value;
		onParentChanged();
	}

	public int getChildCount() {
		return children.size();
	}

	/**
	 * True if the node has no parent.
	 */
	public boolean isRoot() {
		return parent == null;
	}

	/**
	 * True if the node has no children and has a parent node.
	 */
	public boolean isLeaf() {
		return !isRoot() && children.isEmpty();
	}

	/**
	 * Returns the root node for this node.
	 */
	public Node<T> getRoot() {
		return getRoot(this);
	}

	private Node<T> getRoot(Node<T> t) {
		return isRoot() ? this : getRoot(parent);
	}

	/**
	 * Returns an iterator for the child nodes contained in this node.
	 * 
	 * @see #getChildren()
	 */
	@Override
	public Iterator<T> iterator() {
		return children.iterator();
	}

	/**
	 * For the specified node and all descendants, invoke the
	 * {@link IDisposable#dispose() dispose()} method on any node that implements the
	 * {@link IDisposable} interface.
	 */
	public static <T extends Node<T>> void dispose(Node<T> obj) {
		if (obj instanceof IDisposable)
			((IDisposable) obj).dispose();

		for (T child : obj.children)
			dispose(child);
	}

}
