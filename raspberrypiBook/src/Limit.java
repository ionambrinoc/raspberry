package main;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Limit  //    each limit is a doubly linked list of limit objects
{
	private final int hashNumber = 647;
	
	public final int limitPrice;			public int totalVolume;
	
	public Limit nextLimit;					public Limit prevLimit;
	
	public Order headOrder;					public Order tailOrder;
	private ArrayList<WeakReference<Order>> hashTable = new ArrayList<WeakReference<Order>>(hashNumber); 
	
	private int hasher (Order order)
	{	return order.idNumber%hashNumber; }
	
	private int hasher (int idNumber)
	{	return idNumber%hashNumber;		  }
	
	public Limit (int price, Limit prevLimit, Limit nextLimit)
	{
		this.limitPrice=price;					this.prevLimit    = prevLimit;
		totalVolume        =0;					this.nextLimit    = nextLimit;
		for (int i=0; i< hashNumber; i++) hashTable.add(null);
	}
	
	public void addOrder(Order order)
	{
		int hash = hasher(order); 
		order.parentLimit=this;
		if (headOrder==null) 
		{ 
			headOrder = order; 
			order.prevOrder=null; 
			order.nextOrder=null; 
			tailOrder = order; 
			hashTable.set(hash, new WeakReference<Order>(order));
		} //empty list case
			else
			{
				if (hashTable.get(hash) == null) 
				{
					hashTable.set(hash, new WeakReference<Order>(order));
					order.prevOrder=tailOrder;
					tailOrder.nextOrder=order;
					tailOrder=order; tailOrder.nextOrder=null;  // add to tail	
				}
				else
				{
					
					WeakReference<Order> ref = hashTable.get(hash); 
					Order searcher = ref.get();
					while (searcher.nextOrder !=null && hasher(searcher.nextOrder.idNumber) == hash) 
								searcher = searcher.nextOrder;
					order.prevOrder = searcher;
					order.nextOrder = searcher.nextOrder;
					searcher.nextOrder=order;
				}		
			}
		totalVolume+=order.getVolume();
	}
	
	public Order getOrder(int idNumber)
	{
		if (hashTable.get(hasher(idNumber))==null) return null;
		Order current = hashTable.get(hasher(idNumber)).get(); 
		while (current.nextOrder != null) if (current.idNumber==idNumber) return current;
									else current=current.nextOrder; 
		return current;
	}
	
	public Order getOrderBySymbol(int symbol)  // get order by symbol; returns latest order found
	{
		Order current = headOrder; if (headOrder==null) return null;
		while (current != null) if (current.symbol==symbol) return current;
									else current=current.nextOrder; 
		return null;
	}
	
	public void removeOrder (int idNumber) throws Throwable
	{
		Order current = getOrder(idNumber); System.out.println(current.idNumber);
		int vol = current.getVolume();
		if (current.idNumber==headOrder.idNumber) headOrder=current.nextOrder;
		if (current.idNumber==tailOrder.idNumber) tailOrder=current.prevOrder;
		if (current.prevOrder != null) current.prevOrder.nextOrder = current.nextOrder; 
		if (current.nextOrder != null) current.nextOrder.prevOrder = current.prevOrder; 
		totalVolume-=vol;
		if (totalVolume==0) { prevLimit.nextLimit=nextLimit; nextLimit.prevLimit=prevLimit; prevLimit=null; nextLimit=null; }
	}
	
	public void modifyOrder (int idNumber, int vol, int price)
	{	getOrder(idNumber).modify(vol,  price);   	}
}