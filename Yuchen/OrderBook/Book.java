import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class Book {
	int symbol;
	MidData midData;
	Statistic statistic;
	PriorityQueue<Limit> buyTree;	// highest price first
	PriorityQueue<Limit> sellTree;// lowest price first
	HashMap<Integer, Limit> buyMap;	// highest price first
	HashMap<Integer, Limit> sellMap;// lowest price first
	Map<Integer, Order> orders = new HashMap<Integer, Order>();	 // orderId of this symbol
	
	private class MidData{
		int price = -1;	// current price
		int volume = 0;// volume
		long totalVolume = 0; // total volume
		int open = -1;	// open price
		int high = -1;	// high price
		int low = -1;	// low price
		long tvwp = 0;	// total volume weighted price
		long priceSum = 0;// sum of prices
		int time = -1;	// time
		
		ArrayList<Integer> prices = new ArrayList<Integer>(); // sorted price list
		
		int getChange(){ return (int)(((double)(price-open))/((double)open)*10000); }
		int getVWAP(){ return (int)(tvwp/totalVolume); }
		int getSMM(){ return prices.get(prices.size()/2); }
		int getSMA(){ return (int)(priceSum/prices.size()); }
		void putNewTrade(int price, int volume, int time){
			this.price = price;
			this.volume = volume;
			this.totalVolume += volume;
			if(open == -1) open = price;
			if(high == -1 || high<price) high = price;
			if(low == -1 || low>price) low = price;
			tvwp += ((long)price)*((long)volume);
			priceSum += price;
			this.time = time;
			prices.add(price);
			Collections.sort(prices);
		}
	}
	
	public Book(int symbol) {
		this.symbol = symbol;
		this.midData = new MidData();
		this.statistic = new Statistic(symbol, -1, 0, 0, -1, -1, -1, 0, -1, -1, -1);
		buyTree = new PriorityQueue<Limit>(100, 
				new Comparator<Limit>() {
					public int compare(Limit o1, Limit o2) {
						return o2.price-o1.price;
					}
				});
		sellTree = new PriorityQueue<Limit>(100, 
				new Comparator<Limit>() {
					public int compare(Limit o1, Limit o2) {
						return o1.price-o2.price;
					}
				});
		buyMap = new HashMap<Integer, Limit>();
		sellMap = new HashMap<Integer, Limit>();
	}
	
	public void addOrder(Order order){
		orders.put(order.orderId, order);
		
		if(order.buyOrSell){
			Limit limit = buyMap.get(order.price);
			if(limit == null){
				limit = new Limit(order);
				buyTree.add(limit);
				buyMap.put(order.price, limit);
			}else{
				order.parentLimit = limit;
				limit.addOrder(order);
			}
		}else{
			Limit limit = sellMap.get(order.price);
			if(limit == null){
				limit = new Limit(order);
				order.parentLimit = limit;
				sellTree.add(limit);
				sellMap.put(order.price, limit);
			}else{
				order.parentLimit = limit;
				limit.addOrder(order);
			}
		}
	}
	
	public void modifyOrder(Order newOrder){
		Order oldOrder = orders.get(newOrder.orderId);
		if(oldOrder != null)
			oldOrder.parentLimit.deleteOrder(oldOrder);
		else{
//			System.out.println("Book.Modify: orderId doesn't exist");
		}
		this.addOrder(newOrder);
	}
	
	public void deleteOrder(int orderId){
		Order order = orders.get(orderId);
		orders.remove(orderId);
		if(order != null){
			order.parentLimit.deleteOrder(order);
		}else{
//			System.out.println("Book.Delete: orderId doesn't exist");
		}
	}
	
	public void execute(int price, int volume, int time){

	}
	
	public Statistic trade(int price, int volume, int time) {
		midData.putNewTrade(price, volume, time);
		updateStatistic();
		return this.statistic;
	}
	
	private void updateStatistic(){
		statistic.price = midData.price;
		statistic.change = midData.getChange();
		statistic.volume = midData.volume;
		statistic.open = midData.open;
		statistic.high = midData.high;
		statistic.low = midData.low;
		statistic.vWAP = midData.getVWAP();
		statistic.sMM = midData.getSMM();
		statistic.sMA = midData.getSMA();
		statistic.time = midData.time;
	}


}