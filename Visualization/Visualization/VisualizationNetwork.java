package Visualization;

import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class VisualizationNetwork {
	private ZContext context;
	private ZMQ.Socket visualization;
	private VisualizationCollector collector;
	
	private class VisualizationCollector extends Thread{
		private ZContext context;
		private ZMQ.Socket collector;
		private ZMQ.Socket visualization;
		
		public VisualizationCollector(ZContext context) {
			// prepare context and sockets
			this.context = context;
			collector = context.createSocket(ZMQ.ROUTER);
			collector.bind("tcp://192.168.1.100:20000");
			visualization = context.createSocket(ZMQ.PAIR);
			visualization.connect("inproc://tovisualization");
		}
		
		@Override
		public void run() {
			System.out.println("Visualization collector thread starts");
			
			ZMQ.Poller poller = new ZMQ.Poller(1);
			poller.register(collector, ZMQ.Poller.POLLIN);
			
			while(!Thread.currentThread().isInterrupted()){
				
				if(poller.poll(3000) == -1){
					System.out.println("polling time out");
					break;
				}
				
				if(poller.pollin(0)){
					ZMsg msg = ZMsg.recvMsg(collector);
					if (msg == null){
						System.out.println("recv msg fail");
						break;
					}
					
					ZFrame frame = msg.getLast();
					visualization.send(frame.getData(), 0);
					msg.destroy();
				}
			}
			
			context.destroy();
		}
	}

	public VisualizationNetwork() {
		context = new ZContext();
		visualization = context.createSocket(ZMQ.PAIR);
		visualization.bind("inproc://tovisualization");
		collector = new VisualizationCollector(context);
		collector.start();
	}
	
	public byte[] recv(){
		return visualization.recv();
	}
}
