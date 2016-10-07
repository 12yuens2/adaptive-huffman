import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class AHCoder {

	private FileInputStream in;
	private FileOutputStream out;
	private int indexCounter;
	private HashMap<Integer, Node> lookup;
	private Node root;
	
	private static final int bit = 8;
	private static final int MAX_BUFFER_SIZE = 64;
	
	public AHCoder(String inputFile, String outputFile, int indexCounter) throws IOException {
		this.in = new FileInputStream(inputFile);
		this.out = new FileOutputStream(outputFile);
		this.indexCounter = indexCounter;
		this.lookup = new HashMap<Integer, Node>();
		this.root = new Node(-1, this.indexCounter--, null);
	}
	
	
	public void encode() throws NumberFormatException, IOException {
		Node NYT = root;

		StringBuilder writeBuffer = new StringBuilder("");
		int c;
		while((c = in.read()) != -1) {
			NYT = appendCode(NYT, c, writeBuffer);
			if (writeBuffer.length() > MAX_BUFFER_SIZE) {
				writeOut(writeBuffer);
			}
		}
		writeOut(writeBuffer);

		//write rest of buffer
		if (writeBuffer.length() > 0) {

			//fill end with as much NYT as possible
			char[] NYTCode = buildCode(NYT).toCharArray();
			int i = 0;
			while (writeBuffer.length() < bit && i < NYTCode.length) {
				writeBuffer.append(NYTCode[i] + "");
				i++;
			}
			//otherwise fill with 0s if there is still space
			while(writeBuffer.length() < bit) {
				writeBuffer.append("0");
			}
			out.write(Integer.parseInt(writeBuffer.toString(), 2));
		}
		out.flush();		
	}
	
	public void decode() throws IOException {
		Node currentNode = root;
		Node NYT = root;
		int c = 0;
		int ch = 0;
		StringBuilder readBuffer = new StringBuilder("");
		
		while(c != -1 && readBuffer.length() < MAX_BUFFER_SIZE) {
			c = in.read();
			readBuffer.append(getUncompressed(c));			
		}
		
		while (c != -1 || !(readBuffer.length() == 0)) {
			//at a leaf node
			if (currentNode.getRight() == null && currentNode.getLeft() == null) {
				if (currentNode == NYT) {
					
					try {
						//read c as uncompressed
						ch = Integer.parseInt(readBuffer.substring(0, bit), 2);
						readBuffer.delete(0, bit);
						
						out.write(ch);
						
						//insert the new symbol into the tree
						NYT = insert(ch, NYT);
						
						//read next byte
						if (readBuffer.length() < MAX_BUFFER_SIZE && (c = in.read()) != -1) {
							readBuffer.append(getUncompressed(c));
						}
						
					} catch (StringIndexOutOfBoundsException e) {
						//got an NYT node but couldn't get next 8 bits so we must have reached the end 
						System.err.println("Reached end of input.");
					}
					
				} else {
					//output data at current leaf node
					ch = currentNode.getData();
					out.write(currentNode.getData());
				}
				updateTree(lookup.get(ch));
				currentNode = root;
				
			} else {
				//move 1 bit down the tree
				String bit = readBuffer.substring(0, 1);
				currentNode = (bit.equals("0")) ? currentNode.getLeft() : currentNode.getRight();
				
				//move buffer to next bit
				readBuffer = readBuffer.delete(0, 1);
				
				//read more into buffer
				if (readBuffer.length() < MAX_BUFFER_SIZE && (c = in.read()) != -1) {
					readBuffer.append(getUncompressed(c));
				}
			}
		}
	}

	/**
	 * Appends the codeword for the given symbol 'c' to the given 'writeBuffer' based on the code tree.
	 * Returns the updated NYT node.
	 */
	private Node appendCode(Node NYT, int c, StringBuilder writeBuffer) {
		if (lookup.containsKey(c)) {
			writeBuffer.append(buildCode(lookup.get(c)));
		} else {
			writeBuffer.append(buildCode(NYT));
			writeBuffer.append(getUncompressed(c));
			NYT = insert(c, NYT);
		}
		updateTree(lookup.get(c));
		
		return NYT;
	}
	
	/**
	 * Writes out the buffer in 8 bit chunks
	 */
	private void writeOut(StringBuilder code) throws NumberFormatException, IOException {
		while(code.length() >= bit) {
			String writeOut = code.substring(0, bit);
			code = code.delete(0, bit);
			out.write(Integer.parseInt(writeOut, 2));
		}
	}
	
	/**
	 * Starting from the given node, this function builds the code as it moves up the tree
	 * Returns the binary code in String format
	 */
	private String buildCode(Node node) {
		String code = "";
		
		//go up the tree from the symbol node using the parent
		while(node.getParent() != null) {
			if (node.getParent().getLeft() == node) {
				code = "0" + code;
			} else {
				code = "1" + code;
			}
			node = node.getParent();
		}
		return code;
	}
	
	/**
	 * Recursive function that increments the weight of the given node, then calls itself with the parent until it reaches the root node.
	 * The function also swaps nodes if necessary.
	 */
	public void updateTree(Node node) {
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
	}
	
	/**
	 * Returns the binary representation of 'c' in a String format.
	 */
	private static String getUncompressed(int c) {
		return String.format("%"+bit+"s", Integer.toBinaryString(c)).replace(' ', '0');
	}
	
	
	/**
	 * Insert a new symbol at the NYT node.
	 * Creates two new nodes, the new NYT node as the left child 
	 * and the new symbol node as the right child.
	 */
	private Node insert(int c, Node NYT) {
		//new symbol node
		Node newNode = new Node(c, indexCounter--, NYT);
		lookup.put(c, newNode);

		//new NYT node
		Node newNYT = new Node(-1, indexCounter--, NYT);
		
		NYT.setLeft(newNYT);
		NYT.setRight(newNode);
		
		return newNYT;
	}
	
	/**
	 * Swap two nodes. This function swaps the parent pointers and the indexes, not the subtrees and data.
	 */
	private static void swap(Node a, Node b) {		
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

		Node temp = new Node(-1, -1, null);
		swapProperties(temp, a);
		swapProperties(a, b);
		swapProperties(b, temp);
				
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
	
/*
 * Helper functions to get the highest node block.	
 */
	/**
	 * Returns an array of all nodes with given weight from root
	 */
	private ArrayList<Node> getNodeBlock(int weight) {
		ArrayList<Node> list = new ArrayList<Node>();
		return getNodeBlock(weight, root, list);
	}
	
	private ArrayList<Node> getNodeBlock(int weight, Node node, ArrayList<Node> list) {
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
	private static Node getHighestNode(ArrayList<Node> list) {
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
 * Print function	
 */
	/**
	 * Function to traverse tree and print node details.
	 * Mostly used for viewing the tree for debugging.
	 */
	public void traverse(Node node) {
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
