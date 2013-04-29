package main;

public class ControllerMessage {
	public final Message message;
	public final int orderId;
	public final int symbol;
	
	public ControllerMessage(Message message) {
		this.message = message;
		orderId = message.id;
		symbol = message.symbol;
	}

}
