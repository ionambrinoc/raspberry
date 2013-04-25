public class PiSimulator extends Thread{

	public static void main(String[] args) {
		PiNetwork network = new PiNetwork();
		
		while(true){
			byte[] msg = network.recv();
			try {
				// do somework
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			network.sendToToController(msg);
			network.sendToVisualization((new String(msg)+" result").getBytes());
		}
	}
}
