package networking;

public class ControllerSimulator{
	private static ControllerNetwork network;
	
	public static void main(String[] args) {
		network = new ControllerNetwork();
		while(true){
			network.send("hello".getBytes(), "192.168.1.100");
		}
	}
}
