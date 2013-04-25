package networking;
import java.util.concurrent.LinkedBlockingQueue;

import org.zeromq.ZMQ;
/**
 * Create an instance.
 * Call ParserSender.start() to start a new thread.
 * Call send(msg, pid) to send a msg to a pi with id pid.
 * Call terminate() to disconnect
 */
public class ControllerSender extends Thread{
	private ZMQ.Context context;
	private ZMQ.Socket sender;
//	private LinkedBlockingQueue<byte[]> blockingQueue;
	
	public ControllerSender(String addr, LinkedBlockingQueue<byte[]> blockingQueue) {
		context = ZMQ.context(1);
		sender = context.socket(ZMQ.PUSH);
		sender.bind(addr);
//		this.blockingQueue = blockingQueue;
	}
	
	@Override
	public void run() {
//		while(true){
//			System.out.println("start");
//			byte[] msg =blockingQueue.poll();
//			sender.send(msg, 0);
//			System.out.println("end");
//		}
	}

	public void terminate(){
		sender.close();
		context.term();
	}
	
	public void send(byte[] msg){
		System.out.println(new String(msg));
		//sender.send(msg, 0);
		
	}
}
