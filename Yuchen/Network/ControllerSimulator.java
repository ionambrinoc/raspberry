import java.util.ArrayList;

public class ControllerSimulator{
	private static ControllerNetwork network;
	static ArrayList<String> workers = new ArrayList<>();
	
	private static void processMsg(byte[] msg){
			byte[] id;
			switch (msg[0]){
				case 1:
					id = new byte[msg.length-1];
					System.arraycopy(msg, 1, id, 0, msg.length-1);
					workers.add(new String(id));
					System.out.println(workers+" is up");
					break;
				case 2:
					id = new byte[msg.length-1];
					System.arraycopy(msg, 1, id, 0, msg.length-1);
					System.out.println(new String(id)+" is down");
					workers.remove(new String(id));
					break;
				case 3:
					id = new byte[msg.length-3];
					System.arraycopy(msg, 3, id, 0, msg.length-3);
					System.out.println("Message received from "+new String(id));
					break;
			}
	}
	
	public static void main(String[] args) throws InterruptedException {
		network = new ControllerNetwork();
		int i = 0;
		while(true){
			if(network.hasMessage()){
				byte[] msg = network.nextMessage();
				processMsg(msg);
			}
			if(workers.isEmpty()){
				System.out.println("waiting for worker");
				byte[] msg = network.nextMessage();
				processMsg(msg);
			}else{
				for(String worker : workers)
					network.send("hello".getBytes(), worker);
			}
		}
	}
	
}
