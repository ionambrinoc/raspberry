package main;


import java.lang.ref.WeakReference;
import java.util.ArrayList;



public class Book 
{
	private final int hashNumber = 647;
	
	public Tree BuyTree;			public Limit highestBuy;
	public Tree SellTree; 			private Limit lowestSell;
	
	public Order headOrder;					public Order tailOrder;
	private ArrayList<WeakReference<Order>> hashTable = new ArrayList<WeakReference<Order>>(hashNumber);
	
	public int hasher (int idNumber)
	{	return idNumber%hashNumber;		  }
	
	public Book()
	{
		BuyTree=new Tree(true); SellTree=new Tree(false);
		lowestSell=SellTree.extremeLimit; highestBuy=BuyTree.extremeLimit;
		for (int i=0; i< hashNumber; i++) hashTable.add(null);
	}
	
	public void addOrder(Order order)	//OK
	{
		int hash = hasher(order.idNumber);
		if (headOrder==null) 
		{ 
			headOrder = order; 
			order.prevOrderInBook=null; 
			order.nextOrderInBook=null; 
			tailOrder = order; 
			hashTable.set(hash, new WeakReference<Order>(order));
		} //empty list case
			else
			{
				if (hashTable.get(hash) == null) 
				{
					hashTable.set(hash, new WeakReference<Order>(order));
					order.prevOrderInBook=tailOrder;
					tailOrder.nextOrderInBook=order;
					tailOrder=order; tailOrder.nextOrderInBook=null;  // add to tail	
				}
				else
				{
					WeakReference<Order> ref = hashTable.get(hash); 
					Order searcher = ref.get(); 
				
					while (searcher.nextOrderInBook !=null && hasher(searcher.nextOrderInBook.idNumber) == hash) 
						searcher = searcher.nextOrder;
					
					order.prevOrderInBook = searcher; 
					order.nextOrderInBook = searcher.nextOrderInBook; 
					searcher.nextOrderInBook=order;
					
				}		
			}
		if (order.buy==true) BuyTree.addOrder(order);
			else SellTree.addOrder(order);
		lowestSell = SellTree.extremeLimit; highestBuy = BuyTree.extremeLimit;
	}
	
	public Order getOrder(int idNumber) //OK
	{
		if (hashTable.get(hasher(idNumber))==null) return null;
		Order current = hashTable.get(hasher(idNumber)).get(); 
		while (current.nextOrderInBook != null) if (current.idNumber==idNumber) return current;
									else current=current.nextOrderInBook; 
		return current;
	}

	public void removeOrder (int idNumber) throws Throwable //OK
	{
		Order current = getOrder(idNumber); System.out.println(current.idNumber);
		if (current.buy==true) 
			{
				BuyTree.getLimit(current.getPrice()).removeOrder(idNumber);
				if (BuyTree.getLimit(current.getPrice()).totalVolume==0) BuyTree.removeLimit(current.getPrice());
			}
			else 
				{
					SellTree.getLimit(current.getPrice()).removeOrder(idNumber);
					if (SellTree.getLimit(current.getPrice()).totalVolume==0) SellTree.removeLimit(current.getPrice());
				}
		if (current.prevOrderInBook !=null) current.prevOrderInBook.nextOrderInBook = current.nextOrderInBook;
		if (current.nextOrderInBook!=null) current.nextOrderInBook.prevOrderInBook = current.prevOrderInBook;
	}
	
	public void modifyOrder(int idNumber, int vol, int price) throws Throwable //OK
	{
		Order current = getOrder(idNumber);
		if (current.getPrice() != price)
		{
			Limit from = current.parentLimit;
			Limit to;
			if (current.buy)
			{
				to = BuyTree.getLimit(price);
				if (to==null) BuyTree.addLimit(price);
				to.addOrder(current);
				current.modify(vol, price);
			}
			else
				{
					to = SellTree.getLimit(price);
					if (to==null) SellTree.addLimit(price);
					to.addOrder(current);
					current.modify(vol, price);
				}
			from.removeOrder(idNumber);
		}	
		current.modify(vol, price);
	}

	public Order getOrderBySymbol(int symbol, boolean buy)
	{
		Limit limit; int price;
		if (buy)
		{
			limit = highestBuy; price = highestBuy.limitPrice;
			if (limit.getOrderBySymbol(symbol)!=null) return limit.getOrderBySymbol(symbol);
				else limit=limit.nextLimit;
			while (price!=limit.limitPrice && limit.getOrderBySymbol(symbol)==null) limit=limit.nextLimit;
			if (limit.getOrderBySymbol(symbol)!=null) return limit.getOrderBySymbol(symbol);
			return null;
		}
		else
			{
				limit = lowestSell; price = lowestSell.limitPrice;
				if (limit.getOrderBySymbol(symbol)!=null) return limit.getOrderBySymbol(symbol);
					else limit=limit.nextLimit;
				while (price!=limit.limitPrice && limit.getOrderBySymbol(symbol)==null) limit=limit.nextLimit;
				if (limit.getOrderBySymbol(symbol)!=null) return limit.getOrderBySymbol(symbol);
				return null;
			}
	}
}
