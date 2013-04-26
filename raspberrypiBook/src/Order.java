public class Order implements Comparable<Order>
{
	public final int idNumber;
	public final boolean buy; //true for buy, false for sell;
	public final String symbol;		public final int time;
	private int volume;          	private int price; //limit price
	
	public Order prevOrder;      	public Order nextOrder;
	public Order prevOrderInBook;   public Order nextOrderInBook;
	public Limit parentLimit;
	
	public Order (int idNumber, boolean buy, int volume, int price, int time, 
				  String symbol, Order prevOrder, Order nextOrder, Limit parentLimit,
				  Order prevOrderInBook, Order nextOrderInBook)
	{
		this.idNumber=idNumber;       this.parentLimit=parentLimit;
		this.buy     =     buy;		  this.nextOrder  = nextOrder;
		this.volume  =  volume;		  this.prevOrder  = prevOrder;
		this.time    =    time;		  this.symbol     =    symbol;
		this.prevOrderInBook=prevOrderInBook;
		this.nextOrderInBook=nextOrderInBook;
	}
	
	public int getVolume() { return volume; }
	public int getPrice()  { return price;  }
	
	public void modify(int vol, int price)
	{
		if (vol!=0)   this.volume = vol;
		if (price!=0) this.price = price;
	}

	public int compareTo(Order order) 
	{	
		if (order.price==this.price)
				return this.time-order.time;
			else return this.price-order.price;   	
	}
}