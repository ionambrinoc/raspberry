package Network;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class PiCollector extends Thread{
	// Send heartbeats every 1 second
	private static final int HEARTBEAT_INTERVAL = 1000;
	// Worker is regarded as dead after lost 3 continuous heartbeats
	private static final int HEARTBEAT_LIVENESS = 3;
	// Heartbeat signal
	private static final byte[] HEARTBEAT = {1};
	
	private ZContext context;
	private ZMQ.Socket pi;
	
	private String id;
	private long ventilatorExpiry;
	private long heartbeatAt;
	
	public PiCollector(ZContext context) {
		this.context = context;
		pi = context.createSocket(ZMQ.PAIR);
		pi.connect("inproc://topi");
		id = getHostAddress();
	}
	
	private String getHostAddress(){
		try {
			Enumeration<NetworkInterface> interfaces;
			interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()){
			    NetworkInterface current = interfaces.nextElement();
			    if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
			    Enumeration<InetAddress> addresses = current.getInetAddresses();
			    while (addresses.hasMoreElements()){
			        InetAddress current_addr = addresses.nextElement();
			        if (current_addr instanceof Inet4Address) 
			        	return current_addr.getHostAddress();
			        else continue;
			    }
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

    private ZMQ.Socket connectVentilator(ZContext context) {
        ZMQ.Socket ventilator = context.createSocket(ZMQ.DEALER);
        // Set identity
        if(id != null) ventilator.setIdentity(id.getBytes());
        
        ventilator.connect("tcp://192.168.1.207:10000");
        updateVentilator();
        // Tell controller I am ready, send heartbeat
        sendHeartbeats(ventilator);
        return ventilator;
    }
    
    private void updateVentilator(){
    	ventilatorExpiry = System.currentTimeMillis() + 
				HEARTBEAT_INTERVAL * HEARTBEAT_LIVENESS;
    }
    
    private void sendHeartbeats(ZMQ.Socket ventilator){
    	if(System.currentTimeMillis() > heartbeatAt){
    		heartbeatAt = System.currentTimeMillis() + HEARTBEAT_INTERVAL;
//    		System.out.println("Collector: send heartbeat to Controller");
    		ventilator.send(HEARTBEAT, 0);
    	}
    }
	
	@Override
	public void run() {
		ZMQ.Socket ventilator = connectVentilator(context);
		
		ZMQ.Poller poller = new ZMQ.Poller(2);
		poller.register(ventilator, ZMQ.Poller.POLLIN);
		poller.register(pi, ZMQ.Poller.POLLIN);
		
		while (!Thread.currentThread().isInterrupted()){
			
			if(poller.poll(1000) == -1){
				break;
			}
			
			if(poller.pollin(0)){
				ZMsg msg = ZMsg.recvMsg(ventilator);
				if (msg == null){
					break;
				}
				updateVentilator();
				// If it is work, send the msg part to pi channel
                if (msg.size() == 2){
                	// will block here if too many messages
                	pi.send(msg.getLast().getData(), 0);
                }
                // If it is heartbeat
                else if(msg.size() == 1){
//                	System.out.println("Collector: Heartbeat from Controller received");
                }else{
                	System.out.println("Collector: invalid message from ip");
                	msg.dump(System.out);
                	break; 
                }
			}
			
			if(poller.pollin(1)){
				byte[] msg = pi.recv();
				ventilator.sendMore("");
				ventilator.send(msg, 0);
			}
			
			// Lost connection
			if(ventilatorExpiry < System.currentTimeMillis()){
				System.out.println("Collector: disconnected to Controller");
				// TODO Notify pi to clear cache
				context.destroySocket(ventilator);
				
				System.out.println("Collector: trying to reconnect in 3 seconds");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ventilator = connectVentilator(context);
				poller = new ZMQ.Poller(2);
				poller.register(ventilator, ZMQ.Poller.POLLIN);
				poller.register(pi, ZMQ.Poller.POLLIN);
				
				// TODO Notify pi when connection if back
			}
			
			sendHeartbeats(ventilator);
		}
	}
}
