package networking;

public class ControllerSimulator{
	private static ControllerNetwork network;
	
	public static void main(String[] args) throws InterruptedException {
		network = new ControllerNetwork();
		String worker = null;
		while(true){
			if(network.hasMessage()){
				byte[] msg = network.nextMessage();
				byte[] id;
				switch (msg[0]){
					case 0:
						id = new byte[msg.length-1];
						System.arraycopy(msg, 1, id, 0, msg.length-1);
						worker = new String(id);
						System.out.println(worker+" is up");
						break;
					case 1:
						id = new byte[msg.length-1];
						System.arraycopy(msg, 1, id, 0, msg.length-1);
						System.out.println(new String(id)+" is down");
						worker = null;
						break;
					case 2:
						System.out.println("Confirmation received");
						break;
				}
			}
			if(worker == null){
				System.out.println("waiting for worker");
				Thread.sleep(3000);
			}else{
				network.send("hello".getBytes(), worker);
			}
		}
	}
}
