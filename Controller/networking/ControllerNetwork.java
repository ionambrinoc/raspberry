package networking;


import org.zeromq.ZContext;
import org.zeromq.ZMQ;

/**
 * Create an instance of this class
 * And use only this class to send and recv msg
 * Call send(msg, identity) to send a msg to a specific worker
 * Call hasMessage() to check if there is an msg received
 * Call nextMessage() to recv a msg in bytes
 * 
 * @author yuchen
 *
 */

public class ControllerNetwork {
	private ZContext context;
	private ControllerVentilator ventilator;
	private ZMQ.Socket controller;
	private ZMQ.Poller poller;
	
	public ControllerNetwork() {
		context = new ZContext();
		controller = context.createSocket(ZMQ.PAIR);
		controller.bind("inproc://tocontroller");
		ventilator = new ControllerVentilator(context);
		ventilator.start();
		
		poller = new ZMQ.Poller(1);
		poller.register(controller, ZMQ.Poller.POLLIN);
	}
	/**
	 * send a msg to a particular worker
	 * 
	 * @param msg		The message you want to send to a worker
	 * @param identity	The identity of a worker
	 */
	public void send(byte[] msg, String identity){
		controller.sendMore(identity);
		controller.sendMore("");
		controller.send(msg, 0);
	}
	/**
	 * use this method to check is there a message coming
	 * Call this method before call has Message, so that you won't be blocked
	 * 
	 * @return	True if there is at least one message, False if there isn't any
	 */
	public boolean hasMessage(){
		poller.poll(0);
		return poller.pollin(0);
	}
	
	/**
	 * Call this method to receive a message
	 * This method can block if there isn't any message coming,
	 * so call hasMessage() before call nextMessage
	 * 
	 * Message format:
	 * [1][ID]			for a worker is up
	 * [2][ID]			for a worker is down
	 * [3][MSG][ID]		for MSG sent by a worker
	 * 
	 * @return	The message in bytes:
	 */
	public byte[] nextMessage(){
		return controller.recv();
	}
}