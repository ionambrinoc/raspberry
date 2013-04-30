
public class ApplicationFrame 
{	
	public static void main(String[] args) throws Throwable
	{
		Book book = new Book();
		
		Order order1 = new Order(1,true, 45, 30, 12,1212, null, null, null, null, null);
		Order order2 = new Order(2,true, 50, 30, 13,1212, null, null, null, null, null);
		Order order3 = new Order(3,true, 55, 40, 14,1214, null, null, null, null, null);
		Order order4 = new Order(4,true, 60, 40, 15,1210, null, null, null, null, null);
		Order order5 = new Order(5,true, 65, 20, 16,1212, null, null, null, null, null);
		Order order6 = new Order(6,true, 70, 30, 17,1213, null, null, null, null, null);
		Order order7 = new Order(648, true, 10, 30, 12, 1211, null, null, null, null, null);
		Order order8 = new Order(1295,true, 10, 25, 12, 1212, null, null, null, null, null);

		book.addOrder(order1); 
		book.addOrder(order2); 
		book.addOrder(order3);
		book.addOrder(order4); 
		book.addOrder(order5); 
		book.addOrder(order6);
		book.addOrder(order7);
		book.addOrder(order8);
		
		System.out.println(order1.parentLimit.prevLimit.prevLimit.limitPrice);
		 System.out.println(book.getOrderBySymbol(1214,true).getVolume());		
	}
}