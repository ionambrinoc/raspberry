import org.zeromq.ZMQ;

public class PiReceiver {

	public static void main(String[] args) {
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket receiver = context.socket(ZMQ.PULL);
		receiver.connect("tcp://192.168.1.100:10000");
		String str = new String(receiver.recv(0)).trim();
		System.out.println("received");
		System.out.println(str);
		receiver.close();
		context.term();
	}
}
