package parser;

public class ControllerMessage {
	public final byte[] message;
	public final int orderId;
	public final int symbol;
	
	public ControllerMessage(Message message) {
		this.message = message.toBytes();
		orderId = message.id;
		symbol = message.symbol;
	}

}
