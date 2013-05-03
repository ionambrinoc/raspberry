package controller;

import java.util.Iterator;
import networking.ControllerNetwork;
import java.util.List;
public class MessageSender {
	protected ControllerNetwork controllerNetwork;
	public MessageSender(ControllerNetwork controllerNetwork){
		this.controllerNetwork = controllerNetwork;
	}

	public void sendMessage(byte[] newOrders, String newPi) {
//		controllerNetwork.send((newOrders), newPi);
		//System.out.println(new String(newPi.toString().getBytes()));
		System.out.println(newOrders.toString());
	}

	
	
	public void sendMessage(List<byte[]> newOrders, String newPi) {
		Iterator<byte[]> newOrdersIterator = newOrders.iterator();
		while (newOrdersIterator.hasNext()){
			sendMessage(newOrdersIterator.next(), newPi);
		}
		
	}



}
