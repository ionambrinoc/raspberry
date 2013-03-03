public class Limit  //    each limit is a doubly linked list of limit objects
{
	public final int limitPrice;			public int totalVolume;
	
	public Limit parent;	public Limit leftChild;		public Limit rightChild;
	
	public Order headOrder;					public Order tailOrder;
	
	public Limit (int price, Limit parent, Limit leftChild, Limit rightChild)
	{
		this.limitPrice=price;
		totalVolume        =0;					this.parent    =null;
		headOrder       =null;					this.leftChild =null;
		tailOrder       =null;					this.rightChild=null;
	}
	
	public void addOrder(int idNumber, boolean buy, int volume, int time, String symbol)
	{	
		headOrder = new Order(idNumber, buy, volume, limitPrice, time, symbol, null, headOrder, this);   
		totalVolume+=volume;
	}
	
	public Order getOrder(int idNumber)
	{
		Order current = headOrder;
		while (current != null) if (current.idNumber==idNumber) return current;
									else current=current.nextOrder; 
		return null;
	}
	
	public Order getOrder(String symbol)  // get order by symbol; returns latest order found
	{
		Order current = headOrder;
		while (current != null) if (current.symbol==symbol) return current;
									else current=current.nextOrder; 
		return null;
	}
	
	public void removeOrder (int idNumber)
	{
		Order current = getOrder(idNumber);
		current.prevOrder.nextOrder = current.nextOrder;
		current.nextOrder.prevOrder = current.prevOrder;
	}
	
	public void modifyOrder (int idNumber, int vol, int price)
	{	getOrder(idNumber).modify(vol,  price);   	}
}