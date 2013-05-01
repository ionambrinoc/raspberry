package controller;

import parser.ControllerMessage;

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
	
		ControllerMessage nextMessage = parserReader.getMessage();
		String nextPi = symbolAssignment.addSymbol(nextMessage.symbol);
		messageHistory.addMessage(nextMessage.message, nextMessage.orderId, nextPi);
		messageSender.sendMessage(nextMessage.message, nextPi);
	}
}
