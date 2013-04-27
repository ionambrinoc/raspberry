import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Book 
{
	private final int hashNumber = 647;
	
	public Limit buyTree;		public Limit sellTree;
	public Limit currentBuy;    public Limit currentSell;
	
	public Limit lowestSell;	public Limit highestBuy;
	private Order headOrder;	private Order tailOrder;
	
	private ArrayList<WeakReference<Limit>> buyHashTable = new ArrayList<WeakReference<Limit>>(hashNumber); 
	private ArrayList<WeakReference<Limit>> sellHashTable = new ArrayList<WeakReference<Limit>>(hashNumber); 
	private ArrayList<WeakReference<Order>> orderHashTable = new ArrayList<WeakReference<Order>>(hashNumber);
	
	private int hasher (int price)
	{	return price%hashNumber;		  }
	
	public Book()
	{
		buyTree=sellTree=null;
		lowestSell=highestBuy=null;
		for (int i=0; i< hashNumber; i++) 
			{ buyHashTable.add(null);  sellHashTable.add(null); orderHashTable.add(null); }
	}
	
	public void addOrder(int idNumber, boolean buy, int volume, int price, int time, 
				  String symbol)
	{
		Order order = new Order(idNumber,buy,volume,price,time,symbol,null, null, null, null, null);
		int hash = hasher(price);
		
		addOrderToHash(order);
				
		Limit limit = getLimit(price,buy);
		
		if (buy)
		{
			if (limit==null) limit =new Limit(price, null, null);
			
			if (buyTree==null) 
			{
				buyTree = limit;
				buyTree.nextLimit=null;
				buyHashTable.set(hash, new WeakReference<Limit>(buyTree));
				currentBuy=buyTree; highestBuy = buyTree;
			}
				else
				{	
					if (buyHashTable.get(hash)==null)
					{
						buyHashTable.set(hash, new WeakReference<Limit>(limit));
						limit.prevLimit = currentBuy;
						currentBuy.nextLimit=limit;
						currentBuy = limit;
						if (currentBuy.limitPrice>highestBuy.limitPrice) highestBuy=currentBuy;
					}
						else
						{
							WeakReference<Limit> ref = buyHashTable.get(hash);
							Limit searcher = ref.get();
							while (hasher(searcher.nextLimit.limitPrice) == hash && searcher.nextLimit !=null) 
										searcher = searcher.nextLimit;
							limit.prevLimit = searcher;
							limit.nextLimit = searcher.nextLimit;
							searcher.nextLimit=limit;		
						}
				}
			currentBuy.addOrder(order);			
		}
			else
			{
				if (limit==null) limit =new Limit(price, null, null);
				
				if (sellTree==null) 
				{			
					sellTree = limit;
					sellTree.nextLimit=null;
					sellHashTable.set(hash, new WeakReference<Limit>(sellTree));
					currentSell=sellTree; lowestSell = sellTree;
				}
					else
					{	
						if (sellHashTable.get(hash)==null)
						{
							sellHashTable.set(hash, new WeakReference<Limit>(limit));
							limit.prevLimit = currentSell;
							currentSell.nextLimit=limit;
							currentSell = limit; currentSell.nextLimit=null;
						}
							else
							{
								WeakReference<Limit> ref = sellHashTable.get(hash);
								Limit searcher = ref.get();
								
								if (price!=hash)
								{
									while (hasher(searcher.nextLimit.limitPrice) == hash && searcher.nextLimit !=null) 
												searcher = searcher.nextLimit;
									limit.prevLimit = searcher;
									limit.nextLimit = searcher.nextLimit;
									searcher.nextLimit=limit;
								}	
									else { 	limit=searcher; }
							}
						if (currentSell.limitPrice>lowestSell.limitPrice) lowestSell=currentSell;
					}
				currentSell.addOrder(order);	
			}
		// then send confirmation back to server
		
		System.out.println("Order added "+idNumber+" symbol "+symbol+" buy side "+buy+" volume "+volume+" price "+price+ "time "+time);
	}					
	
	private int hasher (Order order)
	{	return order.idNumber%hashNumber; }
	
	private void addOrderToHash(Order order)
	{
		int hash = hasher(order);
		
		if (headOrder==null) 
		{ 
			headOrder = order; 
			order.prevOrder=null; 
			order.nextOrder=null; 
			tailOrder = order; 
			orderHashTable.set(hash, new WeakReference<Order>(order));
		} //empty list case
			else
			{
				if (orderHashTable.get(hash) == null) 
				{
					orderHashTable.set(hash, new WeakReference<Order>(order));
					order.prevOrderInBook=tailOrder;
					tailOrder.nextOrderInBook=order;
					tailOrder=order; tailOrder.nextOrderInBook=null;  // add to tail	
				}
				else
				{
					WeakReference<Order> ref = orderHashTable.get(hash);
					Order searcher = ref.get();
					while (hasher(searcher.nextOrderInBook.idNumber) == hash && searcher.nextOrderInBook !=null) 
								searcher = searcher.nextOrderInBook;
					order.prevOrderInBook = searcher;
					order.nextOrderInBook = searcher.nextOrderInBook;
					searcher.nextOrderInBook=order;
				}		
			}
	}
	
	public Limit getLimit (int price, boolean buy)
	{
		Limit current;
		if (buy==true) current = highestBuy;
			else current = lowestSell;
		
		if (buy)  { if (buyHashTable.get(hasher(price))!=null)  current = buyHashTable.get(hasher(price)).get(); }
		if (!buy) { if (sellHashTable.get(hasher(price))!=null) current= sellHashTable.get(hasher(price)).get(); }	
		
		if (current!=null) while (current.nextLimit != null) if (current.limitPrice==price) return current;
									else current=current.nextLimit; 
		return current;
	}
	
	public Order getOrder(int idNumber)			// Works up until here.
	{
		if (orderHashTable.get(hasher(idNumber))==null) return null;
		Order current = orderHashTable.get(hasher(idNumber)).get(); 
		while (current.nextOrder != null) if (current.idNumber==idNumber) return current;
									else current=current.nextOrder; 
		return current;
	}
	
	/* GET ORDER BY TRADE SYMBOL */
	
	public void modifyOrder(int idNumber, int vol, int price)
	{
		Order order = getOrder(idNumber);
		order.modify(vol, price);
		order.parentLimit = getLimit(price, order.buy);
		
		System.out.println("Order modified: "+idNumber+" new volume "+vol+" new price "+price);
		// then send confirmation back to server
	}
	
	public void removeOrder(int idNumber) throws Throwable
	{	
		Order current = getOrder(idNumber);
		current.prevOrder.nextOrder = current.nextOrder; current.prevOrderInBook.nextOrderInBook = current.nextOrderInBook;
		current.nextOrder.prevOrder = current.prevOrder; current.nextOrderInBook.prevOrderInBook = current.prevOrderInBook;
		if (current.parentLimit.headOrder==current.parentLimit.tailOrder) //remove limit
			{
				current.parentLimit.prevLimit.nextLimit=current.parentLimit.nextLimit;
				current.parentLimit.nextLimit.prevLimit=current.parentLimit.prevLimit;
			}
		System.out.println("Order removed: "+idNumber);
	}
	
	public void execute(int thisIdNumber, int thatIdNumber) throws Throwable
	{
		Order one = getOrder(thisIdNumber); int oneVolume=one.getVolume();
		Order two = getOrder(thatIdNumber); int twoVolume=two.getVolume();
		
		if (oneVolume>twoVolume) {one.modify(oneVolume-twoVolume, one.getPrice()); removeOrder(thatIdNumber); }		
			else if (oneVolume<twoVolume) {two.modify(twoVolume-oneVolume, two.getPrice()); removeOrder(thisIdNumber); }
				else {removeOrder(thisIdNumber); removeOrder(thatIdNumber); }
		// pass confirmation back to server
		
		// pass to visualisation the following: symbol, price, volume
	}
}