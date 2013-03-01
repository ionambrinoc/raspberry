import org.zeromq.ZMQ;

public class PiReceiver extends Thread{
	private ZMQ.Context context;
	private ZMQ.Socket receiver;
	
	public PiReceiver() {
		context = ZMQ.context(1);
		receiver = context.socket(ZMQ.PULL);
		receiver.connect("tcp://192.168.1.100:10000");
	}
	
	public byte[] getNextMsg(){
		return receiver.recv(0);
	}
	
	public void terminate(){
		receiver.close();
		context.term();
	}
}
