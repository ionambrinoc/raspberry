package Controller;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageProcessor {

	protected ArrayList<String> workers;
	HashMap<String,Integer> idToSize;
	
	public MessageProcessor(ArrayList<String> workers, HashMap<String,Integer> idToSize){
		this.workers = workers;
		this.idToSize = idToSize;
	}
	
	void processMsg(byte[] msg){
		byte[] id;
		switch (msg[0]){
			case 1:
				id = new byte[msg.length-1];
				System.arraycopy(msg, 1, id, 0, msg.length-1);
				workers.add(new String(id));
				idToSize.put(new String(id), 0);
				System.out.println(workers+" is up");
				break;
			case 2:
				id = new byte[msg.length-1];
				System.arraycopy(msg, 1, id, 0, msg.length-1);
				System.out.println(new String(id)+" is down");
				idToSize.remove(new String(id));
				workers.remove(new String(id));
				break;
			case 3:
				id = new byte[msg.length-3];
				System.arraycopy(msg, 3, id, 0, msg.length-3);
//				System.out.println("Message received from "+new String(id));
				break;
		}
	}

}
