package Controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class Parser extends Thread {
	LinkedBlockingQueue<Message> queue;
	FileReader reader;
	
	public class FileReader {
		DataInputStream di;
		final String data1 = "data1.dat";
		final String data2 = "data2.dat";
		final String data3 = "data3.dat";
		String current = data1;


		public FileReader() {
			try {
				this.di = new DataInputStream(new FileInputStream(new File(data1)));
			} catch (FileNotFoundException e) {
				System.out.println("fail to open the file");
			}
		}
		
		// returns the number of message to read, or 0 if the flag is not 11
		public int decodePacketHeader(){
			try {
				short size = Short.reverseBytes((di.readShort()));
				byte flag = di.readByte();
				byte numOfMsg = di.readByte();
				 
				if(flag != 11){
					di.skip(size-4);
					numOfMsg = 0;
				}else
					di.skip(12);
				
				return numOfMsg;
			} catch (IOException e) {
				if(current == data1) current = data2;
				else if(current == data2) current = data3;
				try {
					di.close();
					di = new DataInputStream(new FileInputStream(new File(current)));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println("switched to "+current);
				return 0;
			}
		}
			
		// null if useless message
		public Message nextMessage(){
			try{
				short size = Short.reverseBytes(di.readShort());
				short type = Short.reverseBytes(di.readShort());
				switch(type){
					case 100:
						if(size != 31){
							System.out.println("invalid size: "+size);
							return null;
						}
						return getAddMessage();
					case 101:
						if(size != 31){
							System.out.println("invalid size: "+size);
							return null;
						}
						return getModifyMessage();
					case 102:
						if(size != 23){
							System.out.println("invalid size: "+size);
							return null;
						}
						return getDeleteMessage();
					case 103:
						if(size != 34){
							System.out.println("invalid size: "+size);
							return null;
						}
						return getExecutionMessage();
					case 220:
						if(size != 54){
							System.out.println("invalid size: "+size);
							return null;
						}
						return getTradeMessage();
					default:
						di.skip(size-4);
						return null;
				}
			}
			catch(IOException e){
				System.out.println("nextMessage");
			}
			return null;
		}
		
		private Message getAddMessage() {
			try {
				int type = 100;	
				int time = Integer.reverseBytes(di.readInt());
				int sIndex = Integer.reverseBytes(di.readInt());
				di.skip(4);	//SymbolSeqNum 
				int orderId = Integer.reverseBytes(di.readInt());
				int price = Integer.reverseBytes(di.readInt());
				int volume = Integer.reverseBytes(di.readInt());
				char side = (char)di.read();
				boolean b;
				if(side == 'B') b = true;
				else if(side == 'S') b = false;
				else {
					System.out.println("side in getAddMessage: "+side);
					return null;
				}
				di.skip(2);	//OrderIDGTCIndicator and TradeSession
				return new Message(type, time, orderId, volume, price, b , sIndex);
			} catch (IOException e) {
//				System.out.println("getAddMessage");
			}
			return null;
		}

		private Message getModifyMessage() {
			try {
				int type = 101;	
				int time = Integer.reverseBytes(di.readInt());
				int sIndex = Integer.reverseBytes(di.readInt());
				di.skip(4);	//SymbolSeqNum 
				int orderId = Integer.reverseBytes(di.readInt());
				int price = Integer.reverseBytes(di.readInt());
				int volume = Integer.reverseBytes(di.readInt());
				char side = (char)di.read();
				boolean b;
				if(side == 'B') b = true;
				else if(side == 'S') b = false;
				else {
					System.out.println("side in getModifyMessage: "+side);
					return null;
				}
				di.skip(2);	//OrderIDGTCIndicator and reason code
				return new Message(type, time, orderId, volume, price, b , sIndex);
			} catch (IOException e) {
//				System.out.println("getModifyMessage");
			}
			return null;
		}
		
		private Message getDeleteMessage() {
			try {
				int type = 102;	
				int time = Integer.reverseBytes(di.readInt());
				int sIndex = Integer.reverseBytes(di.readInt());
				di.skip(4);	//SymbolSeqNum 
				int orderId = Integer.reverseBytes(di.readInt());
				char side = (char)di.read();
				boolean b;
				if(side == 'B') b = true;
				else if(side == 'S') b = false;
				else {
					System.out.println("side in getDeleteMessage: "+side);
					return null;
				}
				di.skip(2);	//OrderIDGTCIndicator and reason code
				return new Message(type, time, orderId, -1, -1, b , sIndex);
			} catch (IOException e) {
//				System.out.println("getDeleteMessage");
			}
			return null;
		}
		
		private Message getExecutionMessage() {
			try {
				int type = 103;	
				int time = Integer.reverseBytes(di.readInt());
				int sIndex = Integer.reverseBytes(di.readInt());
				di.skip(4);	//SymbolSeqNum 
				int orderId = Integer.reverseBytes(di.readInt());
				int price = Integer.reverseBytes(di.readInt());
				int volume = Integer.reverseBytes(di.readInt());
				boolean b;
				di.skip(6);	//OrderIDGTCIndicator and reason code and trade id
				return new Message(type, time, orderId, volume, price, false , sIndex);
			} catch (IOException e) {
//				System.out.println("getExecutionMessage");
			}
			return null;
		}
		
		private Message getTradeMessage() {
			try {
				int type = 220;
				int time = Integer.reverseBytes(di.readInt());
				di.skip(4);
				int sIndex = Integer.reverseBytes(di.readInt());
				di.skip(8);	
				int price = Integer.reverseBytes(di.readInt());
				int volume = Integer.reverseBytes(di.readInt());
				di.skip(22);
				return new Message(type, time, -1, volume, price, false , sIndex);
			} catch (IOException e) {
//				System.out.println("getTradeMessage");
			}
			return null;
		}
	}
	
	public Parser(LinkedBlockingQueue<Message> queue) {
		this.queue = queue;
		reader = new FileReader();
	}

	public void run() {
		while(true){
			int num = 0;
			
			while(num == 0){
				num = reader.decodePacketHeader();
			}
			
			for(int j=0; j<num; j++){
				Message msg = reader.nextMessage();
				
				if(msg != null) {
					try {
						queue.put(msg);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}



