import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SampleDataCreator {
	DataOutputStream fo;
	
	public SampleDataCreator() {
		try {
			fo = new DataOutputStream(new FileOutputStream(new File("data.dat")));
		} catch (FileNotFoundException e) {
			System.out.println("fail creating file");
		}
	}

	public void putAddMessage(int symbolIndex, int orderId, 
			int price, int volume, char side) {
		try {
			fo.writeShort((short)31);
			fo.writeShort((short) 100);	//type
			fo.writeInt(0);				//time
			fo.writeInt(symbolIndex);	//symbol index
			fo.writeInt(0);				//symbol sequence num
			fo.writeInt(orderId);		//order id
			fo.writeInt(price);			//price
			fo.writeInt(volume);		//volume
			fo.write((byte)side);		//side
			fo.write((byte)0);			//OrderIDGTCIndicator
			fo.write((byte)0);			//TradeSession
		} catch (IOException e) {
			System.out.println("fail write message");
		}
	}
	
	public void putModifyMessage(int symbolIndex, int orderId, 
			int price, int volume, char side) {
		try {
			fo.writeShort((short)31);
			fo.writeShort((short)101);	//type
			fo.writeInt(0);				//time
			fo.writeInt(symbolIndex);	//symbol index
			fo.writeInt(0);				//symbol sequence num
			fo.writeInt(orderId);		//order id
			fo.writeInt(price);			//price
			fo.writeInt(volume);		//volume
			fo.write((byte)side);		//side
			fo.write((byte)0);			//OrderIDGTCIndicator
			fo.write((byte)0);			//ReasonCode
		} catch (IOException e) {
			System.out.println("fail write message");
		}
	}
	
	public void putDeleteMessage(int symbolIndex, int orderId, char side) {
		try {
			fo.writeShort((short)23);
			fo.writeShort((short)102);	//type
			fo.writeInt(0);				//time
			fo.writeInt(symbolIndex);	//symbol index
			fo.writeInt(0);				//symbol sequence num
			fo.writeInt(orderId);		//order id
			fo.write((byte)side);		//side
			fo.write((byte)0);			//OrderIDGTCIndicator
			fo.write((byte)1);			//ReasonCode
		} catch (IOException e) {	
			System.out.println("fail write message");
		}
	}
	
	public void putExecutionMessage(int symbolIndex, int orderId, 
			int price, int volume) {
		try {
			fo.writeShort((short)34);
			fo.writeShort((short)103);	//type
			fo.writeInt(0);				//time
			fo.writeInt(symbolIndex);	//symbol index
			fo.writeInt(0);				//symbol sequence num
			fo.writeInt(orderId);		//order id
			fo.writeInt(price);			//price
			fo.writeInt(volume);		//volume
			fo.write((byte)0);			//OrderIDGTCIndicator
			fo.write((byte)0);			//ReasonCode
			fo.writeInt(0);				//TradeID			
		} catch (IOException e) {
			System.out.println("fail write message");
		}
	}
	
	public void finish(){
		try {
			fo.close();
		} catch (IOException e) {
			System.out.println("fail close file");
		}
	}
}
