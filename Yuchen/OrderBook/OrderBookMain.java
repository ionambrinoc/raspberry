import java.util.Map;

public class OrderBookMain {
		
	public static void main(String[] args) {
		Map<Integer, Book> books;	// maps a symbol to a it's book
		PiNetwork network = new PiNetwork();	//network interface
		MessageProcessor processor = new MessageProcessor();
		
		while(true){
			Message msg = new Message(network.recv());
			switch(msg.type){
				case 100:
					processor.addOrderMessage(msg);
					break;
				case 101:
					processor.modifyMessage(msg);
					break;
				case 102:
					processor.deleteMessage(msg);
					break;
				case 103:
					processor.executionMessage(msg);
					break;
				case -36:
					Statistic s = processor.tradeMessage(msg);
					if(s != null) {
						network.sendToVisualization(s.toByte());
						System.out.println(s.toString());
					}
					break;
				default:
					System.out.println("bad msg type");
					break;
			}
		}
	}
}