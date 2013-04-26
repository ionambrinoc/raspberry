
public class ApplicationFrame 
{
	
	
	public static void main(String[] args) throws InterruptedException
	{
		Limit limit = new Limit(100, null, null);
		Book book = new Book();
		
		book.addOrder(1, false, 50, 30, 0, "Coal");
		book.addOrder(2, false, 40, 30, 1, "Oil");
		book.addOrder(3, false, 20, 25, 4, "Steam");
		book.modifyOrder(2, 60, 100);
/*		limit.addOrder(4, false, 30, 6, "Water");
		limit.addOrder(5, false, 10, 9, "Shit");
		limit.addOrder(651,false, 30, 5, "more shit");
		limit.addOrder(1298, false, 11, 8, "wat");*/
		
	/*	System.out.println(limit.getOrder(4).getVolume());
		System.out.println(limit.getOrder(651).symbol);
		System.out.println(limit.getOrder(1298).symbol);*/
		
		book.modifyOrder(2, 60, 100);
		
		System.out.println(book.getOrder(2).getVolume());
		System.out.println(book.getLimit(30,false).limitPrice);
	}
}