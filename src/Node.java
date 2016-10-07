public class Node {
	private Node parent;
	private Node left;
	private Node right;
	private int weight;
	private int index;
	private int data;
	
	public Node(int data, int index, Node parent) {
		this.data = data;
		this.index = index;
		this.parent = parent;
	}
	
	public void incrementWeight() {
		this.weight++;
	}
	
	/**
	 * Print node attributes, used mostly for debugging.
	 */
	public void printDetails() {
		System.out.print(data + "|w:" + weight + "|n:" + index +" , ");
	}
	
/* 
 * Getters and Setters 
 */
	
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
