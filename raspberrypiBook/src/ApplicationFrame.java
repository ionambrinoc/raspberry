
public class ApplicationFrame 
{	
	public static void main(String[] args) throws InterruptedException
	{
		Book book = new Book();
		
		book.addOrder(1, false, 50, 30, 0, "Coal");
		book.addOrder(2, false, 40, 30, 1, "Oil");
		book.addOrder(3, false, 20, 25, 4, "Steam");
		book.modifyOrder(2, 60, 100);
		
		book.modifyOrder(2, 60, 100);
		
		System.out.println(book.getOrder(2).getVolume());
		System.out.println(book.getLimit(30,false).limitPrice);
		
		Message mess = new Message(1,347,1048,300,666,false,333);
		byte[] m = mess.toBytes();
		Message me = new Message(m);
		System.out.println(me.type+" "+me.time+" "+me.id+" "+me.vol+" "+" "+ me.price+ " "+me.buy+" "+me.symbol+" ");
	}
}