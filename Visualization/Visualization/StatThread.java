package Visualization;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class StatThread extends Thread{
	VisualizationNetwork network;
	LinkedList<Statistic> list;
	
	public StatThread(){
		network = new VisualizationNetwork();
		list = new LinkedList<Statistic>();
	}
	
	public void updateList() throws InterruptedException{
		byte[] bs = network.recv();
		ByteBuffer stat = ByteBuffer.wrap(bs);
		Statistic statistic = new Statistic(bs);
		list.add(statistic);
		}

	public Statistic getStatistic(){
		Statistic stat = list.getLast();
		list.clear();
		return stat;
	}
}
