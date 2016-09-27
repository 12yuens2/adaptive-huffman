import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;


public class AhEncoder {
	
	private static HashMap<Integer, Node> lookup;
	public static int indexCounter = 0;
	public static Node root;
	public static Node[] indexLookup;

	public static void main(String[] args) {
//		
//		Scanner scanner = new Scanner(System.in);
//		String toEncode;
//		
//		toEncode = scanner.next();
//		String toEncode = "aardv";

//		Node a = new Node("a", 0, null);
//		Node b = new Node("b", 0, a);
//		Node c = new Node("c", 0, a);
//		
//		a.setLeft(b);
//		a.setRight(c);
//		
//		Node d = new Node("d", 0, b);
//		Node e = new Node("e", 0, b);
//		b.setLeft(d);
//		b.setRight(e);
//		
//		Node f = new Node("f", 0, c);
//		c.setLeft(f);
//		
//		traverse(a);
//		swap(c, d);
//		traverse(a);
		
		
//		Node NYT;

		try {
			FileInputStream in = new FileInputStream("test.txt");
			indexCounter = 100;
			lookup = new HashMap<Integer, Node>();
			root = new Node();
			root.setIndex(indexCounter--);
			encode(in);
//			Reader r = new InputStreamReader(in, "UTF-8");
//			int c = in.read();
//			while (c != -1) {
//				System.out.println(c + " ");
//				c = in.read();
//			}

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}
	
	public static void encode(FileInputStream in) throws IOException {
		Node NYT = root;
		FileOutputStream out = new FileOutputStream("output");
		DataOutputStream dout = new DataOutputStream(out);
		int c;
		
		while((c = in.read()) != -1) {
			String code = "";
			if (lookup.containsKey(c)) {
				Node symbolNode = lookup.get(c);
				code += buildCode(symbolNode);
//				updateTree(symbolNode);
			} else {
				code += buildCode(NYT);
				code += getUncompressed(c);
				NYT = insert(c, NYT);
			}
			updateTree(lookup.get(c));
			
			
			System.out.println(code + " ");
//			dout.write(Integer.parseInt(code, 2));
//			out.write(Byte.parseByte(code, 2));
//			while (code.length() > 5) {
//				out.write(Byte.parseByte(code.substring(0, 5), 2));
////				out.write(Byte.parseByte(code.substring(5, code.length()), 2));
//				code = code.substring(5, code.length());
//			}
			
			dout.write(Integer.valueOf(code, 2));
			System.out.println(Integer.valueOf(code, 2));
//			out.write(Integer.parseInt(code, 2));


//			updateWeight(NYT);
//			out.flush();
		}
	}
	
	public static String buildCode(Node node) {
		String code = "";
		
		//go up the tree from the symbol node
		while(node.getParent() != null) {
			if (node.getParent().getLeft() == node) {
				code += "0";
			} else {
				code += "1";
			}
			node = node.getParent();
		}
		return code;
	}
	
	public static void updateTree(Node node) {
//		int currentWeight = node.getWeight();
		if (node == root) {
			node.incrementWeight();
		} else {
			Node highestNode = getHighestNode(getNodeBlock(node.getWeight()));
			if (highestNode != null && highestNode != node.getParent() && highestNode != node) {
				swap(node, highestNode);
			}
			node.incrementWeight();
			updateTree(node.getParent());
		}
//		Node highestNode = getHighestNode(getNodeBlock(node.getWeight()));
//		if (node != highestNode.getParent() && highestNode != node.getParent() && node != highestNode && highestNode != null && highestNode != root) {
//			swap(node, highestNode);
//		}
//		node.incrementWeight();
	}
	
	public static String getUncompressed(int c) {
		return Integer.toBinaryString(c);
		//		byte[] bs = s.getBytes();
//		StringBuilder binary = new StringBuilder();
//		for (byte b : bs) {
//			int v = b;
//			for (int i=0; i<8; i++) {
//				binary.append((v & 128) == 0 ? 0 : 1);
//				v <<=1;
//			}
//		}
//		return binary.toString();
	}
	
	
	/**
	 * Insert a new symbol at the NYT node.
	 * Creates two new nodes, the new NYT node as the left child 
	 * and the new symbol node as the right child.
	 */
	public static Node insert(int c, Node NYT) {
		//new symbol node
		Node newNode = new Node(c, indexCounter--, NYT);
		newNode.setParent(NYT);
		lookup.put(c, newNode);
		NYT.setRight(newNode);
		
		//new NYT node
		Node newNYT = new Node();
		NYT.setLeft(newNYT);
		NYT.setWeight(1);
		newNYT.setParent(NYT);
		newNYT.setIndex(indexCounter--);
		
		return newNYT;
	}
	
	public static void updateWeight(Node node) {	
		int tempWeight = node.getWeight();
		node.updateWeight();
		
		//if weight is incremented on updateWeight()
		if (node.getWeight() > tempWeight) {
			//reset weight back to previous weight and swap nodes
			node.setWeight(tempWeight);
			Node highestNode = getHighestNode(getNodeBlock(tempWeight));
			if (highestNode != node.getParent() && node != highestNode && highestNode != null && node != highestNode.getParent()&& highestNode != root && node != root) {
				swap(node, highestNode);			
			} 
			
			//update weight after swap
			node.updateWeight();
		}
		
		//recursive call to update weight up the tree
		if (node.getParent() != null) {
			updateWeight(node.getParent());
		}

		
	}
	
	public static void swap(Node a, Node b) {
//		System.out.println("===");
//		traverse(root);
//		System.out.print("\nSWAPPING ("); a.printDetails();
//		System.out.print(") WITH ("); b.printDetails();
//		System.out.println(")");
		
		if (a.getSibling() == b) {
			if (a.getParent().getLeft() == a) {
				a.getParent().setLeft(b);
				a.getParent().setRight(a);
			} else {
				a.getParent().setLeft(a);
				a.getParent().setRight(b);
			}
		} else {
			swapParents(a, b);
			swapParents(b, a);
		}
//		swapParents(a, b);
//		swapParents(b, a);
		
		Node temp = new Node();
		swapProperties(temp, a);
		swapProperties(a, b);
		swapProperties(b, temp);

//		traverse(root);
//		System.out.println("===END SWAP");
				
	}

/*
 * Helper functions to swap nodes
 */
	/**
	 * Set node b as child of node a's parent
	 */
	private static void swapParents(Node a, Node b) {
		if (a.getParent() != null) {
			if (a.getParent().getLeft() == a) {
				a.getParent().setLeft(b);
			} else {
				a.getParent().setRight(b);
			}
		}
	}
	/**
	 * Set parent and children of node a to parent/children of node b
	 */
	private static void swapProperties(Node a, Node b) {
		a.setParent(b.getParent());
		a.setIndex(b.getIndex());
	}
	
	
	
	/**
	 * Returns an array of all nodes with given weight from root
	 */
	public static ArrayList<Node> getNodeBlock(int weight) {
		ArrayList<Node> list = new ArrayList<Node>();
		return getNodeBlock(weight, root, list);
	}
	
	private static ArrayList<Node> getNodeBlock(int weight, Node node, ArrayList<Node> list) {
		if (node.getWeight() == weight) {
			list.add(node);
		}
		if (node.getLeft() != null) {
			getNodeBlock(weight, node.getLeft(), list);
		}
		if (node.getRight() != null) {
			getNodeBlock(weight, node.getRight(), list);
		}
		return list;
	}
	
	/**
	 * Returns the Node with the highest index from the given list of nodes.
	 * Returns null if the list is empty.
	 */
	public static Node getHighestNode(ArrayList<Node> list) {
		Node node = null;
		int i = 0;
		for (Node n : list) {
			if (n.getIndex() > i) {
				node = n;
				i = n.getIndex();
			}
		}
		
		return node;
	}
	
/*
 * Functions to traverse tree
 */
	public static void traverse(Node node) {
		if (node == null) {
			return;
		}
		if (node.getLeft() != null) {
			traverse(node.getLeft());
		}
		node.printDetails();
		if (node.getRight() != null) {
			traverse(node.getRight());
		}
	}
	
}
