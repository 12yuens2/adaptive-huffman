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
			
			in = new FileInputStream("output");
			indexCounter = 100;
			lookup = new HashMap<Integer, Node>();
			root = new Node();
			root.setIndex(indexCounter--);
			decode(in);
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
	
	public static void decode(FileInputStream in) throws IOException {
		root = new Node();
		Node NYT = root;
		Node currentNode = root;
		FileOutputStream out = new FileOutputStream("result.huffman");
		DataOutputStream dout = new DataOutputStream(out);
		byte[] bs = {32};
		int c;
//		while((in.read(bs)) > 0) {
//			for ( byte b : bs) {
//				System.out.println(Byte.toString(b));
//			}
////			dout.writeUTF(Integer.toBinaryString(c));
//		}
		char ch;
		String readBuffer = "";
		c = in.read();
		readBuffer += String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');

		while (c > 0 || !readBuffer.equals("")) {
//			readBuffer += String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
			System.out.println(readBuffer + "  -dcode");
			if (currentNode.getRight() == null && currentNode.getLeft() == null) {
				System.out.println(buildCode(NYT));
				if (currentNode == NYT) {
					System.out.println((char)Integer.parseInt(readBuffer.substring(0, 8), 2));
					ch = (char)Integer.parseInt(readBuffer.substring(0, 8), 2);
					
					//READ NEXT BIT
					if ((c = in.read()) > 0) {
					readBuffer += String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
					}				
					readBuffer = readBuffer.substring(8);		

					
				} else {
					ch = (char) currentNode.getData();
					System.out.println(ch + " =");
				}
				
				
				//update tree
				if (lookup.containsKey(ch)) {
					updateTree(lookup.get(ch));
				} else {
					NYT = insert(ch, NYT);
				}
				currentNode = root;
				
			} else {
				String bit = readBuffer.substring(0, 1);
				currentNode = (bit.equals("0")) ? currentNode.getLeft() : currentNode.getRight();
				
				//move to next bit
				readBuffer = readBuffer.substring(1);
				if ((c = in.read()) > 0) {
				readBuffer += String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');}
			}
//			System.out.println(readBuffer.equals(""));
		}
	}
	
	public static void encode(FileInputStream in) throws IOException {
		Node NYT = root;
		FileOutputStream out = new FileOutputStream("output");
		DataOutputStream dout = new DataOutputStream(out);
//		FileOutputStream debugOut = new FileOutputStream("output.debug");
		DataOutputStream debugOut = new DataOutputStream(new FileOutputStream("output.debug"));
		int c;
		String writeBuffer = "";
		while((c = in.read()) != -1) {
			String code = "";
			if (lookup.containsKey(c)) {
				Node symbolNode = lookup.get(c);
				code += buildCode(symbolNode);
//				updateTree(symbolNode);
			} else {
				code += buildCode(NYT);
				System.out.println(buildCode(NYT));
				code += getUncompressed(c);
				NYT = insert(c, NYT);
			}
			updateTree(lookup.get(c));
			
			writeBuffer += code;
//			System.out.println(code + " ");
//			dout.write(Integer.parseInt(code, 2));
//			out.write(Byte.parseByte(code, 2));
//			while (code.length() > 5) {
//				out.write(Byte.parseByte(code.substring(0, 5), 2));
////				out.write(Byte.parseByte(code.substring(5, code.length()), 2));
//				code = code.substring(5, code.length());
//			}
			System.out.println(writeBuffer + " ==");
			
			while (writeBuffer.length() >= 8) {
				String writeOut = writeBuffer.substring(0, 8);
				writeBuffer = writeBuffer.substring(8);

				dout.write((byte)Integer.parseInt(writeOut, 2));
			}
//			System.out.println(writeBuffer + "bug2");
			//write rest of buffer then clear it
//			System.out.println(writeBuffer + " asd");
//			System.out.println(Integer.parseInt(writeBuffer, 2));
//			dout.write(Integer.parseInt(writeBuffer, 2));
//			writeBuffer = "";
			
//			dout.write(Integer.valueOf(code, 2));
//			System.out.println(Integer.valueOf(code, 2));
//			out.write(Integer.parseInt(code, 2));


//			updateWeight(NYT);
//			out.flush();
		}
		
		while(writeBuffer.length() > 8) {
			String writeOut = writeBuffer.substring(0, 8);
			writeBuffer = writeBuffer.substring(8);
			dout.write((byte)Integer.parseInt(writeOut, 2));
		}
		if (writeBuffer.length() > 0) {
			while (writeBuffer.length() % 8 != 0) {
				writeBuffer += "0";
			}
			dout.write((byte)Integer.parseInt(writeBuffer, 2));
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
		return String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
//		return String.valueOf(c);
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
