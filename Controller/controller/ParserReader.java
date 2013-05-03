package controller;

import parser.ControllerMessage;

public class ParserReader {
	public int i = 0;
	public ControllerMessage getMessage(){
		i++;
		return new ControllerMessage(null);
		}
	}
	
