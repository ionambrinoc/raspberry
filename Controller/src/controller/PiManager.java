package controller;

import java.util.ArrayList;
import java.util.List;

import networking.ControllerNetwork;
import networking.ControllerNetworkListener;

public class PiManager implements ControllerNetworkListener {

	protected MessageSender messageSender;
	protected SymbolAssignment symbolAssignment;
	protected MessageHistory messageHistory;
	protected List<String> listOfPies;
	
	
	public PiManager (ControllerNetwork controllerNetwork, MessageSender messageSender, SymbolAssignment symbolAssignment, MessageHistory messageHistory) {
		this.messageSender = messageSender;
		this.symbolAssignment = symbolAssignment;
		this.messageHistory = messageHistory;
		listOfPies = new ArrayList<String>();
		controllerNetwork.addListener(this);
	}
	
	public void piUp(int pi) {
		if (!listOfPies.contains(pi)) {
			symbolAssignment.add(pi);
		}
	}
	
	public void piDown(int pi){
		if (listOfPies.contains(pi)) {
			int newPi = symbolAssignment.removePi(pi);
			List<byte[]> newOrders = messageHistory.piDown(pi, newPi);
			messageSender.sendMessage(newOrders, newPi);
		}
	}

	public void orderConfirmed(int orderId, int piId) {
		messageHistory.orderCompleted(orderId, piId);
		
	}

}
