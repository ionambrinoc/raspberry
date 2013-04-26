public class PiSimulator extends Thread{

	public static void main(String[] args) throws InterruptedException {
		PiNetwork network = new PiNetwork();
		
		int i = 0;
		while(true){
			i = (i+1) % 5;
			network.sendToVisualization(network.recv());
			if(i == 0) network.sendToToController("ok".getBytes());
		}
	}
}
