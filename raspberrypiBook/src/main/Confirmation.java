package main;
import java.nio.ByteBuffer;

public class Confirmation 
{
	public final int orderId;
	
	public Confirmation(int orderId) { this.orderId = orderId; }
	public Confirmation(byte[] conf) { this.orderId = byteToInt(conf); }
	
	public byte[] toByte()           { return ByteBuffer.allocate(4).putInt(orderId).array(); }
	
	public int byteToInt (byte[] b)
	{    
		ByteBuffer wrapped = ByteBuffer.wrap(b); // big-endian by default
		int num = wrapped.getInt(0); // 1
		return num;
	}
}
