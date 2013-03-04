import java.util.HashMap;
import java.util.Map;


public class ControlNetwork {
	private Map<String, String> pis;
	private ControlerSender sender;
	private ControlerReceiver receiver;
	private PiTracker piTracker;
	
	public ControlNetwork() {
		pis = new HashMap<String, String>(5);
	}
	
	public void start(){
		sender.start();
		receiver.start();
		piTracker.start();
	}
	
	public void send(byte[] msg, String piId){
		sender.send(msg, pis.get(piId));
	}
	
	public byte[] getNextMsg(){
		return receiver.nextMsg();
	}
}