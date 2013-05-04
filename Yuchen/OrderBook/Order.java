public class Order {
	int orderId;
	boolean buyOrSell;
	int volume;
	int price;
	int time;
	Limit parentLimit;
	
	public Order(Message msg) {
		this.orderId = msg.id;
		this.buyOrSell = msg.buy;
		this.volume = msg.vol;
		this.price = msg.price;
		this.time = msg.time;
		this.parentLimit = null;
	}
}
