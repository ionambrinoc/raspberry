package main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Parse {
	static int i = 0; // i represents where in the inputArray we have processed
	static int j = 0; // j represents where in the inputArray the last read was stored up to
	static int readSize = 9216; // size of each read call. 9216 = 9*1024;
	static int arraySize = 10240; // size of the inputArray
	static byte[] inputArray = new byte[arraySize];
	static BufferedInputStream in;
	
	
	public static void initialReadFile() {	
		try {
			in = new BufferedInputStream (new FileInputStream("data1.dat"));
			int size = in.available();
			System.out.println("this is the size: "+size);
			System.out.println("we have found the file");
			try {
				j = readSize;
				in.read(inputArray,0,j);
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
	
	
	
	public static void readFile() { // BufferedInputStream in, byte[] inputArray) {
		try {
			int newStart = j; System.out.println("j equals "+j+" and the other thing is: "+(arraySize-newStart-1)+
					" inputArray[newStart] = "+inputArray[newStart]+ " inputArray[blah] = "+inputArray[arraySize-newStart-1]);
			if(newStart+readSize>arraySize) {
				// we will need to wrap around
				System.out.println("we need to wrap around, storing from "+newStart+" until the end.");
				in.read(inputArray, newStart, arraySize-newStart-1); // read from the file filling up the latter part of the array
				System.out.println("j = "+j);
				j = readSize - (arraySize-newStart-1); // this is the index of where in the array we will have stored up to
				System.out.println("j now = "+j);
				in.read(inputArray, 0, j); // read the file into the first part of the array
			}
			else {
				j = readSize+newStart;
				System.out.println("it all fits in, so no need to wrap around");
				in.read(inputArray, newStart, readSize);
			System.out.println("we have read the file");
			}
		}
		catch (IOException e) {
			System.out.println("we could not read the file");
		}
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		initialReadFile();
		int packetSize = toInt(inputArray[i], inputArray[i+1]);
		System.out.println("i= "+i+" and first packet size is "+packetSize);
		while(i<arraySize) { // while the point we have processed is within the stored area
			while(i+packetSize<j) { // while the next packet has been fully read and stored in inputArray
				splitPacket(); // this is where the link with the controller needs to be I think...
				packetSize = toInt(inputArray[i], inputArray[i+1]); // update packetSize to the size of the next packet
			}
			// the next packet hasn't been fully read/stored, so we need to read more of the file
			System.out.println("packet too big ("+packetSize+"), time to wrap around, i= "+i+" and j= "+j);
			readFile();
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
	
	public static void splitPacket() {
		// this will split the data into packets
		int remainingPacketSize = toInt(inputArray[i], inputArray[i+1]); // the first two bytes represent the size of the packet
		System.out.println("the packet size is "+remainingPacketSize);
		i = (i+16)%arraySize; // the packet header is 16 bytes
		remainingPacketSize-=16; // we move past the 16 bytes of the header
		while (remainingPacketSize>0) { // while we are still in the same packet
			remainingPacketSize -= (toInt(inputArray[i], inputArray[i+1])); // decrement the remainingPacketSize by the size of the next message
			splitMessage(); // split the packet into messages
		}
		
	}
	
	public static ControllerMessage splitMessage() {
		// i is where we have reached in inputArray. inputArray[i] is the first byte from the new message
		int messageSize = toInt(inputArray[i], inputArray[i+1]);
		Message message;
		
		if ( ((i+messageSize)%arraySize) > j) { // this should no longer happen
			System.out.println("why are we here?");
			readFile(); //in, inputArray);
		}
		else if (messageSize==0) { // this shouldn't really happen, but might
			i= (i+1)%arraySize;
			if( ((i+1)%arraySize) == j) {
				// needs to read more of the file
				readFile(); //in, inputArray);
				System.out.println("message too big, and message size 0");
			}
			System.out.println("message size is 0, i is "+i); 
			return null;
		}
		
		int messageType = toInt(inputArray[i+2], inputArray[i+3]);
		System.out.print("new message. i="+i);
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
			i= (i+messageSize)%arraySize;
			System.out.println(" ignored");
			return null;
		}	
		i= (i+messageSize)%arraySize;
	
		ControllerMessage messageToSend = new ControllerMessage(message);
		
		return messageToSend;
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

// deal with it keeping reading - set the size of buffer, when it reads, it needs to wrap around
// and not just replace everything i.e. so a partially read message doesn't lose the first half
// could consider only opening a message/packet if the whole thing has been read/stored
// which would deal with the problem of half-read messages
// can't do it for packets, as a packet could be too big to fit in one go

// so we start with a packet: packet header (16 bytes), control message (14 bytes, sequence number reset)
// then next packet, size 282 bytes, type original message: 16 byte packet header, 7 x 38 byte symbol index mapping control messages ish
// then new packet, size 54 bytes, type original message: 16 byte packet header, 38 byte symbol index mapping control message
// then more the same (this is confirmed by what is output)





