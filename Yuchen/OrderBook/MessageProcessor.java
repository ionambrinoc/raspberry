import java.util.HashMap;
import java.util.Map;

public class MessageProcessor {
	//maps a symbol to book
	Map<Integer, Book> books = new HashMap<Integer, Book>();	
	
	public void addOrderMessage(Message msg){
		Order order = new Order(msg);
		Book book = books.get(msg.symbol);
		if(book == null){
			book = new Book(msg.symbol);
			books.put(msg.symbol, book);
		}
		book.addOrder(order);
	}
	
	public void modifyMessage(Message msg){
		Book book = books.get(msg.symbol);
		if(book != null){
			Order newOrder = new Order(msg);
			book.modifyOrder(newOrder);
		}else
			System.out.println("Modify: symbol doesn't exist");
	}
	
	public void deleteMessage(Message msg){
		Book book = books.get(msg.symbol);
		if(book != null){
			book.deleteOrder(msg.id);
		}else
			System.out.println("Delete: symbol doesn't exist");
			
	}
	
	public void executionMessage(Message msg){
		Book book = books.get(msg.symbol);
		if(book != null){
			book.execute(msg.price, msg.vol, msg.time);
		}else
			System.out.println("Execution: symbol doesn't exist");
	}

	public Statistic tradeMessage(Message msg) {
		Book book = books.get(msg.symbol);
		if(book != null){
			return book.trade(msg.price, msg.vol, msg.time);
		}else
			System.out.println("Trade: symbol doesn't exist");
		return null;
	}
}
