package Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import Network.ControllerNetwork;

public class Controller {

	static HashMap<Integer,String> symbolToId;// maps symbol to a Pi ID
	static HashMap<String,Integer> idToSize = new HashMap<String,Integer>();// maps PiID to number of symbols sent to that Pi
	
	public static void main(String[] args) {
		
		ControllerNetwork network = new ControllerNetwork();
		ArrayList<String> workers = new ArrayList<>();
		MessageProcessor msgProcessor = new MessageProcessor(workers,idToSize);
		LinkedBlockingQueue<Message> MessageQueue = new LinkedBlockingQueue<Message>(1000);
		symbolToId= new HashMap<Integer,String>();
		Parser parser = new Parser(MessageQueue);
		parser.start();
		
		while(true){
			if(network.hasMessage()){
				byte[] msg = network.nextMessage();
				msgProcessor.processMsg(msg);
			}
			if(workers.isEmpty()){
				System.out.println("waiting for worker");
				byte[] msg = network.nextMessage();
				msgProcessor.processMsg(msg);
			}else{
					try {
						Message msg = MessageQueue.take();
						int symbol = msg.symbol;
						byte[] byteMsg = msg.toBytes();
						network.send(byteMsg, chooseWorker(symbol,workers));
						System.out.println("Message sent");
					} catch (InterruptedException e) {
						System.out.println("Failed to take message");
						e.printStackTrace();
					}
			}
		}
	}

	//This method finds the Pi that deals with the symbol
	private static String chooseWorker(int symbol, ArrayList<String> workers) {
		if ((!symbolToId.containsKey(symbol)) || (symbolToId.containsKey(symbol) && !workers.contains((symbolToId.get(symbol))))){
			String minWorker = workers.get(0);
			System.out.println(minWorker);
			int minSize = idToSize.get(minWorker);
			for (String worker: workers){
				int size = idToSize.get(worker);
				if (size<minSize){
					minSize = size;
					minWorker = worker;
				}
			}
			symbolToId.put(symbol, minWorker);
			idToSize.put(minWorker, minSize+1);
		}
		return symbolToId.get(symbol);
	}

}
