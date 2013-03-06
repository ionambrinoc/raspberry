package controller;

import java.util.ArrayList;
import java.util.Arrays;
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
	}

	public void addMessage(Byte[] message, String orderID, int pi) {
		Pi newPi = history.get(pi); // checks to see if any messages have been sent to this particular pi before
		if (newPi != null) { 
			if (newPi.listOfOrderID.containsKey(orderID)){ //checks to see if that orderid has been seen before
				newPi.listOfOrderID.get(orderID).add(message); //adds the message to the list associtated with that orderid
			}
			else newPi.listOfOrderID.put(orderID, new LinkedList<Byte[]>((message))); //Creates a new linked list with one element which is the message to be added
		}
		else history.listOfPi = new ArrayList<Pi>();
	}
	
	public void orderCompleted(int pi, String orderID){
		history.get(pi).listOfOrderID.remove(orderID);
	}
	
	public List<Byte[]> piDown(int oldPiAddress, int newPiAddress) {
		List<Byte[]> listOfMessagesToBeSent = new LinkedList<Byte[]>();
		
		Pi oldPi = history.get(oldPiAddress);
		Pi newPi = history.get(newPiAddress);
		
		Iterator<String> oldPiElements = oldPi.listOfOrderID.keySet().iterator();
		while (oldPiElements.hasNext()) {
			String currentOldPiElement = oldPiElements.next(); //iterate through the elements in the hash table associated with the pi that has just gone down
			Queue<Byte[]> currentOldPiMessage = oldPi.listOfOrderID.get(currentOldPiElement); //Generate a list of all the messages associated with a particular orderID
			listOfMessagesToBeSent.addAll(currentOldPiMessage); //Add these messages to the output
			newPi.listOfOrderID.put(currentOldPiElement, currentOldPiMessage); //Add these orderID and associated messages to the new pis
		}
		
		
		return listOfMessagesToBeSent;
	}
	
	protected class Pi {
		public int name;
		public HashMap<String, Queue<Byte[]>> listOfOrderID; //List of OrderIDs
		
	}
	
	protected class ListOfPi {
		public List<Pi> listOfPi;
		
		public ListOfPi(){
			listOfPi = new ArrayList<Pi>(10);
		}
		
		public Pi get(int nameOfPi){
			for (int i = 0; i < listOfPi.size(); i ++){
				if (listOfPi.get(i).name == nameOfPi) return listOfPi.get(i);
			}
			return null;
		}
	}
	
	


}
