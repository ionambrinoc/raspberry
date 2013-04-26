public class PiSimulator extends Thread{

	public static void main(String[] args) throws InterruptedException {
		PiNetwork network = new PiNetwork();
		
		while(true){
			network.sendToVisualization(network.recv());
		}
	}
}
