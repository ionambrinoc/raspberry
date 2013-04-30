public class Order implements Comparable<Order>
{
	public final int idNumber;
	public final boolean buy; //true for buy, false for sell;
	public final int symbol;		public final int time;
	private int volume;          	private int price; //limit price
	
	public Order prevOrder; public Order nextOrder; public Limit parentLimit;
	public Order prevOrderInBook; public Order nextOrderInBook;
	
	public Order (int idNumber, boolean buy, int volume, int price, int time, 
				  int symbol, Order prevOrder, Order nextOrder, Limit parentLimit,
				  Order prevOrderInBook, Order nextOrderInBook)
	{
		this.idNumber=idNumber;		  this.parentLimit=parentLimit;
		this.buy     =     buy;		  this.prevOrder  = prevOrder;
		this.volume  =  volume;		  this.nextOrder  = nextOrder;
		this.time    =    time;		  this.symbol     =    symbol;
		this.prevOrderInBook=prevOrderInBook; this.price=price;
		this.nextOrderInBook=nextOrderInBook;
	}
	
	public int getVolume() { return volume; }
	public int getPrice()  { return price;  }
	
	public void modify(int vol, int price)
	{
		int pvol = this.volume;
		if (vol!=0)   this.volume = vol;
		if (price!=0) this.price = price;
		this.parentLimit.totalVolume=this.parentLimit.totalVolume-pvol+vol;
	}

	public int compareTo(Order order) 
	{	
		if (order.price==this.price)
				return this.time-order.time;
			else return this.price-order.price;   	
	}
}