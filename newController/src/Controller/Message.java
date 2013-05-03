package Controller;


import java.nio.ByteBuffer;
import java.util.Arrays;

public class Message 
{
	public final byte type; 	public final int vol;
	public final int time;      public final int price;
	public final int id;  	    public final boolean buy;
	public final int symbol;

	public Message (int t, int tm, int i, int v, int p, boolean b, int s)
	{
		this.type = (byte)t;  this.price = p;	 this.time=   tm; this.id=i;
		this.vol  = v;  this.buy   = b;  this.symbol = s;
	}

	public Message (byte[] bytes)
	{
		byte[] type    = Arrays.copyOfRange(bytes, 0, 1); 
			this.type  = type[0];
		byte[] time    = Arrays.copyOfRange(bytes, 1, 5);
			this.time  = byteToInt(time);
		byte[] symb    = Arrays.copyOfRange(bytes, 5, 9);
			this.symbol= byteToInt(symb);
		byte[] id      = Arrays.copyOfRange(bytes, 9,13);
			if (byteToInt(id)<0) this.id=-byteToInt(id);
				else this.id    = byteToInt(id);
		byte[] price   = Arrays.copyOfRange(bytes,13,17);
			if (byteToInt(price)<0) this.price=-byteToInt(price);
				else this.price    = byteToInt(price);
		byte[] vol     = Arrays.copyOfRange(bytes,18,22);
			this.vol   = byteToInt(vol);
		byte[] buy     = Arrays.copyOfRange(bytes,17,18);
	    if (buy[0]==0) 
	    	   this.buy=false;
		  else this.buy=true;
	}

	public byte[] toBytes()
	{
		byte bb;		if (buy==true) bb=1; else bb=0;
		byte[] t    = new byte[1]; t[0]=type;
		byte[] tm   = ByteBuffer.allocate(4).putInt(time).array();
		byte[] symb = ByteBuffer.allocate(4).putInt(symbol).array();
		byte[] i    = ByteBuffer.allocate(4).putInt(id).array();
		byte[] p    = ByteBuffer.allocate(4).putInt(price).array();
 		byte[] b    = new byte[1]; b[0]=bb;
		byte[] v    = ByteBuffer.allocate(4).putInt(vol).array();

		byte[] bytes = new byte[23];
		System.arraycopy(t,0,bytes,0,1);
		System.arraycopy(tm,0,bytes,1,4);
		System.arraycopy(symb,0,bytes,5,4);
		System.arraycopy(i,0,bytes,9,4);
		System.arraycopy(p,0,bytes,13,4);		
		System.arraycopy(b,0,bytes,17,1);
		System.arraycopy(v,0,bytes,18,4);

		return bytes;
	}
	
	//to use for testing Message class
	public String toString(){
		return "Symbol: "+symbol+". type: "+type+". id: "+id+". price: "+price+". volume: "+vol+". time: "+time+". buy?:"+buy;

	}

	public int byteToInt (byte[] b)
	{    
		ByteBuffer wrapped = ByteBuffer.wrap(b); // big-endian by default
		int num = wrapped.getInt(0); // 1
		return num;
	}
}