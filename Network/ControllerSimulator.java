public class ControllerSimulator{
	private static ControllerNetwork network;
	static String worker = null;
	
	private static void processMsg(byte[] msg){
			byte[] id;
			switch (msg[0]){
				case 1:
					id = new byte[msg.length-1];
					System.arraycopy(msg, 1, id, 0, msg.length-1);
					worker = new String(id);
					System.out.println(worker+" is up");
					break;
				case 2:
					id = new byte[msg.length-1];
					System.arraycopy(msg, 1, id, 0, msg.length-1);
					System.out.println(new String(id)+" is down");
					worker = null;
					break;
				case 3:
					id = new byte[msg.length-3];
					System.arraycopy(msg, 3, id, 0, msg.length-3);
//					System.out.println("confirmation from :"+new String(id));
					worker = new String(id);
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
			if(worker == null){
				System.out.println("waiting for worker");
				byte[] msg = network.nextMessage();
				processMsg(msg);
			}else{
				i = (i+1)%100;
				network.send("hello".getBytes(), worker);
				if(i == 0) worker = null;
			}
		}
	}
	
}
