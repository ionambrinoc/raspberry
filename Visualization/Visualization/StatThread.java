package Visualization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import MapCreator.MapCreator;

public class StatThread{
	VisualizationNetwork network;
	HashMap<String,VisualizationQueue> map;
	//HashMap<String,LinkedBlockingDeque> map;//
	HashMap<String,StreamListener> listeners;
	MapCreator mapCreator;
	HashMap<Integer,Integer> scaleMap;
	HashMap<Integer,String> symbolMap;
	
	public StatThread(){
		network = new VisualizationNetwork();
		mapCreator = new MapCreator();
		scaleMap = mapCreator.getScaleMap();
		symbolMap = mapCreator.getSymbolMap();
		map = new HashMap<String,VisualizationQueue>();
		//map = new HashMap<String,LinkedBlockingDeque>();//
		listeners = new HashMap<String,StreamListener>();
	}
	
	public void updateList(boolean fire) throws InterruptedException{
		byte[] bs = network.recv();
		Statistic statistic = new Statistic(bs);
		updateStat(statistic);	
		System.out.println(statistic.toString());
		int n = 2000;
		// Add to the correct queue for the symbol of the statistic
		VisualizationQueue q = map.get(statistic.getSymbol());
		//LinkedBlockingDeque q = map.get(statistic.getSymbol());//
		if(q == null){
			q = new VisualizationQueue(n);
			//q = new LinkedBlockingDeque(n);
			map.put(statistic.getSymbol(), q);
		}

		if(q.size() == n) q.takeFirst();
		q.put(statistic);
		statistic = (Statistic) q.peekLast();
		if(fire)
			fireUpdate(statistic);
	}
	
	private void updateStat(Statistic statistic) {
		int sym = statistic.symInt;
		if(scaleMap.get(sym) != null){
			statistic.symbol = symbolMap.get(sym);
			double price = statistic.price;
			System.out.println(scaleMap == null);
			int scale = scaleMap.get(sym);
			for(int i=0; i<scale; i++) price/=10;
			statistic.actualPrice = price;
		}

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
