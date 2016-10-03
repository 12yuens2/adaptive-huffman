import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {
	
	private static HashMap<Integer, Node> lookup;
	public static int indexCounter = 0;
	public static Node root;

	public static void main(String[] args) {
		try {
			AHCoder encoder = new AHCoder("test.txt", "output.huffman", 1000);
			encoder.encode();
//			FileInputStream in = new FileInputStream("test.txt");
//			indexCounter = 1000;
//			lookup = new HashMap<Integer, Node>();
//			root = new Node();
//			root.setIndex(indexCounter--);
//			encode(in);
//			System.err.println(root.getWeight());
//			System.err.println(lookup.size());
			
			AHCoder decoder = new AHCoder("output.huffman", "decode.huffman", 1000);
			decoder.decode();
//			in = new FileInputStream("output");
//			indexCounter = 1000;
//			lookup = new HashMap<Integer, Node>();
//			root = new Node();
//			root.setIndex(indexCounter--);
//			decode(in);
//			System.err.println(root.getWeight());
//			System.err.println(lookup.size());
			
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}
	
//	public static void decode(FileInputStream in) throws IOException {
//		Node NYT = root;
//		Node currentNode = root;
//		FileOutputStream out = new FileOutputStream("result.huffman");
//		int c;
//		int ch;
//		String readBuffer = "";
//		c = in.read();
//		readBuffer += getUncompressed(c);
//		
//		while (c > 0 || !readBuffer.equals("")) {
////			readBuffer += String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
////			System.out.println(readBuffer + "  -dcode");
//			
//			//at a leaf node
//			if (currentNode.getRight() == null && currentNode.getLeft() == null) {
//				if (currentNode == NYT) {
//					
//					//read c as uncompressed
//					ch = Integer.parseInt(readBuffer.substring(0, 8), 2);
//					out.write(ch);
//
//					//insert the new symbol into the tree
//					NYT = insert(ch, NYT);
//					
//					//READ NEXT BIT
//					if ((c = in.read()) > 0) {
//						readBuffer += getUncompressed(c);
//					}
//					readBuffer = readBuffer.substring(8);
//					
//				} else {
//					//output data at current leaf node
//					ch = currentNode.getData();
//					out.write(currentNode.getData());
//				}
//				updateTree(lookup.get(ch));
//				currentNode = root;
//				
//			} else {
//				//move 1 bit down the tree
//				String bit = readBuffer.substring(0, 1);
//				currentNode = (bit.equals("0")) ? currentNode.getLeft() : currentNode.getRight();
//				
//				//move buffer to next bit
//				readBuffer = readBuffer.substring(1);
//				if ((c = in.read()) > 0) {
//					readBuffer += String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
//				}
//			}
//		}
//	}
//	
//	public static void encode(FileInputStream in) throws IOException {
//		Node NYT = root;
//		FileOutputStream out = new FileOutputStream("output");
//		int c;
//		String writeBuffer = "";
//		
//		while((c = in.read()) != -1) {
//			String code = "";
//			if (lookup.containsKey(c)) {
//				code += buildCode(lookup.get(c));
//			} else {
//				code += buildCode(NYT);
//				code += getUncompressed(c);
//				NYT = insert(c, NYT);
//			}
//			updateTree(lookup.get(c));
//			writeBuffer += code;
//
////			System.out.println(writeBuffer + " -encode");
//			//write out in 8 bits
//			while(writeBuffer.length() >= 8) {
//				String writeOut = writeBuffer.substring(0, 8);
//				writeBuffer = writeBuffer.substring(8);
//				out.write((byte)Integer.parseInt(writeOut, 2));
//			}
//		}
//
//		//write rest of buffer
//		if (writeBuffer.length() > 0) {
//			
//			//fill end with 0s
//			while (writeBuffer.length() % 8 != 0) {
//				writeBuffer += "0";
//			}
//			out.write((byte)Integer.parseInt(writeBuffer, 2));
//		}
//	}
//	
//	public static String buildCode(Node node) {
//		String code = "";
//		
//		//go up the tree from the symbol node
//		while(node.getParent() != null) {
//			if (node.getParent().getLeft() == node) {
//				code = "0" + code;
//			} else {
//				code = "1" + code;
//			}
//			node = node.getParent();
//		}
//		return code;
//	}
//	
//	public static void updateTree(Node node) {
////		System.out.println();
////		traverse(root);
////		int currentWeight = node.getWeight();
//		if (node == root) {
//			node.incrementWeight();
//		} else {
//			Node highestNode = getHighestNode(getNodeBlock(node.getWeight()));
//			if (highestNode != null && highestNode != node.getParent() && highestNode != node && node != highestNode.getParent()) {
//				swap(node, highestNode);
//			}
//			node.incrementWeight();
//			updateTree(node.getParent());
//		}
//	}
//	
//	public static String getUncompressed(int c) {
//		return String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
////		return String.valueOf(c);
//		//		byte[] bs = s.getBytes();
////		StringBuilder binary = new StringBuilder();
////		for (byte b : bs) {
////			int v = b;
////			for (int i=0; i<8; i++) {
////				binary.append((v & 128) == 0 ? 0 : 1);
////				v <<=1;
////			}
////		}
////		return binary.toString();
//	}
//	
//	
//	/**
//	 * Insert a new symbol at the NYT node.
//	 * Creates two new nodes, the new NYT node as the left child 
//	 * and the new symbol node as the right child.
//	 */
//	public static Node insert(int c, Node NYT) {
//		//new symbol node
//		Node newNode = new Node(c, indexCounter--, NYT);
//		lookup.put(c, newNode);
//
//		//new NYT node
//		Node newNYT = new Node(-1, indexCounter--, NYT);
//		
//		NYT.setLeft(newNYT);
//		NYT.setRight(newNode);
//		NYT.setWeight(1);
//		
//		return newNYT;
//	}
//	
//	public static void swap(Node a, Node b) {
////		System.out.println("===");
////		traverse(root);
////		System.out.print("\nSWAPPING ("); a.printDetails();
////		System.out.print(") WITH ("); b.printDetails();
////		System.out.println(")");
//		
//		if (a.getSibling() == b) {
//			if (a.getParent().getLeft() == a) {
//				a.getParent().setLeft(b);
//				a.getParent().setRight(a);
//			} else {
//				a.getParent().setLeft(a);
//				a.getParent().setRight(b);
//			}
//		} else {
//			swapParents(a, b);
//			swapParents(b, a);
//		}
//
//		Node temp = new Node();
//		swapProperties(temp, a);
//		swapProperties(a, b);
//		swapProperties(b, temp);
//
////		traverse(root);
////		System.out.println("===END SWAP");
//				
//	}
//
///*
// * Helper functions to swap nodes
// */
//	/**
//	 * Set node b as child of node a's parent
//	 */
//	private static void swapParents(Node a, Node b) {
//		if (a.getParent() != null) {
//			if (a.getParent().getLeft() == a) {
//				a.getParent().setLeft(b);
//			} else {
//				a.getParent().setRight(b);
//			}
//		}
//	}
//	/**
//	 * Set parent and children of node a to parent/children of node b
//	 */
//	private static void swapProperties(Node a, Node b) {
//		a.setParent(b.getParent());
//		a.setIndex(b.getIndex());
//	}
//	
//	
//	
//	/**
//	 * Returns an array of all nodes with given weight from root
//	 */
//	public static ArrayList<Node> getNodeBlock(int weight) {
//		ArrayList<Node> list = new ArrayList<Node>();
//		return getNodeBlock(weight, root, list);
//	}
//	
//	private static ArrayList<Node> getNodeBlock(int weight, Node node, ArrayList<Node> list) {
//		if (node.getWeight() == weight) {
//			list.add(node);
//		}
//		if (node.getLeft() != null) {
//			getNodeBlock(weight, node.getLeft(), list);
//		}
//		if (node.getRight() != null) {
//			getNodeBlock(weight, node.getRight(), list);
//		}
//		return list;
//	}
//	
//	/**
//	 * Returns the Node with the highest index from the given list of nodes.
//	 * Returns null if the list is empty.
//	 */
//	public static Node getHighestNode(ArrayList<Node> list) {
//		Node node = null;
//		int i = 0;
//		for (Node n : list) {
//			if (n.getIndex() > i) {
//				node = n;
//				i = n.getIndex();
//			}
//		}
//		
//		return node;
//	}
//	
///*
// * Function to traverse tree and print node details
// */
//	public static void traverse(Node node) {
//		if (node == null) {
//			return;
//		}
//		if (node.getLeft() != null) {
//			traverse(node.getLeft());
//		}
//		node.printDetails();
//		if (node.getRight() != null) {
//			traverse(node.getRight());
//		}
//	}
	
}
