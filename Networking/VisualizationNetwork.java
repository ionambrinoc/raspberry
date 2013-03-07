import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class VisualizationNetwork {
	private BlockingQueue<byte[]> queue;
	private ArrayList<VisualizationReceiver> receivers;

	public VisualizationNetwork() {
		receivers = new ArrayList<VisualizationReceiver>(5);
		receivers.add(new VisualizationReceiver("tcp://192.168.1.100:30000", this));
		receivers.add(new VisualizationReceiver("tcp://192.168.1.100:30001", this));
		receivers.add(new VisualizationReceiver("tcp://192.168.1.100:30002", this));
		receivers.add(new VisualizationReceiver("tcp://192.168.1.100:30003", this));
		receivers.add(new VisualizationReceiver("tcp://192.168.1.100:30004", this));
		
		queue = new LinkedBlockingQueue<byte[]>(1000);
	}
	
	public byte[] recv() throws InterruptedException{
		return queue.take();
	}
	
	protected void putMsg(byte[] msg) throws InterruptedException{
		queue.put(msg);
	}
	
	public void start(){
		for(VisualizationReceiver receiver: receivers)
			receiver.start();
	}
}
