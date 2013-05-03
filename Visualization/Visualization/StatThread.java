package Visualization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatThread{
	VisualizationNetwork network;
	HashMap<String,VisualizationQueue> map;
	HashMap<String,StreamListener> listeners;
	
	public StatThread(){
		network = new VisualizationNetwork();
		map = new HashMap<String,VisualizationQueue>();
		listeners = new HashMap<String,StreamListener>();
	}
	
	public void updateList(boolean fire) throws InterruptedException{
		byte[] bs = network.recv();
		Statistic statistic = new Statistic(bs);
		int n = 50;
		// Add to the correct queue for the symbol of the statistic
		VisualizationQueue q = map.get(statistic.getSymbol());
		if(q == null){
			q = new VisualizationQueue(n);
			map.put(statistic.getSymbol(), q);
		}

		if(q.size() == n) q.takeFirst();
		q.put(statistic);
		statistic = q.peekLast();
		if(fire)
			fireUpdate(statistic);
		
	}
	
	protected void fireUpdate(Statistic s){
		String symbol = s.getSymbol();
		StreamListener l = listeners.get(symbol);
		if (l!=null) l.dataUpdate(s);
	}
	
	public void addStreamListener(String symbol, StreamListener l){
		listeners.put(symbol, l);
	}
	
	public void removeStreamListener(String symbol){
		listeners.remove(symbol);
	}

	public List<Statistic> peekStatistic(){
		ArrayList<Statistic> list = new ArrayList<Statistic>();
		for (VisualizationQueue q: map.values()){
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
