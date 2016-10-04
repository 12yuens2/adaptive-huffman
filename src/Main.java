import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {
	
	public static double NANOSECONDS_IN_SECOND = 1000000000.0;

	public static void main(String[] args) {

		String originalFile = "audio/audio.ogg";
		
		try {
			AHCoder encoder = new AHCoder(originalFile, "output.huffman", 1000);
			
			long encodeStartTime = System.nanoTime();
			encoder.encode();
			long encodeEndTime = System.nanoTime();
			
			System.out.println("Encoding complete.");
			System.out.println("Encode time: " + (encodeEndTime - encodeStartTime)/NANOSECONDS_IN_SECOND + " seconds");
			
			AHCoder decoder = new AHCoder("output.huffman", "decode.huffman", 1000);
			
			long decodeStartTime = System.nanoTime();
			decoder.decode();
			long decodeEndTime = System.nanoTime();
			
			System.out.println("Decoding complete.");
			System.out.println("Decode time: " + (decodeEndTime - decodeStartTime)/NANOSECONDS_IN_SECOND + " seconds");
			
			System.out.println();
			File original = new File(originalFile);
			File compressed = new File("output.huffman");
			System.out.println("Original size: " + original.length());
			System.out.println("Compressed size: " + compressed.length());
			System.out.println("Compressed: " + (0.0 + compressed.length())/(0.0 + original.length()) * 100 + "%");

		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
