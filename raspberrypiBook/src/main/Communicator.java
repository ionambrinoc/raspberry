package main;



public class Communicator 
{
	private StatisticEngine engine;
	private Book book;
	private PiNetwork piNetwork;
	
	public Communicator (Book book, PiNetwork piNetwork)
	{
		this.book=book;
		this.piNetwork=piNetwork; this.engine= new StatisticEngine();
	}
	
	public void start() throws Throwable
	{
		while(true)		
		{
			Message message = new Message(piNetwork.recv());
			
			if (message.type==1)
				{
				  System.out.println("Received order "+message.id+" buy side "+message.buy+" volume "+message.vol+" price "+ message.price + " at time "+ message.time + " symbol " + message.symbol  );
				  book.addOrder(new Order(message.id, message.buy, message.vol, message.price,
											message.time, message.symbol, null, null, null, null, null));
				  System.out.println("Added order "+message.id+" buy side "+message.buy+" volume "+message.vol+" price "+ message.price + " at time "+ message.time + " symbol " + message.symbol  );
				  piNetwork.sendToVisualization((engine.addData(message.symbol, message.price, message.vol, message.time)).toByte());
				}
			if (message.type==2)
				{
				  System.out.println("Modifying order "+message.id+" new volume "+message.vol+" new price "+message.price);
				  book.modifyOrder(message.id, message.vol, message.price);
				  System.out.println("Order "+message.id+" modified");
				}
			if (message.type==3)
				{
				  System.out.println("Removing Order "+message.id);
				  book.removeOrder(message.id);
				  System.out.println("Order "+message.id+" removed");
				  Confirmation confirmation = new Confirmation(message.id);
				  piNetwork.sendToToController(confirmation.toByte());
				}	
			
			if (message.type==0)  // trade execution message
				{
					int symbol = message.symbol; int vol=0;
					Order buy = book.getOrderBySymbol(symbol, true);
					Order sell = book.getOrderBySymbol(symbol, false);
					
					if (buy!=null && sell!=null)
					{	
					if (buy.getVolume()==sell.getVolume())
					{
						int buyId = buy.idNumber; int sellId = sell.idNumber;
						book.removeOrder(buyId); book.removeOrder(sellId);
						 System.out.println("Order "+buyId+" removed");
						 System.out.println("Order "+sellId+" removed");
						 Confirmation confirmation = new Confirmation(buyId);
						 piNetwork.sendToToController(confirmation.toByte());

						 confirmation = new Confirmation(sellId);
					     piNetwork.sendToToController(confirmation.toByte());
					     vol = buy.getVolume();
					}
					if (buy.getVolume()>sell.getVolume())
					{
						int buyId = buy.idNumber; int sellId = sell.idNumber;
						int sellVolume = sell.getVolume();
						book.removeOrder(sellId);
						 System.out.println("Order "+sellId+" removed");

						 Confirmation confirmation = new Confirmation(sellId);
					     piNetwork.sendToToController(confirmation.toByte());
					     
					     book.modifyOrder(buyId, buy.getVolume()-sellVolume, buy.getPrice());		
					     vol = sell.getVolume();
					}
					if (buy.getVolume()<sell.getVolume())
					{
						int buyId = buy.idNumber; int sellId = sell.idNumber;
						int buyVolume = buy.getVolume();
						book.removeOrder(buyId);
						 System.out.println("Order "+buyId+" removed");

						 Confirmation confirmation = new Confirmation(buyId);
					     piNetwork.sendToToController(confirmation.toByte());
					     
					     book.modifyOrder(sellId, sell.getVolume()-buyVolume, buy.getPrice());		
					     vol = buy.getVolume();
					}
					piNetwork.sendToVisualization((engine.addData(symbol, buy.getPrice(), vol, message.time)).toByte());
					}
				}	
		}
	}
}

// YUCHEN: please commit these to github :) thank you   ION