package controller;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;
import networking.ControllerNetwork;
import java.util.List;
public class MessageSender {
	protected ControllerNetwork controllerNetwork;
	public MessageSender(ControllerNetwork controllerNetwork){
		this.controllerNetwork = controllerNetwork;
	}

	public void sendMessage(byte[] newOrders, Integer newPi) {
		controllerNetwork.send((newPi.toString().getBytes()), newPi);
		//System.out.println(new String(newPi.toString().getBytes()));
	}

	
	
	public void sendMessage(List<byte[]> newOrders, int newPi) {
		Iterator<byte[]> newOrdersIterator = newOrders.iterator();
		while (newOrdersIterator.hasNext()){
			sendMessage(newOrdersIterator.next(), newPi);
		}
		
	}



}
