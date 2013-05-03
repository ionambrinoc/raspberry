package controller;

import java.util.concurrent.LinkedBlockingQueue;

import parser.ControllerMessage;

public class MessageReader {
	
	protected ParserReader parserReader;
	protected MessageHistory messageHistory;
	protected MessageSender messageSender;
	protected SymbolAssignment symbolAssignment;
	protected LinkedBlockingQueue<ControllerMessage> messageQueue;
	
	
	public MessageReader(ParserReader parserReader, MessageHistory messageHistory, SymbolAssignment symbolAssignment, MessageSender messageSender, LinkedBlockingQueue<ControllerMessage> messageQueue){
		this.parserReader = parserReader;
		this.messageHistory = messageHistory;
		this.symbolAssignment = symbolAssignment;
		this.messageSender = messageSender;
		this.messageQueue = messageQueue;
	}
	
	public void ReadAndSendNextMessage(){
	
		ControllerMessage nextMessage = messageQueue.poll();
		if (nextMessage != null)
				{
		String nextPi = symbolAssignment.addSymbol(nextMessage.symbol);
		messageHistory.addMessage(nextMessage.message, nextMessage.orderId, nextPi);
		messageSender.sendMessage(nextMessage.message, nextPi);
	}
	}
}
