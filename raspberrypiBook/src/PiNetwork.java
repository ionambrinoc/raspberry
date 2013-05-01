import java.nio.ByteBuffer;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class PiNetwork 
{
	private ZContext context;
	private PiCollector collector;
	private ZMQ.Socket pi;
	private ZMQ.Socket visualization;
	private Book book;

	public PiNetwork(Book book) {
		context = new ZContext();
		pi = context.createSocket(ZMQ.PAIR);
		pi.bind("inproc://topi");
		visualization = context.createSocket(ZMQ.DEALER);
		visualization.connect("tcp://192.168.1.207:20000");

		collector = new PiCollector(context);
		collector.start();
		this.book=book;
	}

	public void sendToVisualization(byte[] msg)  { 	visualization.send(msg, 0);	}

	public void sendToToController(byte[] msg)   { 	pi.send(msg, 0); 			}

	public byte[] recv()					     
			{	byte[] received = pi.recv(); return received; 	}
}