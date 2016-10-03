
public class Node {
	private Node parent;
	private Node left;
	private Node right;
	private int weight;
	private int index;
	private int data;
	private boolean NYT;
	
	/**
	 * Blank constructor
	 */
	public Node() {
		weight = 0;
		data = -1;
	}
	
	/**
	 * Constructor to be called for first occurrence of a symbol
	 */
	public Node(int data, int index, Node parent) {
		this.parent = parent;
		this.index = index;
		this.data = data;
		
//		//weight is one because first occurrence of symbol
//		this.weight = 1;
	}
	
//	public Node(Node parent, Node l, Node r, int weight, int index, int data, boolean NYT) {
//		this.parent = parent;
//		this.left = l;
//		this.right = r;
//		this.weight = weight;
//		this.index = index;
//		this.data = data;
//		this.NYT = NYT;
//	}
	


	public boolean isRoot() {
		return parent == null;
	}

	public void updateWeight() {
		//update weight based on children weight
		if (data < 0) {
			int l = 0, r = 0;
			if (left != null) {
				l = left.getWeight();
			} 
			if (right != null) {
				r = right.getWeight();
			}
			weight = l + r;
		}
	}
	
	public void incrementWeight() {
		this.weight++;
	}
	
	public void printDetails() {
		System.out.print(data + "|w:" + weight + "|n:" + index +" , ");
	}
	
/* getters and setters */
	
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public Node getLeft() {
		return left;
	}
	public void setLeft(Node left) {
		this.left = left;
	}
	public Node getRight() {
		return right;
	}
	public void setRight(Node right) {
		this.right = right;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public boolean isNYT() {
		return NYT;
	}
	public void setNYT(boolean NYT) {
		this.NYT = NYT;
	}
	public int getData() {
		return data;
	}
	public void setData(int data) {
		this.data = data;
	}
	
	public Node getSibling() {	
		return (parent.getLeft() == this) ? parent.getRight() : parent.getLeft();
	}
}
