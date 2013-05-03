

import java.util.concurrent.LinkedBlockingQueue;

public class Reader extends Thread {
	LinkedBlockingQueue<ControllerMessage> queue;
	Parser parser;
	
	public Reader(LinkedBlockingQueue<ControllerMessage> queue) {
		this.queue = queue;
		parser = new Parser();
	}

	public void run() {
		while(true){
			int num = 0;
			
			while(num == 0){
				num = parser.decodePacketHeader();
			}
			
			for(int j=0; j<num; j++){
				Message msg = parser.nextMessage();
				
				if(msg != null) {
					try {
						queue.put(new ControllerMessage(msg));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
