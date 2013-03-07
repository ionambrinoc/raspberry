import org.zeromq.ZMQ;

public class VisualizationReceiver extends Thread{
	private ZMQ.Context context;
	private ZMQ.Socket receiver;
	private VisualizationNetwork network;
	
	public VisualizationReceiver(String addr, VisualizationNetwork network) {
		context = ZMQ.context(1);
		receiver = context.socket(ZMQ.PULL);
		receiver.connect(addr);
		this.network = network;
	}
	
	@Override
	public void run() {
		System.out.println("start");
		while(true){
			try {
				network.putMsg(receiver.recv(0));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
