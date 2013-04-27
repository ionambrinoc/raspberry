public class VisualizationSimulator {
	
	public static void main(String[] args){
		VisualizationNetwork network = new VisualizationNetwork();
		while(true){
			System.out.println(new String(network.recv()));
		}
	}
}
