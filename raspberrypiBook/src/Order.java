public class Order 
{
	public final int idNumber;
	public final boolean buy; //true for buy, false for sell;
	public final String symbol;
	private int volume;
	private int price; //limit price
	public final int time;
	
	public Order prevOrder;
	public Order nextOrder;
	private Limit parentLimit;
	
	public Order (int idNumber, boolean buy, int volume, int price, int time, 
				  String symbol, Order prevOrder, Order nextOrder, Limit parentLimit)
	{
		this.idNumber=idNumber;       this.parentLimit=parentLimit;
		this.buy     =     buy;		  this.nextOrder  = nextOrder;
		this.volume  =  volume;		  this.prevOrder  = prevOrder;
		this.time    =    time;		  this.symbol     =    symbol;
	}
	
	public int getVolume() { return volume; }
	public int getPrice()  { return price;  }
	
	public void modify(int vol, int price)
	{
		if (vol!=0)   this.volume = vol;
		if (price!=0) this.price = price;
	}
}
