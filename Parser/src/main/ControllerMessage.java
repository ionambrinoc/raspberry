package main;

public class ControllerMessage {
	public final String message;
	public final int orderId;
	public final int symbol;
	
	public ControllerMessage(Message message) {
		this.message = message.toString();
		orderId = message.id;
		symbol = message.symbol;
	}

}
