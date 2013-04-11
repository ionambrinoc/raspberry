import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class PiNetwork {
	private ZContext context;
	private PiCollector collector;
	private ZMQ.Socket pi;
	private ZMQ.Socket visualization;
	
	public PiNetwork() {
		context = new ZContext();
		pi = context.createSocket(ZMQ.PAIR);
		pi.bind("inproc://topi");
		visualization = context.createSocket(ZMQ.DEALER);
		visualization.connect("tcp://192.168.1.100:20000");
		
		collector = new PiCollector(context);
		collector.start();
	}
	
	public void sendToVisualization(byte[] msg){
		visualization.send(msg, 0);
	}
	
	public void sendToToController(byte[] msg){
		pi.send(msg, 0);
	}
	
	public byte[] recv(){
		return pi.recv();
	}
}
