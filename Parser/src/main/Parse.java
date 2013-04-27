package parser;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import parser.Message;

public class Parse {
	static byte[] inputArray = new byte[10000];
	static int i = 0;
	static BufferedInputStream in;
	
	
	public static void readFile(BufferedInputStream in, byte[] inputArray) {	
		try {
			in = new BufferedInputStream (new FileInputStream("data1.dat"));
			int size = in.available();
			System.out.println("this is the size: "+size);
			System.out.println("we have found the file");
			try {
				in.read(inputArray,0,10000);
				System.out.println("we have read the file");
			}
			catch (IOException e) {
				System.out.println("we could not read the file");
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("we have not found the file");
		} catch (IOException e1) {
			System.out.println("we could not work out the size");
		}
		String s = Arrays.toString(inputArray);
		System.out.println("here it is using toString "+s);
//		String s2 = new String(inputArray);   This give something horrible, left here as a reminder I guess...
//		System.out.println("here it is using New String "+s2);
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Hi "+inputArray[1]);
		readFile(in, inputArray);
		System.out.println("should be a message size: "+toInt(inputArray[0], inputArray[1]));
		System.out.println("should be a message type: "+toInt(inputArray[2], inputArray[3]));
		while(i<inputArray.length) {
			splitMessage(); // this is where the link with the controller needs to be I think...
		}
		// need to code what happens when we need to wrap round in the array
	}
	public static int toInt(byte first, byte second) {
	    ByteBuffer bb = ByteBuffer.wrap(new byte[] {second, first}); // swap second and first depending on endianness
	    int answer = bb.getShort(); // Implicitly widened to an int per JVM spec.
	    if(answer<0) { // due to 2's compliment
	    	answer+=256; // if leading bit is one, this will be read as -128, should be (+)128.
	    	// N.B. this is the case for each byte, so the above won't always work...
	    }
	    return answer;
	}
	
	
	public static Message splitMessage() {
		// i is where we have reached in inputArray. inputArray[i] is the first byte from the new message
		int messageSize = toInt(inputArray[i], inputArray[i+1]);
		Message message;
		
		int messageType = toInt(inputArray[i+2], inputArray[i+3]);
		System.out.print("split into a new message. i="+i);
		switch (messageType) {
		case 100:
			message = newAddMessage(i);
			break;
		case 101:
			message = newModifyMessage(i);
			break;
		case 102:
			message = newDeleteMessage(i);
			break;
		case 103:
			message = newExecuteMessage(i);
			break;
		default:
			i+=messageSize;
			System.out.println(" but not a useful message though");
			return null;
		}
		
		i+=messageSize;
		
		return message;		
	}
	
	public static Message newAddMessage(int index) { // inputArray[index] is the start of the message
		byte[] message = new byte[22];
		message[0] = (byte)1; // 1 is the reference for a message of type Add
		for(int j = 1; j<5; j++) {
			message[j] = inputArray[index+j+4]; // inputArray[index+4..index+8) = the time reference;
		}
		for(int j = 5; j<9; j++) {
			message[j] = inputArray[index+j+8]; // inputArray[index+8..index+12) = the Symbol Index reference;
		}
		for(int j = 9; j<13; j++) {
			message[j] = inputArray[index+j+16]; // inputArray[index+16..index+20) = the OrderID reference;
		}
		for(int j = 13; j<17; j++) {
			message[j] = inputArray[index+j+20]; // inputArray[index+20..index+24) = the price reference;
		}
		message[17] = inputArray[28]; // inputArray[28] is the side reference. Note, this is currently stored as an ASCII Character
		for(int j = 18; j<22; j++) {
			message[j] = inputArray[index+j+24]; // inputArray[index+24..index+28) = the volume reference;
		}
		
		Message newMessage = new Message(message);
		
		return newMessage;
	}
	
	public static Message newModifyMessage(int index) { // inputArray[index] is the start of the message
		byte[] message = new byte[22];
		message[0] = (byte)2; // 2 is the reference for a message of type Modify
		for(int j = 1; j<5; j++) {
			message[j] = inputArray[index+j+4]; // inputArray[index+4..index+8) = the time reference;
		}
		for(int j = 5; j<9; j++) {
			message[j] = inputArray[index+j+8]; // inputArray[index+8..index+12) = the Symbol Index reference;
		}
		for(int j = 9; j<13; j++) {
			message[j] = inputArray[index+j+16]; // inputArray[index+16..index+20) = the OrderID reference;
		}
		for(int j = 13; j<17; j++) {
			message[j] = inputArray[index+j+20]; // inputArray[index+20..index+24) = the price reference;
		}
		message[17] = inputArray[28]; // inputArray[28] is the side reference. Note, this is currently stored as an ASCII Character
		for(int j = 18; j<22; j++) {
			message[j] = inputArray[index+j+24]; // inputArray[index+24..index+28) = the volume reference;
		}
		Message newMessage = new Message(message);
		
		return newMessage;
	}
	
	public static Message newDeleteMessage(int index) { // inputArray[index] is the start of the message
		byte[] message = new byte[22];
		message[0] = (byte)3; // 3 is the reference for a message of type Delete
		for(int j = 1; j<5; j++) {
			message[j] = inputArray[index+j+4]; // inputArray[index+4..index+8) = the time reference;
		}
		for(int j = 5; j<9; j++) {
			message[j] = inputArray[index+j+8]; // inputArray[index+8..index+12) = the Symbol Index reference;
		}
		for(int j = 9; j<13; j++) {
			message[j] = inputArray[index+j+16]; // inputArray[index+16..index+20) = the OrderID reference;
		}
		for(int j = 13; j<17; j++) {
			message[j] = 0; // delete messages have no price reference;
		}
		message[17] = inputArray[20]; // inputArray[20] is the side reference. Note, this is currently stored as an ASCII Character
		for(int j = 18; j<22; j++) {
			message[j] = 0; // delete messages have no volume reference;
		}
		Message newMessage = new Message(message);
		
		return newMessage;
	}
	
	public static Message newExecuteMessage(int index) { // inputArray[index] is the start of the message
		byte[] message = new byte[22];
		message[0] = (byte)0; // 0 is the reference for a message of type Add
		for(int j = 1; j<5; j++) {
			message[j] = inputArray[index+j+4]; // inputArray[index+4..index+8) = the time reference;
		}
		for(int j = 5; j<9; j++) {
			message[j] = inputArray[index+j+8]; // inputArray[index+8..index+12) = the Symbol Index reference;
		}
		for(int j = 9; j<13; j++) {
			message[j] = inputArray[index+j+16]; // inputArray[index+16..index+20) = the OrderID reference;
		}
		for(int j = 13; j<17; j++) {
			message[j] = inputArray[index+j+20]; // inputArray[index+20..index+24) = the price reference;
		}
		message[17] = 2; // execute messages have no side reference
		for(int j = 18; j<22; j++) {
			message[j] = inputArray[index+j+24]; // inputArray[index+24..index+28) = the volume reference;
		}
		Message newMessage = new Message(message);
		
		return newMessage;
	}
}



// to do:
// sort out the two's compliment reading in problem
// deal with it keeping reading...
// fix the reading in problem i.e. sort out why the stuff it reads in isn't what we really expect











