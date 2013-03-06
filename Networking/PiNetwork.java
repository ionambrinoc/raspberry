
public class PiNetwork {
	private PiReceiver receiver;
	private PiSender sender;
	
	public PiNetwork(String recvAddr, String sendAddr) {
		receiver = new PiReceiver(recvAddr);
		sender = new PiSender(sendAddr);
	}
	
	public void start(){
		receiver.start();
	}
	
	public byte[] recv(){
		return receiver.recv();
	}
	
	public void sendToVisualization(byte[] msg){
		sender.send(msg);
	}
}
