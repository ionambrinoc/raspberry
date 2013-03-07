package Visualization;
public class VisualizationNetwork {
	private VisualizationReceiver receiver;

	public VisualizationNetwork() {
		receiver = new VisualizationReceiver("tcp://192.168.1.100:30000");
	}
	
	public byte[] recv(){
		return receiver.recv();
	}
}
