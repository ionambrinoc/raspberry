import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class Tree 
{
	private final int hashNumber = 647;
	public final boolean buySide;
	
	public Limit headLimit; public Limit tailLimit; public Limit extremeLimit; // highest buy or lowest sell
	private ArrayList<WeakReference<Limit>> hashTable = new ArrayList<WeakReference<Limit>>(hashNumber); 
	
	private int hasher (int price)
	{	return price%hashNumber;		  }
	
	public Tree(boolean side) 
	{
		this.buySide = side; 
		for (int i=0; i< hashNumber; i++) hashTable.add(null);
		headLimit = null;
		tailLimit = null;
		extremeLimit = null;
	}
	
	public void addLimit(int price)
	{
			int hash = hasher (price);
			Limit limit;
			
			if (headLimit == null)
			{
				limit = new Limit (price, null, null);
				headLimit = limit; 				tailLimit = limit; 
				headLimit.nextLimit = tailLimit; tailLimit.prevLimit=headLimit;
				hashTable.set(hash, new WeakReference<Limit>(limit));
			}
			else
			{
				if (hashTable.get(hash) == null) 
				{
					System.out.println("Adding limit"+price);
					limit = new Limit (price, tailLimit, headLimit);
					
					hashTable.set(hash, new WeakReference<Limit>(limit));
					tailLimit.nextLimit=limit; headLimit.prevLimit=limit;
					Limit oldTail = tailLimit;
					tailLimit=limit; // add to tail	
					limit.prevLimit = oldTail; limit.nextLimit = headLimit;
					System.out.println("Limit prev"+tailLimit.limitPrice+ "next" + tailLimit.prevLimit.limitPrice);
				}
				else
				{
					WeakReference<Limit> ref = hashTable.get(hash); //TODO: add limits in descending order of price for buy
					Limit searcher = ref.get();						//TODO: add limits in ascending order of price for sell
					if (buySide)
						{
							while (searcher.nextLimit !=null && hasher(searcher.nextLimit.limitPrice) == hash
									&& 	searcher.limitPrice>price) 
											searcher = searcher.nextLimit;
							if (searcher.limitPrice>price) limit = new Limit (price, searcher.prevLimit, searcher);
								else limit = new Limit(price, searcher, searcher.nextLimit);
							if (searcher.nextLimit==null) limit.nextLimit=headLimit;
							searcher.nextLimit=limit;
						}	
						else
							{
								while (searcher.nextLimit !=null && hasher(searcher.nextLimit.limitPrice) == hash
										&& 	searcher.limitPrice<price) 
											searcher = searcher.nextLimit;
								if (searcher.limitPrice<price) limit = new Limit (price, searcher.prevLimit, searcher);
									else limit = new Limit(price, searcher, searcher.nextLimit);
								if (searcher.nextLimit==null) limit.nextLimit=headLimit;
								searcher.nextLimit=limit;
							}
				}		
			}
			
			
			if (buySide) if (extremeLimit==null || price>extremeLimit.limitPrice) extremeLimit=limit;
			if (!buySide)if (extremeLimit==null || price<extremeLimit.limitPrice) extremeLimit=limit;
	}
	
	public WeakReference<Limit> getBucket (int price)
	{		return hashTable.get(hasher(price));	}
	
	public Limit getLimit (int price)
	{
		if (hashTable.get(hasher(price))==null) return null; 
		Limit current = hashTable.get(hasher(price)).get();
		while (current.nextLimit != null) if (current.limitPrice==price) return current;
									else current=current.nextLimit;
		if (current.limitPrice==price) return current;
			else return null;
	}
	
	public void removeLimit (int price) throws Throwable
	{
		Limit current = getLimit(price);
		if (current.prevLimit != null) current.prevLimit.nextLimit = current.nextLimit; 
		if (current.nextLimit != null) current.nextLimit.prevLimit = current.prevLimit; 
		if (headLimit==null && tailLimit == null) this.finalize();
	}

	public void addOrder (Order order)
	{
		if (order.buy==buySide)
		{
			
			Limit limit = getLimit(order.getPrice());
			if (limit==null) { addLimit(order.getPrice()); limit = tailLimit; }
			limit.addOrder(order);		
		}
		System.out.println("Order added" + order.idNumber); 
	}
}
