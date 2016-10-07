import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {
	
	public static final double NANOSECONDS_IN_SECOND = 1000000000.0;
	public static final String compressedFileName = "compressed.huffman";
	
	public static void main(String[] args) {
		
		try {
			String originalFile = args[0];
			String compressedFile = args[1];
			String outputFile = args[2];
			
			AHCoder encoder = new AHCoder(originalFile, compressedFile, 1000);
			
			long encodeStartTime = System.nanoTime();
			encoder.encode();
			long encodeEndTime = System.nanoTime();
			
			System.out.println("Encoding complete.");
			System.out.println("Encode time: " + (encodeEndTime - encodeStartTime)/NANOSECONDS_IN_SECOND + " seconds");
			
			AHCoder decoder = new AHCoder(compressedFile, outputFile, 1000);
			
			long decodeStartTime = System.nanoTime();
			decoder.decode();
			long decodeEndTime = System.nanoTime();
			
			System.out.println("Decoding complete.");
			System.out.println("Decode time: " + (decodeEndTime - decodeStartTime)/NANOSECONDS_IN_SECOND + " seconds");
			
			System.out.println();
			printFileSizes(originalFile, compressedFile);

		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Not enough arguments to main.");
		}
		
	}
	
	public static void printFileSizes(String inputFile, String compressedFile) {
		File original = new File(inputFile);
		File compressed = new File(compressedFile);
		System.out.println("Original size: " + original.length());
		System.out.println("Compressed size: " + compressed.length());
		System.out.println("Compressed: " + (0.0 + compressed.length())/(0.0 + original.length()) * 100 + "%");
	}
	
}
