import java.util.Arrays;

public class Message 
{
	public static int type;
	public static int time;
	public static int id;
	public static int vol;
	public static int price;
	public static boolean buy;
	public static String symbol;
	
	public Message (int t, int tm, int i, int v, int p, boolean b, String s)
	{
		this.type = t;  this.price = p;	 this.time=   tm; this.id=i;
		this.vol  = v;  this.buy   = b;  this.symbol = s;
	}
	
	public Message (byte[] bytes)
	{
		byte[] type    = Arrays.copyOfRange(bytes, 2, 4); 
			this.type  = byteToInt(type);
		byte[] time    = Arrays.copyOfRange(bytes, 4, 8);
			this.time  = byteToInt(time);
		byte[] symb    = Arrays.copyOfRange(bytes, 8,12);
			this.symbol= symb.toString();
		byte[] id      = Arrays.copyOfRange(bytes,16,20);
			this.id    = byteToInt(id);
		byte[] price   = Arrays.copyOfRange(bytes,20,24);
			this.price = byteToInt(price);
		byte[] vol     = Arrays.copyOfRange(bytes,24,28);
			this.vol   = byteToInt(vol);
		byte[] buy     = Arrays.copyOfRange(bytes,28,29);
			    int b  = byteToInt(buy);
	    if (b==0) 
	    	   this.buy=false;
		  else this.buy=true;
	}
	
	public byte[] toBytes()
	{
		byte[] bytes = null;
		
		return bytes;
	}
	
	public int byteToInt (byte[] b)
	{    
	    int MASK = 0xFF;
	    int result = 0;   
	        result = b[0] & MASK;
	        result = result + ((b[1] & MASK) << 8);
	        result = result + ((b[2] & MASK) << 16);
	        result = result + ((b[3] & MASK) << 24);            
	    return result;
	}
}
