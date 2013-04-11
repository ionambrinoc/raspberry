import java.util.ArrayList;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ControllerNetwork {
	private ZContext context;
	private ControllerVentilator ventilator;
	private ZMQ.Socket controller;
	private ZMQ.Poller poller;
	private ControllerNetworkListener listener;
	
	public ControllerNetwork(ControllerNetworkListener listener) {
		this.listener = listener;
		context = new ZContext();
		controller = context.createSocket(ZMQ.PAIR);
		controller.bind("inproc://tocontroller");
		ventilator = new ControllerVentilator(context, listener);
		ventilator.start();
		
		poller = new ZMQ.Poller(1);
		poller.register(controller, ZMQ.Poller.POLLIN);
	}
	
	public void send(byte[] msg, String identity){
		controller.sendMore(identity);
		controller.sendMore("");
		controller.send(msg, 0);
	}
	
	public boolean hasConfirmation(){
		poller.poll(1000);
		return poller.pollin(0);
	}
	
	public byte[] nextConfirmation(){
		return controller.recv();
	}
}