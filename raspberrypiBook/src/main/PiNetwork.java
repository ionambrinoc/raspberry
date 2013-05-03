package main;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

/**
 * Create an instance of this class
 * And use only this class to send and recv msg
 * Call sendToVisualization(msg) to send a msg to a Visualization
 * Call sendToToController(msg) to send a msg to a Controller
 * Call recv() to recv a msg from Controller
 * 
 * @author yuchen
 *
 */

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
		visualization.connect("tcp://192.168.1.207:20000");
		
		collector = new PiCollector(context);
		collector.start();
	}
	/**
	 * Call this method to send a msg to Visualization
	 * 
	 * @param msg	The msg you want to send in bytes 
	 */
	public void sendToVisualization(byte[] msg){
		visualization.send(msg, 0);
	}
	/**
	 * Call this method to send a msg to Controller
	 * 
	 * @param msg	The msg you want to send in bytes 
	 */
	public void sendToToController(byte[] msg){
		pi.send(msg, 0);
	}
	/**
	 * Call this method to recv a msg from the Controller
	 * This method will wait, that means currently there is no work you
	 * If disconnected, the network thread will try to reconnect automatically
	 * If reconnected, the method will return when there is msg coming
	 * 
	 * @return		the message from controller
	 */
	public byte[] recv(){
		return pi.recv();
	}
}
