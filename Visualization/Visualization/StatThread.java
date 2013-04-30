package Visualization;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class StatThread extends Thread{
	VisualizationNetwork network;
	LinkedList<Statistic> list;//? extends Statistic
	
	public StatThread(){
		network = new VisualizationNetwork();
		list = new LinkedList<Statistic>();//? extends Statistic
	}
	
	public void updateList() throws InterruptedException{
		Statistic statistic = null;
		byte[] bs = network.recv();
		ByteBuffer stat = ByteBuffer.wrap(bs);
		int symbol = stat.getInt(4);
		switch (symbol){
			case 1: statistic = new LastOrderPrice(bs);
		}
		list.add(statistic);
		/*byte[] bs = {
				0,0,0,(byte)1,
				0,0,0, (byte)1,
				0,(byte)255,0,0,
				(byte)127,0,0,0};
		Statistic statistic = new LastOrderPrice(bs);
		list.add(statistic);*/	
	}
	public Statistic getStatistic(){
		Statistic stat = list.getLast();
		list.clear();
		return stat;
	}
}
