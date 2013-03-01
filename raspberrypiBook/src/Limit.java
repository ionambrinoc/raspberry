
public class Limit  //each limit is a doubly linked list of limit objects
{
	int limitPrice;
	int totalVolume;
	
	Limit parent;
	Limit leftChild;
	Limit rightChild;
	
	Order headOrder;
	Order tailOrder;
	
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
}
