import org.zeromq.ZMQ;

public class ControllerReceiver extends Thread{
	private ZMQ.Context context;
	private ZMQ.Socket receiver;
	private ControlNetworkListener listener;
	
	public ControllerReceiver() {
		context = ZMQ.context(1);
		receiver = context.socket(ZMQ.PULL);
		receiver.connect("tcp://192.168.1.100:10010");
	}
	
	@Override
	public void run() {
		while(true){
			listener.newMessage(receiver.recv(0));
		}
	}

	public void register(ControlNetworkListener listener){
		this.listener = listener;
	}
}