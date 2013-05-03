package networking;



import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;
import org.zeromq.ZMQ.Socket;

public class ControllerVentilator extends Thread{
	// Send heartbeats every 1 second
	private static final int HEARTBEAT_INTERVAL = 1000;
	// Worker is regarded as dead after lost 3 continuous heartbeats
	private static final int HEARTBEAT_LIVENESS = 3;
	// Heartbeat signal
	private static final byte[] HEARTBEAT = {1};
	
	private ZContext context;
	private ZMQ.Socket pi;
	private ZMQ.Socket controller;
	private WorkersPool workers;
	
	private class Worker{
        ZFrame identity;	// Unique identity of each worker
        long expiry;		// Expiry time
        
        public Worker(ZFrame identity) {
            this.identity = identity;
            this.expiry = System.currentTimeMillis() + HEARTBEAT_INTERVAL
                    * HEARTBEAT_LIVENESS;
        }
        
        @Override
        public int hashCode() {
            return Arrays.hashCode(identity.getData());
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Worker))
                return false;
            Worker other = (Worker) obj;
            return Arrays.equals(identity.getData(), other.identity.getData());
        }
	}
	
	private class WorkersPool{
		private ZMQ.Socket pi;
		private Deque<Worker> workers = new ArrayDeque<Worker>();
		private final ZFrame heartbeatFrame = new ZFrame(HEARTBEAT);
        private long heartbeatAt = System.currentTimeMillis() + HEARTBEAT_INTERVAL;
        
        public WorkersPool(Socket pi) {
			this.pi = pi;
		}
        
        /**
         * Update the expiry time of a worker
         */
		public void workerUpdate(Worker worker) {
        	if(!workers.remove(worker)){
        		// One pi is up
        		System.out.println("Ventilator: "+worker.identity.toString()+" is connected");
        		byte[] id = worker.identity.getData();
        		System.out.println(new String(id));
        		byte[] msg = new byte[1+id.length];
        		msg[0] = 1; System.arraycopy(id, 0, msg, 1, id.length);
        		controller.send(msg, 0);
        	}
            workers.offerLast(worker);
        }
        
        public void sendHeartbeats() {
            // Send heartbeats to workers
            if (System.currentTimeMillis() >= heartbeatAt) {
                for (Worker worker : workers) {
//                	System.out.println("Ventilator: send heartbeat to " + worker.identity.toString());
                    worker.identity.sendAndKeep(pi, ZMQ.SNDMORE);
                    heartbeatFrame.sendAndKeep(pi);
                }
                heartbeatAt = System.currentTimeMillis() + HEARTBEAT_INTERVAL;
            }
        }
        
        /**
         * Remove expired workers.
         */
        public void purge() {
            for (Worker w = workers.peekFirst(); w != null
                    && w.expiry < System.currentTimeMillis(); w = workers
                    .peekFirst()) {
                ZFrame identity = workers.pollFirst().identity;
//                System.out.println("Ventilator: "+identity.toString()+" disconnected");
                byte[] id = identity.getData();
                byte[] msg = new byte[1+id.length];
                msg[0] = 2; System.arraycopy(id, 0, msg, 1, id.length);
                controller.send(msg, 0);
                identity.destroy();
            }
        }
	}
	
	public ControllerVentilator(ZContext context) {
		this.context = context;
		pi = context.createSocket(ZMQ.ROUTER);
		pi.bind("tcp://192.168.1.140:10000");
		controller = context.createSocket(ZMQ.PAIR);
		controller.connect("inproc://tocontroller");
		workers = new WorkersPool(pi);
	}
	
	@Override
	public void run() {
//		System.out.println("Ventilator: Ventilator starts");
		
		ZMQ.Poller poller = new ZMQ.Poller(2);
		poller.register(pi, ZMQ.Poller.POLLIN);
		poller.register(controller, ZMQ.Poller.POLLIN);
		
		while (!Thread.currentThread().isInterrupted()){
			
			if(poller.poll(1000) == -1){
				break;
			}
			
			if(poller.pollin(0)){
				ZMsg msg = ZMsg.recvMsg(pi);
				if(msg == null) 
					break;
				// Any message received, update the worker pool
				workers.workerUpdate(new Worker(msg.getFirst()));
				
				// If it is confirmation, send the msg part to controller channel
                if(msg.size() == 3)	{
                	byte[] msgFromPi = msg.getLast().getData();
                	byte[] id = msg.getFirst().getData();
                	byte[] msgToController = new byte[1+msgFromPi.length+id.length];
                	msgToController[0] = 3;
                	System.arraycopy(msgFromPi, 0, msgToController, 1, msgFromPi.length);
                	System.arraycopy(id, 0, msgToController, 1+msgFromPi.length, id.length);
                	controller.send(msgToController, 0);
                }
                // If it is heartbeat
                else if(msg.size() == 2){
//                	System.out.println("Ventilator: Heart beat received");
                }
                else{
                	System.out.println("Ventilator: invalid message from pi");
                	msg.dump(System.out);
                	break; 
                }
			}
			
			if(poller.pollin(1)){
				// receive msg from controller, forward it to the specific pi
				ZMsg msg = ZMsg.recvMsg(controller);
				if(msg.size() != 3){
					System.out.println("Ventilator: invalid msg from controller");
					msg.dump(System.out);
					break;
				}
				msg.send(pi);
			}
			
			workers.sendHeartbeats();
			workers.purge();
		}
	}
}
