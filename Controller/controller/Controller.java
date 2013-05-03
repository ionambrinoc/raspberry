package controller;

import java.util.concurrent.LinkedBlockingQueue;

import parser.ControllerMessage;
import parser.Parse;

import networking.ControllerNetwork;

public class Controller {

	protected ControllerNetwork controllerNetwork;
	protected MessageSender messageSender;
	protected MessageHistory messageHistory;
	protected SymbolAssignment symbolAssignment;
	protected ParserReader parserReader;
	protected MessageReader messageReader;
	protected PiManager piManager;
	protected LinkedBlockingQueue<ControllerMessage> messageQueue;
	protected Parse parse;
	
	public Controller(){
		this.symbolAssignment = new SymbolAssignment();
		this.controllerNetwork = new ControllerNetwork();
		this.messageSender = new MessageSender(controllerNetwork);
		this.messageHistory = new MessageHistory(messageSender);
		this.parserReader = new ParserReader();
		this.piManager = new PiManager(messageSender, symbolAssignment, messageHistory);
		this.messageQueue = new LinkedBlockingQueue<ControllerMessage>(10);
		this.parse = new Parse(messageQueue);
		this.messageReader = new MessageReader(parserReader, messageHistory, symbolAssignment, messageSender, messageQueue);
		parse.start();
	}
	
	public static void main(String[] args) {
		Controller controller = new Controller();
		while (true){
			
			if (controller.controllerNetwork.hasMessage()){
				System.out.println("up");
				controller.piManager.executeMessage(controller.controllerNetwork.nextMessage());
			}
			else {
				if (controller.symbolAssignment.pisAvailable()) controller.messageReader.ReadAndSendNextMessage();
			}
		}
	}

}
