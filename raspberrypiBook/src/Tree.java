import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class Tree 
{
	private final int hashNumber = 647;
	public final boolean buySide;
	private int size = 0;
	
	public Limit extremeLimit; // highest buy or lowest sell
	private ArrayList<WeakReference<Limit>> hashTable = new ArrayList<WeakReference<Limit>>(hashNumber); 
	
	private int hasher (int price)
	{	return price%hashNumber;		  }
	
	public Tree(boolean side) 
	{
		this.buySide = side; 
		for (int i=0; i< hashNumber; i++) hashTable.add(null);
		extremeLimit = null;
	}
	
	public Limit addLimit(int price)
	{
			int hash = hasher (price);
			Limit limit = null;
			
			System.out.println("Adding limit"+price);
			if (buySide)
			{
				if (extremeLimit==null)
				{
					limit = new Limit (price, null, null);
					hashTable.set(hash, new WeakReference<Limit>(limit));
					extremeLimit=limit;
					limit.prevLimit=limit; limit.nextLimit=limit;
					size=1;
				}
				else 
				{
					Limit current = extremeLimit; int steps=0;
					while (current.limitPrice>price && steps<size)
						{		current=current.nextLimit;	steps+=1;		}
					current=current.prevLimit;
					limit = new Limit (price, current, current.nextLimit);
					current.nextLimit.prevLimit=limit; current.nextLimit=limit;
					if (getBucket(price)==null) hashTable.set(hasher(price), new WeakReference<Limit>(limit));
					size+=1;
					if (limit.limitPrice>extremeLimit.limitPrice) extremeLimit=limit;
				}
			
			}
			else
			{
				if (extremeLimit==null)
				{
					limit = new Limit (price, null, null);
					hashTable.set(hash, new WeakReference<Limit>(limit));
					extremeLimit=limit;
					limit.prevLimit=limit; limit.nextLimit=limit;
					size=1;
				}
				else 
				{
					Limit current = extremeLimit; int steps=0;
					while (current.limitPrice<price && steps<size)
						{		current=current.nextLimit;		steps+=1;		}
					current=current.prevLimit;
					limit = new Limit (price, current, current.nextLimit);
					current.nextLimit.prevLimit=limit; current.nextLimit=limit;
					if (getBucket(price)==null) hashTable.set(hasher(price), new WeakReference<Limit>(limit));
					size+=1;
					if (limit.limitPrice<extremeLimit.limitPrice) extremeLimit=limit;
				}
			}
			return limit;
	}
	
	public WeakReference<Limit> getBucket (int price)
	{		return hashTable.get(hasher(price));	}
	
	public Limit getLimit (int price)
	{
		if (getBucket(price)==null) return null;
		Limit current = getBucket(price).get();
		int steps = 0;
		
		while (current.limitPrice != price && steps<size)
			{ current=current.nextLimit; steps+=1; }
		if (current.limitPrice!=price) return null;
			else return current;
	}
	
	
	public void removeLimit (int price) throws Throwable
	{
		Limit current = getLimit(price);
		if (current.prevLimit != null) current.prevLimit.nextLimit = current.nextLimit; 
		if (current.nextLimit != null) current.nextLimit.prevLimit = current.prevLimit; 
		if (extremeLimit == null) this.finalize();
	}

	public void addOrder (Order order)
	{
		if (order.buy==buySide)
		{
			
			Limit limit = getLimit(order.getPrice()); 
			
			if (limit==null) { limit=addLimit(order.getPrice());  }
			limit.addOrder(order);		
		}
		System.out.println("Order added" + order.idNumber); 
	}
}
