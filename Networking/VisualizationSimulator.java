
public class VisualizationSimulator {
	public static void main(String[] args) throws InterruptedException{
		VisualizationNetwork network = new VisualizationNetwork();
		network.start();
		
		while(true){
			System.out.println(new String(network.recv())+" received");
		}
	}
}
