import org.zeromq.ZMQ;

public class PiSender {
	private ZMQ.Context context;
	private ZMQ.Socket senderToVisualization;
	
	public PiSender(String addr) {
		context = ZMQ.context(1);
		senderToVisualization = context.socket(ZMQ.PUSH);
		senderToVisualization.bind(addr);
	}
	
	public void send(byte[] msg){
		senderToVisualization.send(msg, 0);
	}
}
