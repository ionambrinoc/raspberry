package controller;

import java.util.ArrayList;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class PiManager {

	protected MessageSender messageSender;
	protected SymbolAssignment symbolAssignment;
	protected MessageHistory messageHistory;
	protected List<String> listOfPies;
	
	
	public PiManager (MessageSender messageSender, SymbolAssignment symbolAssignment, MessageHistory messageHistory) {
		this.messageSender = messageSender;
		this.symbolAssignment = symbolAssignment;
		this.messageHistory = messageHistory;
		listOfPies = new ArrayList<String>();
	}
	
	public void piUp(String piId) {
		if (!listOfPies.contains(piId)) {
			symbolAssignment.add(piId);
			System.out.println("Pi is up");
		}
	}
	
	public void piDown(String piId){
		if (listOfPies.contains(piId)) {
			String newPi = symbolAssignment.removePi(piId);
			List<byte[]> newOrders = messageHistory.piDown(piId, newPi);
			messageSender.sendMessage(newOrders, newPi);
		}
	}

	public void orderConfirmed(int orderId, String piId) {
		messageHistory.orderCompleted(orderId, piId);
		
	}

	public void executeMessage(byte[] message) {
		System.out.println("Message being executed");
		ByteBuffer bb = ByteBuffer.wrap(message);
		byte messageType = bb.get();
		
		int orderId = 0;
		if (messageType == 3) { orderId = bb.getInt(); System.out.println(orderId);}
		byte[] id = new byte[bb.remaining()];
		bb.get(id, 0, bb.remaining());
		System.out.println(new String(id));
		if (messageType == 1) {piUp(new String(id)); System.out.println("up");};
		if (messageType == 2) {piDown(new String(id)); System.out.println("out");};
		if (messageType == 3) {orderConfirmed(orderId, new String(id));};
		
	}

}
