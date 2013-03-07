public class PiSimulator extends Thread{
	private PiNetwork network;

	public PiSimulator(String recvAddr, String sendAddr) {
		network = new PiNetwork(recvAddr, sendAddr);
	}

	public void run() {
		System.out.println("Pi start");
		while(true){
			byte[] msg = network.recv();
			System.out.println(new String(msg)+" received");
			network.sendToVisualization(msg);
			System.out.println("Forwarded to Visualization");
		}
	}
}
