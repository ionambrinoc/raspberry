import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

public class Limit {
	int price;			// limit price
	int volume;			// total volume
	PriorityQueue<Order> orders;// queue of orders, sorted by time, oldest first
	
	public Limit(Order order) {
		this.price = order.price;
		this.volume = 0;
		this.orders = new PriorityQueue<>(100, new Comparator<Order>() {
			public int compare(Order o1, Order o2) {
				return o2.time - o1.time;
			}
		});
		addOrder(order);
	}
	
	public void addOrder(Order order){
		order.parentLimit = this;
		orders.add(order);
		volume += order.volume;
	}
	
	public void deleteOrder(Order order){
		this.volume -= order.volume;
		orders.remove(order);
	}
	
	public void execute(int volume, Map<Integer, Order> orderMap){
//		int v = volume;
//		while(v>0){
//			Order o = orders.peek();
//			if(v>=o.volume){
//				deleteOrder(o);
//				orderMap.remove(o.orderId);
//				v -= o.volume;
//			}else{
//				o.volume -= v;
//				v = 0;
//			}
//		}
	}
	
	public void clear(Map<Integer, Order> orderMap){
//		for(Order o: orders){
//			orderMap.remove(o.orderId);
//		}
//		orders.clear();
	}
}
