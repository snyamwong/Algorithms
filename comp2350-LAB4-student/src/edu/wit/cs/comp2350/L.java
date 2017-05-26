package edu.wit.cs.comp2350;


/*
 * A doubly linked list implementation to hold DiskLocations
 * The list is sorted on inserts so that next and prev are fast
 * 
 * The class uses DiskLocation's right/parent for next/prev in the list
 */
public class L extends LocationHolder {
	
	private DiskLocation root;
	private static DiskLocation nil = new DiskLocation(-1, -1);
	
	@Override
	public DiskLocation find(DiskLocation d) {
		return _find(d, root);
	}

	private DiskLocation _find(DiskLocation d, DiskLocation curr) {
		if (curr == nil)
			return null;
		else if (d.equals(curr))
			return curr;
		else
			return _find(d, curr.right);
	}

	@Override
	public DiskLocation next(DiskLocation d) {
		return d.right;
	}

	@Override
	public DiskLocation prev(DiskLocation d) {
		return d.parent;
	}

	@Override
	public void insert(DiskLocation d) {
		if (root == null) {		//empty list
			root = d;
			d.parent = nil;
			d.right = nil;
			return;
		}
		DiskLocation temp = root;
		DiskLocation prev = nil;
		while (!temp.isGreaterThan(d) && !temp.equals(nil)) {
			prev = temp;
			temp = temp.right;
		}

		d.parent = prev;
		d.right = temp;
		if (temp != nil)
			temp.parent = d;
		if (prev != nil)
			prev.right = d;
		else
			root = d;

	}

	@Override
	public int height() {
		int h = 0;
		DiskLocation d = root;
		while (d != nil) {
			h++;
			d = d.right;
		}
		return h;
	}


}
