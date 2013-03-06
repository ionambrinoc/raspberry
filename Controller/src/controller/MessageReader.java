package controller;

public class MessageReader {
	
	protected ParserReader parserReader;
	protected MessageHistory messageHistory;
	protected MessageSender messageSender;
	protected SymbolAssignment symbolAssignment;
	
	
	public MessageReader(ParserReader parserReader, MessageHistory messageHistory, SymbolAssignment symbolAssignment, MessageSender messageSender){
		this.parserReader = parserReader;
		this.messageHistory = messageHistory;
		this.symbolAssignment = symbolAssignment;
		this.messageSender = messageSender;
	}
	
	public void ReadAndSendNextMessage(){
	
		ParserMessage nextMessage = parserReader.getMessage();
		int nextPi = symbolAssignment.addSymbol(nextMessage.symbol);
		messageHistory.addMessage(nextMessage.message, nextMessage.orderID, nextPi);
		messageSender.sendMessage(nextMessage.message, nextPi);
	}
}
