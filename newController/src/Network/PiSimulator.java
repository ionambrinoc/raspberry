package Network;

import Controller.Message;

public class PiSimulator{
	private static PiNetwork network;
	public static void main(String[] args) throws InterruptedException {
	
		network = new PiNetwork();
		int i = 0;
		while(true){
//			i = (i+1)%100;
			//network.sendToVisualization(network.recv());
			System.out.println(new Message(network.recv()).toString());
//			if(i==0) network.sendToToController("ok".getBytes());
		}
	}
}
