package parser;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class Parse extends Thread {
	int i = 0; // i represents where in the inputArray we have processed
	int j = 0; // j represents where in the inputArray the last read was stored up to
	int readSize = 9216; // size of each read call. 9216 = 9*1024;
	int arraySize = 10240; // size of the inputArray
	byte[] inputArray = new byte[arraySize];
	BufferedInputStream in;
	final LinkedBlockingQueue<ControllerMessage> messageQueue;
	
	public Parse(LinkedBlockingQueue<ControllerMessage> messageQueue){
		this.messageQueue = messageQueue;
		
	}

	
	public void initialReadFile() {	
		try {
			in = new BufferedInputStream (new FileInputStream("data2.dat"));
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
	
	
	
	public void readFile() { // BufferedInputStream in, byte[] inputArray) {
		try {
			int newStart = j; System.out.println("j equals "+j+" and the other thing is: "+(arraySize-newStart-1)+
					" inputArray[newStart] = "+inputArray[newStart]+ " inputArray[blah] = "+inputArray[arraySize-newStart-1]);
			if(newStart+readSize>arraySize) {
				// we will need to wrap around
				System.out.println("we need to wrap around, storing from "+newStart+" until the end.");
				in.read(inputArray, newStart, arraySize-newStart); // read from the file filling up the latter part of the array
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
	
	@Override
	public void run() {
		initialReadFile();
		int packetSize = toInt(inputArray[i], inputArray[(i+1)%arraySize]);
		System.out.println("i= "+i+" and first packet size is "+packetSize);
		while(i<arraySize) { // while the point we have processed is within the stored area
	//		while(i+packetSize<j) { // while the next packet has been fully read and stored in inputArray
				try {
					splitPacket();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // this is where the link with the controller needs to be I think...
				packetSize = toInt(inputArray[i], inputArray[i+1]); // update packetSize to the size of the next packet
	//		}
			// the next packet hasn't been fully read/stored, so we need to read more of the file
	//		System.out.println("packet too big ("+packetSize+"), time to wrap around, i= "+i+" and j= "+j);
	//		readFile();
		}
		// need to code what happens when we need to wrap round in the array
	}
	public int toInt(byte first, byte second) {
/**	    ByteBuffer bb = ByteBuffer.wrap(new byte[] {second, first}); // swap second and first depending on endianness
	    int answer = bb.getShort(); // Implicitly widened to an int per JVM spec.
	    if(answer<0) { // due to 2's compliment
	    	answer+=256; // if leading bit is one, this will be read as -128, should be (+)128.
	    	// N.B. this is the case for each byte, so the above won't always work...
	    }
	    System.out.println("answer is "+answer);
*/
		int answer = 0; int largeBit = second; int smallBit = first;
		if(second<0) largeBit += 256;
		if(first<0) smallBit += 256;
		answer = largeBit*256+smallBit;
		return answer;
	}
	
	public void splitPacket() throws InterruptedException {
		// this will split the data into packets
		int remainingPacketSize = toInt(inputArray[i], inputArray[(i+1)%arraySize]); // the first two bytes represent the size of the packet
		System.out.println("the packet size is "+remainingPacketSize+" and the packet type is "+inputArray[(i+2)%arraySize]);
		i = inputArray[999999];
		i = (i+16)%arraySize; // the packet header is 16 bytes
		remainingPacketSize-=16; // we move past the 16 bytes of the header
		while (remainingPacketSize>0) { // while we are still in the same packet
			System.out.println("i= "+i);
			int nextMessageSize = (toInt(inputArray[i%arraySize], inputArray[(i+1)%arraySize]));
			int x = (i+nextMessageSize)%arraySize; // x is the where the end of the next message should be stored, we want this to be within the stored area
			if( (x>j && x<(j+arraySize-readSize)) || x<(j-readSize) ) { // if the next message hasn't been fully read
				System.out.println("the next message hasn't been fully read. i= "+i+". messageSize = "+nextMessageSize+". j="+j);
				System.out.println("x = "+x+". j-readSize = "+ (j-readSize) );
				readFile();
			}
			remainingPacketSize -= nextMessageSize; // decrement the remainingPacketSize by the size of the next message
			messageQueue.put(splitMessage()); // split the packet into messages
		}
		
	}
	
	public ControllerMessage splitMessage() {
		// i is where we have reached in inputArray. inputArray[i] is the first byte from the new message
		int messageSize = toInt(inputArray[i], inputArray[(i+1)%arraySize]);
		Message message;
		
		int x = (i+messageSize)%arraySize;
		if ( (x>j && x<(j+arraySize-readSize)) || x<(j-readSize)) { // this should no longer happen
			System.out.println("why are we here?");
			readFile(); //in, inputArray);
		}
		else if (messageSize==0) { // this shouldn't really happen, but might
			i= (i+1)%arraySize;
			if( ((i+1)%arraySize) == j) {
				// needs to read more of the file
				readFile(); //in, inputArray);
				System.out.println("message too big, and message size 0");
				System.out.println(inputArray[99999999]); // used to crash out of the system
			}
			System.out.println("message size is 0, i is "+i); 
			return null;
		}
		
		int messageType = toInt(inputArray[(i+2)%arraySize], inputArray[(i+3)%arraySize]);
		System.out.print("new message of size "+messageSize+". i="+i);
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
//			i= (i+messageSize)%arraySize;
//			System.out.println(" ignored, type was "+messageType+" and i is now "+i);
			return null;
		}	
		i= (i+messageSize)%arraySize;
	
		ControllerMessage messageToSend = new ControllerMessage(message);
		
		return messageToSend;
	}
	
	public Message newAddMessage(int index) { // inputArray[index] is the start of the message
		byte[] message = new byte[22];
		message[0] = (byte)1; // 1 is the reference for a message of type Add
		for(int k= 1; k<5; k++) {
			message[j] = inputArray[index+j+4]; // inputArray[index+4..index+8) = the time reference;
		}
		for(int k = 5; k<9; k++) {
			message[j] = inputArray[index+j+8]; // inputArray[index+8..index+12) = the Symbol Index reference;
		}
		for(int k = 9; k<13; k++) {
			message[k] = inputArray[index+k+16]; // inputArray[index+16..index+20) = the OrderID reference;
		}
		for(int k = 13; k<17; k++) {
			message[k] = inputArray[index+k+20]; // inputArray[index+20..index+24) = the price reference;
		}
		message[17] = inputArray[index+28]; // inputArray[28] is the side reference. Note, this is currently stored as an ASCII Character
		for(int k = 18; k<22; k++) {
			message[k] = inputArray[index+k+24]; // inputArray[index+24..index+28) = the volume reference;
		}
		
		Message newMessage = new Message(message);
		
		return newMessage;
	}
	
	public Message newModifyMessage(int index) { // inputArray[index] is the start of the message
		byte[] message = new byte[22];
		message[0] = (byte)2; // 2 is the reference for a message of type Modify
		for(int k = 1; k<5; k++) {
			message[k] = inputArray[index+k+4]; // inputArray[index+4..index+8) = the time reference;
		}
		for(int k = 5; k<9; k++) {
			message[k] = inputArray[index+k+8]; // inputArray[index+8..index+12) = the Symbol Index reference;
		}
		for(int k = 9; k<13; k++) {
			message[k] = inputArray[index+k+16]; // inputArray[index+16..index+20) = the OrderID reference;
		}
		for(int k = 13; k<17; k++) {
			message[k] = inputArray[index+k+20]; // inputArray[index+20..index+24) = the price reference;
		}
		message[17] = inputArray[index+28]; // inputArray[28] is the side reference. Note, this is currently stored as an ASCII Character
		for(int k = 18; k<22; k++) {
			message[k] = inputArray[index+k+24]; // inputArray[index+24..index+28) = the volume reference;
		}
		Message newMessage = new Message(message);
		
		return newMessage;
	}
	
	public Message newDeleteMessage(int index) { // inputArray[index] is the start of the message
		byte[] message = new byte[22];
		message[0] = (byte)3; // 3 is the reference for a message of type Delete
		for(int k = 1; k<5; k++) {
			message[k] = inputArray[index+k+4]; // inputArray[index+4..index+8) = the time reference;
		}
		for(int k = 5; k<9; k++) {
			message[k] = inputArray[index+k+8]; // inputArray[index+8..index+12) = the Symbol Index reference;
		}
		for(int k = 9; k<13; k++) {
			message[k] = inputArray[index+k+16]; // inputArray[index+16..index+20) = the OrderID reference;
		}
		for(int k = 13; k<17; k++) {
			message[k] = 0; // delete messages have no price reference;
		}
		message[17] = inputArray[index+20]; // inputArray[20] is the side reference. Note, this is currently stored as an ASCII Character
		for(int k = 18; k<22; k++) {
			message[k] = 0; // delete messages have no volume reference;
		}
		Message newMessage = new Message(message);
		
		return newMessage;
	}
	
	public Message newExecuteMessage(int index) { // inputArray[index] is the start of the message
		byte[] message = new byte[22];
		message[0] = (byte)0; // 0 is the reference for a message of type Add
		for(int k = 1; k<5; k++) {
			message[k] = inputArray[index+k+4]; // inputArray[index+4..index+8) = the time reference;
		}
		for(int k = 5; k<9; k++) {
			message[k] = inputArray[index+k+8]; // inputArray[index+8..index+12) = the Symbol Index reference;
		}
		for(int k = 9; k<13; k++) {
			message[k] = inputArray[index+k+16]; // inputArray[index+16..index+20) = the OrderID reference;
		}
		for(int k = 13; k<17; k++) {
			message[k] = inputArray[index+k+20]; // inputArray[index+20..index+24) = the price reference;
		}
		message[17] = 2; // execute messages have no side reference
		for(int k = 18; k<22; k++) {
			message[k] = inputArray[index+k+24]; // inputArray[index+24..index+28) = the volume reference;
		}
		Message newMessage = new Message(message);
		
		return newMessage;
	}
}

// fixed the problem where strange packet and messages sizes etc. occurred, caused by an error
// when looping (skipping the final entry on inputArray)
// it seems to now work, but as of yet (the code is running in the background) it seems to only
// be full of Symbol Index Mapping control messages, like loads and loads of them. At least 20 minutes
// worth anyway. This isn't great really I feel for demonstration purposes

// could consider adding a counter to count how many bytes are boring stuff we don't do anything with
// then "skip" them and start after that

// also needs to incorporate the other three files I guess, although it seems that in a short demonstration
// we won't really have enough time for that to be needed




