public class Book 
{
	public Limit buyTree;		public Limit sellTree;
	
	public Limit lowestSell;	public Limit highestBuy;
	
	public Book()
	{
		buyTree=sellTree=null;
		lowestSell=highestBuy=null;
	}
	
	public void addOrder(int idNumber, boolean buy, int volume, int price, int time, 
				  String symbol)
	{
		if (buy)
		{
			Limit currentLimit = buyTree;
			while (price<currentLimit.limitPrice) currentLimit=currentLimit.leftChild;
			while (price>currentLimit.limitPrice) currentLimit=currentLimit.rightChild;
			if (price==currentLimit.limitPrice) currentLimit.addOrder(idNumber, buy, volume, time, symbol);
				else if (price>currentLimit.limitPrice)
				{
					currentLimit.rightChild = new Limit(price, currentLimit.parent, currentLimit, currentLimit.rightChild)
				}
 		}
		// then send confirmation back to server
	}
	
	public void modifyOrder(int idNumber, int vol, int price)
	{
		
		// then send confirmation back to server
	}
	
	public void removeOrder(int idNumber)
	{
		
		// then send confirmation back to server
	}
	
	public void execute()
	{
		
		// pass confirmation back to server
		
		// pass to visualisation the following: symbol, price, volume
	}
	
}
