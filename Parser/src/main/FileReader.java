package main;
import java.io.*;

public class FileReader {

	protected FileInputStream binaryListOfMessages;
	
	public FileReader(String inputFileName) throws FileNotFoundException{
		binaryListOfMessages = new FileInputStream(inputFileName);
		
	}
	
	public Message ReturnNextMessage() throws IOException {
		byte[] b = null;
		int messageLen = 0;
		binaryListOfMessages.read(b, 0, 2);
		
		
	}
	
	
	public static void Main(String[] args) {
		

	}

}
