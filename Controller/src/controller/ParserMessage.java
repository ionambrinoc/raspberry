package controller;


public class ParserMessage {
	public byte[] message = "hello".getBytes();
	public Integer orderID =1;
	public Integer symbol =1;
	
	public ParserMessage(int symbol){
		this.symbol = symbol;
	}
	
	
	
	
}
