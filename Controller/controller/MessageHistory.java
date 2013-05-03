package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MessageHistory {
	
	protected MessageSender messageSender;
	protected ListOfPi history; //List of messages for each order id, list of order ids for each pi, list of pies
	
	public MessageHistory(MessageSender messageSender){
		this.messageSender = messageSender;
		history = new ListOfPi();
	}

	public void addMessage(byte[] message, Integer orderID, String pi) {
		Pi newPi = history.get(pi); // checks to see if any messages have been sent to this particular pi before
		if (newPi != null) { 
			if (newPi.listOfOrderID.containsKey(orderID)){ //checks to see if that orderid has been seen before
				newPi.listOfOrderID.get(orderID).add(message); //adds the message to the list associtated with that orderid
			}
			else {
				LinkedList<byte[]> newMessageList = new LinkedList<byte[]>();
				newMessageList.add(message);
				newPi.listOfOrderID.put(orderID, newMessageList); //Creates a new linked list with one element which is the message to be added
			}
		}
		else history.listOfPi = new ArrayList<Pi>();
	}
	
	public void orderCompleted(int orderId, String piId){
		history.get(piId).listOfOrderID.remove(orderId);
	}
	
	public List<byte[]> piDown(String piId, String newPi2) {
		List<byte[]> listOfMessagesToBeSent = new LinkedList<byte[]>();
		
		Pi oldPi = history.get(piId);
		Pi newPi = history.get(newPi2);
		
		Iterator<Integer> oldPiElements = oldPi.listOfOrderID.keySet().iterator();
		while (oldPiElements.hasNext()) {
			Integer currentOldPiElement = oldPiElements.next(); //iterate through the elements in the hash table associated with the pi that has just gone down
			Queue<byte[]> currentOldPiMessage = oldPi.listOfOrderID.get(currentOldPiElement); //Generate a list of all the messages associated with a particular orderID
			listOfMessagesToBeSent.addAll(currentOldPiMessage); //Add these messages to the output
			newPi.listOfOrderID.put(currentOldPiElement, currentOldPiMessage); //Add these orderID and associated messages to the new pis
		}
		
		
		return listOfMessagesToBeSent;
	}
	
	protected class Pi {
		public String name;
		public HashMap<Integer, Queue<byte[]>> listOfOrderID; //List of OrderIDs
		
	}
	
	protected class ListOfPi {
		public List<Pi> listOfPi;
		
		public ListOfPi(){
			listOfPi = new ArrayList<Pi>(10);
		}
		
		public Pi get(String piId){
			for (int i = 0; i < listOfPi.size(); i ++){
				if (listOfPi.get(i).name == piId) return listOfPi.get(i);
			}
			return null;
		}
	}
	
	


}
