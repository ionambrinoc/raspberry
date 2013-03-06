import java.util.PriorityQueue; 

public class StatThread extends Thread{
	
	VisualizationNetwork network = new VisualizationNetwork();
	PriorityQueue q = new PriorityQueue(0,new MsgCompare());
	Statistic stat = network.recv();
	q.add(stat);
}
