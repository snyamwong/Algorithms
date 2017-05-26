package edu.wit.cs.comp2350;

public class RBTree extends LocationHolder {

	private DiskLocation nil = new DiskLocation(-1, -1);
	private DiskLocation root = new DiskLocation(-1, -1);

	private void setRed(DiskLocation z) {
		if (z != nil)
			z.color = RB.RED;
	}

	@Override
	public DiskLocation find(DiskLocation d) {
		return _find(d, this.root, this.nil);
	}

	private DiskLocation _find(DiskLocation d, DiskLocation curr, DiskLocation parent) {
		if (curr.equals(this.nil))
			return parent;
		else if (d.equals(curr))
			return curr;
		else if (!d.isGreaterThan(curr))
			return _find(d, curr.left, curr);
		else
			return _find(d, curr.right, curr);
	}

	@Override
	public DiskLocation next(DiskLocation d) {
		if (d.right != nil) {
			return min(d.right);
		} else {
			return up(d);
		}
	}

	@Override
	public DiskLocation prev(DiskLocation d) {
		if (d.left != nil) {
			return max(d.left);
		} else {
			return down(d);
		}
	}

	@Override
	public void insert(DiskLocation d) {
		DiskLocation y = this.nil;
		DiskLocation x = this.root;

		while (!x.equals(nil)) {

			y = x;
			if (!d.isGreaterThan(x)) {
				x = x.left;
			} else {
				x = x.right;
			}
		}

		d.parent = y;
		
		// if root is empty
		if (y.equals(this.nil)) {
			this.root = d;
		} else if (!d.isGreaterThan(y)) {
			y.left = d;
		} else {
			y.right = d;
		}

		// sets d's left and right to nil (leaf)
		d.left = nil;
		d.right = nil;
		setRed(d);

		insertFixup(d);
	}

	private void insertFixup(DiskLocation d) {
		while (d.parent.color == RB.RED) {
			if (d.parent.equals(d.parent.parent.left)) {
				DiskLocation y = d.parent.parent.right;
				if (y.color == RB.RED) {
					//case 1
					d.parent.color = RB.BLACK;
					y.color = RB.BLACK;
					setRed(d.parent.parent);
					d = d.parent.parent;
				} else {
					if (d.equals(d.parent.right)) {
						//case 2
						d = d.parent;
						Left_Rotate(d);
					}
					//case 3
					d.parent.color = RB.BLACK;
					setRed(d.parent.parent);
					Right_Rotate(d.parent.parent);
				}
			} else {
				DiskLocation y = d.parent.parent.left;
				if (y.color == RB.RED) {
					d.parent.color = RB.BLACK;
					y.color = RB.BLACK;
					setRed(d.parent.parent);
					d = d.parent.parent;
				} else {
					if (d.equals(d.parent.left)) {
						d = d.parent;
						Right_Rotate(d);
					}
					d.parent.color = RB.BLACK;
					setRed(d.parent.parent);
					Left_Rotate(d.parent.parent);
				}
			}
		}
		this.root.color = RB.BLACK;
	}

	private void Left_Rotate(DiskLocation d) {
		// set y
		DiskLocation y = d.right;
		// turn y's left subtree into d's right subtree
		d.right = y.left;
		if (y.left != this.nil) {
			y.left.parent = d;
		}
		// link d's parent to y
		y.parent = d.parent;
		if (d.parent.equals(this.nil)) {
			this.root = y;
		} else if (d.equals(d.parent.left)) {
			d.parent.left = y;
		} else {
			d.parent.right = y;
		}

		// put d on y's left
		y.left = d;
		d.parent = y;
	}

	private void Right_Rotate(DiskLocation d) {
		// set y
		DiskLocation y = d.left;
		// turn y's right subtree into d's left subtree
		d.left = y.right;
		if (!y.right.equals(this.nil)) {
			y.right.parent = d;
		}
		//link d's parent to y
		y.parent = d.parent;
		if (d.parent.equals(this.nil)) {
			this.root = y;
		} else if (d.equals(d.parent.right)) {
			d.parent.right = y;
		} else {
			d.parent.left = y;
		}

		// put d on y's right
		y.right = d;
		d.parent = y;
	}

	@Override
	public int height() {
		return _height(root);
	}

	private int _height(DiskLocation d) {
		if (d.equals(nil)){
			return 0;
		}
		return Math.max(_height(d.left) , _height(d.right))+ 1;
	}

	private DiskLocation up(DiskLocation d) {
		DiskLocation p = d.parent;

		if (p.equals(nil) || d.equals(p.left)) {
			return p;
		} else {
			return up(p);
		}
	}

	private DiskLocation down(DiskLocation d) {
		DiskLocation p = d.parent;

		if (p.equals(nil) || d.equals(p.right)) {
			return p;
		} else {
			return down(p);
		}
	}

	private DiskLocation min(DiskLocation d) {
		if (d.left.equals(nil))
			return d;
		else
			return min(d.left);
	}

	private DiskLocation max(DiskLocation d) {
		if (d.right.equals(nil))
			return d;
		else
			return max(d.right);
	}

//	public static void printTree(DiskLocation d) {
//		if (d != null) {
//			printTree(d.left);
//			System.out.println(d + ":" + d.color + " ");
//			printTree(d.right);
//		}
//	}

//	public static void main(String[] args) {
//		RBTree t = new RBTree();
//		DiskLocation d = new DiskLocation(90, 1);
//		DiskLocation d2 = new DiskLocation(92, 1);
//		DiskLocation d3 = new DiskLocation(88, 1);
//
//		t.insert(d);
//		t.insert(d2);
//		t.insert(d3);
//		printTree(d);
//	}
}
