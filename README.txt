To build the project, run ant in the Code directory. 

	ant

The AHuffman.jar should be now be built in the same directory.
To run the .jar file, run the following command in the terminal: 

	java -jar AHuffman.jar [input file] [compressed output file] [decoded output file]

For example:

	java -jar AHuffman.jar "texts/shakespeare.txt" "compressed.huffman" "decoded.huffman"
