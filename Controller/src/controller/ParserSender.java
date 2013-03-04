package controller;
import org.zeromq.ZMQ;

public class ParserSender extends Thread{
	private ZMQ.Context context;
	private ZMQ.Socket sender;

	public ParserSender() {
		context = ZMQ.context(1);
		sender = context.socket(ZMQ.PUSH);
		sender.bind("tcp://192.168.1.100:10000");
	}

	public void send(byte[] msg, int pid) {
		sender.send(msg, 0);
	}

	public void terminate(){
		sender.close();
		context.term();
	}
}