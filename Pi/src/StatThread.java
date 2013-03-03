import java.util.PriorityQueue;


public class StatThread extends Thread{
	PriorityQueue q = new PriorityQueue();
	Statistic stat = reciever.recieve();
	q.add(stat);
}
