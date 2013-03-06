import org.zeromq.ZMQ;

public class PiReceiver extends Thread{
	private ZMQ.Context context;
	private ZMQ.Socket receiver;
	
	public PiReceiver(String addr) {
		context = ZMQ.context(1);
		receiver = context.socket(ZMQ.PULL);
		receiver.connect(addr);
	}
	
	public byte[] recv(){
		return receiver.recv(0);
	}
}
