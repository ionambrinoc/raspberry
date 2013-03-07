public class PiMain {
	public static void main(String[] args){
		PiNetwork network = new PiNetwork("tcp://192.168.1.100:20000", "tcp://192.168.1.100:30000");
		
		while(true){
			byte[] msg = network.recv();
			System.out.println(new String(msg)+" received");
			network.sendToVisualization(msg);
			System.out.println("Forwarded to Visualization");
		}
	}
}
