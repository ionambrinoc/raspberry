package networking;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

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
	
	public void send(byte[] msg, String identity){
		controller.sendMore(identity);
		controller.sendMore("");
		controller.send(msg, 0);
	}
	
	public boolean hasMessage(){
		poller.poll(1000);
		return poller.pollin(0);
	}
	
	public byte[] nextMessage(){
		return controller.recv();
	}
}