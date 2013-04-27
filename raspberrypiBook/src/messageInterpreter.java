public class messageInterpreter 
{

		public void messageDecoder (byte[] msg, Book book)
		{
			Message mssg = new Message(msg);
			if (mssg.type==100)  // add order message
				book.addOrder(mssg.id, mssg.buy, mssg.vol, mssg.price, mssg.time, mssg.symbol);
			if (mssg.type==101)  // modify order message
				book.modifyOrder(mssg.id, mssg.vol, mssg.price);
			if (mssg.type==102)
				try {	book.removeOrder(mssg.id);	} 
			    catch (Throwable e) { /* stub */	e.printStackTrace();		}
			
		}
}