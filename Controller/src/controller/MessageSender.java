package controller;

import java.util.Iterator;
import java.util.List;
public class MessageSender {
	protected ParserSender parserSender;
	public MessageSender(){
		ParserSender parserSender = new ParserSender();
		parserSender.start();
	}

	public void sendMessage(String newOrders, int newPi) {
		byte[] newOrdersByte = newOrders.getBytes();
		parserSender.send(newOrdersByte, newPi);
	}

	
	
	public void sendMessage(List<String> newOrders, int newPi) {
		Iterator<String> newOrdersIterator = newOrders.iterator();
		while (newOrdersIterator.hasNext()){
			sendMessage(newOrdersIterator.next(), newPi);
		}
		
	}



}
