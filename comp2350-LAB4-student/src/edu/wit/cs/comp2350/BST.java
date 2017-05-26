package edu.wit.cs.comp2350;

/*
 * A simple binary tree implementation to hold DiskLocations. The tree is
 * never rebalanced so if inputs are sequential, the tree might end up being
 * very imbalanced.
 */
public class BST extends LocationHolder {
	
	private DiskLocation root;
	private static DiskLocation nil = new DiskLocation(-1, -1);
	
	@Override
	public DiskLocation next(DiskLocation d) {
		if (d.right != nil)
			return min(d.right);
		else
			return upNext(d);
	}

	private DiskLocation upNext(DiskLocation d) {
		DiskLocation p = d.parent;
		if (p == nil || d == p.left)
			return p;
		else
			return upNext(p);
	}

	private DiskLocation min(DiskLocation d) {
		if (d.left == nil)
			return d;
		else
			return min(d.left);
	}
	@Override
	public DiskLocation prev(DiskLocation d) {
		if (d.right != nil)
			return max(d.left);
		else
			return upPrev(d);
	}

	private DiskLocation upPrev(DiskLocation d) {
		DiskLocation p = d.parent;
		if (p == nil || d == p.right)
			return p;
		else
			return upPrev(p);
	}

	private DiskLocation max(DiskLocation d) {
		if (d.right == nil)
			return d;
		else
			return max(d.right);
	}

	@Override
	public void insert(DiskLocation d) {
		d.left = nil;
		d.right = nil;
		if (root == null) {	// tree is empty
			root = d;
			d.parent = nil;
			return;
		}
			
		d.parent = findParent(d, root, nil);
		// tree has elements already
		if (d.isGreaterThan(d.parent))
			d.parent.right = d;
		else
			d.parent.left = d;
	}

	private DiskLocation findParent(DiskLocation d, DiskLocation curr, DiskLocation parent) {
		if (curr == nil)
			return parent;
		if (d.isGreaterThan(curr))
			return findParent(d, curr.right, curr);
		else
			return findParent(d, curr.left, curr);
	}

	@Override
	public int height() {
		return _height(root);
	}
	
	private int _height(DiskLocation d) {
		if (d == nil)
			return 0;
		return 1 + Math.max(_height(d.right), _height(d.left));
	}

	@Override
	public DiskLocation find(DiskLocation d) {
		return _find(d, root);
	}

	private DiskLocation _find(DiskLocation d, DiskLocation curr) {
		if (curr == nil)
			return null;
		else if (d.equals(curr))
			return curr;
		else if (d.isGreaterThan(curr))
			return _find(d, curr.right);
		else
			return _find(d, curr.left);
	}

}
