package networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ControllerNetwork {
	
	private Map<Integer, String> piAddrs;
	private ArrayList<ControllerSender> senders;
//	private ControlerReceiver receiver;
//	private ArrayList<LinkedBlockingQueue<byte[]>> blockingQueues;
//	private PiTracker piTracker;
	
	public ControllerNetwork() {
		piAddrs = new HashMap<Integer, String>(5);
		piAddrs.put(0, "tcp://192.168.1.100:20000");
		piAddrs.put(1, "tcp://192.168.1.100:20001");
//		piAddrs.put(2, "tcp://192.168.1.100:20002");
//		piAddrs.put(3, "tcp://192.168.1.100:20003");
//		piAddrs.put(4, "tcp://192.168.1.100:20004");
		
//		blockingQueues = new ArrayList<LinkedBlockingQueue<byte[]>>(5);
//		for(int i=0; i<5; i++){
//			blockingQueues.add(new LinkedBlockingQueue<byte[]>(1000));
//		}
		
		senders = new ArrayList<ControllerSender>(2);
		for(int i=0; i<2; i++)
			senders.add(new ControllerSender(piAddrs.get(i), null));
		
//		receiver = new ControlerReceiver();

//		piTracker = new PiTracker();
	}
	
	public void start(){
//		for(int i=0; i<2; i++)
//			senders.get(i).start();
//		receiver.start();
//		piTracker.start();
	}
	
	public void send(byte[] msg, int piId){
		senders.get(piId).send(msg);
	}
	public void addListener(ControllerNetworkListener newListener){
		
	}

	public void executeMessage() {
		// TODO Auto-generated method stub
		
	}

	public boolean getSema() {
		// TODO Auto-generated method stub
		return false;
	}
}
