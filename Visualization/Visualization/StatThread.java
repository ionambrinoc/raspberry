package Visualization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class StatThread{
	VisualizationNetwork network;
	HashMap<String,LinkedBlockingDeque<Statistic>> map;
	HashMap<String,StreamListener> listeners;
	
	public StatThread(){
		network = new VisualizationNetwork();
		map = new HashMap<String,LinkedBlockingDeque<Statistic>>();
		listeners = new HashMap<String,StreamListener>();
	}
	
	public void updateList() throws InterruptedException{
		byte[] bs = network.recv();
		Statistic statistic = new Statistic(bs);
		
		// Add to the correct queue for the symbol of the statistic
		LinkedBlockingDeque<Statistic> q = map.get(statistic.getSymbol());
		if(q == null){
			q = new LinkedBlockingDeque<Statistic>(1000);
			map.put(statistic.getSymbol(), q);
		}

		if(q.size() == 1000) q.takeFirst();
		q.putLast(statistic);
		
		fireUpdate(statistic);
		
	}
	
	protected void fireUpdate(Statistic s){
		String symbol = s.getSymbol();
		StreamListener l = listeners.get(symbol);
		LinkedBlockingDeque<Statistic> q = map.get(symbol);
		if (l!=null) l.dataUpdate(q);
	}
	
	public void addStreamListener(String symbol, StreamListener l){
		listeners.put(symbol, l);
	}
	
	public void removeStreamListener(String symbol){
		listeners.remove(symbol);
	}

	public List<Statistic> peekStatistic(){
		ArrayList<Statistic> list = new ArrayList<Statistic>();
		for (LinkedBlockingDeque<Statistic> q: map.values()){
			Object o = q.peekLast();
			if (o != null)
				list.add((Statistic)o);
		}
		//Make Chart implement DataListener, and add itself to a list of listeners in this class.
		//fireNewData(data) to Chart.
		//Chart implements DataListener has newData(data){add(data)}
		//When a chart closes, listener is remove from list of listeners in this class. (ChartFrameFactory.internalFrameClosed)
		return list;
	}
}
