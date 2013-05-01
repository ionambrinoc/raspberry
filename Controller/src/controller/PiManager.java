package controller;

import java.util.ArrayList;
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
		ByteBuffer bb = ByteBuffer.wrap(message);
		int messageType = bb.get();
		int orderId = 0;
		if (messageType == 3) {orderId = bb.getInt();}
		String piId = bb.asCharBuffer().toString();
		if (messageType == 1) {piUp(piId);};
		if (messageType == 2) {piDown(piId);};
		if (messageType == 3) {orderConfirmed(orderId, piId);};
		
	}

}
