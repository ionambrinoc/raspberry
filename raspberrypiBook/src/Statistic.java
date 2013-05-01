import java.nio.ByteBuffer;


public abstract class Statistic {
	public final int symbol;
	public final int type;
	public final int stat1;
	public final int stat2;

	public Statistic(byte[] bs)
	{
		ByteBuffer bb = ByteBuffer.wrap(bs);
		this.symbol = bb.getInt(0);
		this.type = bb.getInt(4);
		this.stat1 = bb.getInt(8);
		this.stat2 = bb.getInt(12);
	}
	
	public Statistic(int s, int t, int s1, int s2)
	{	symbol=s; type=t; stat1=s1; stat2=s2; }
	
	public byte[] toByte()
	{
		byte[] array = new byte[16];
		byte[] s = ByteBuffer.allocate(4).putInt(symbol).array();
		byte[] t = ByteBuffer.allocate(4).putInt(type).array();
		byte[] s1= ByteBuffer.allocate(4).putInt(stat1).array();
		byte[] s2= ByteBuffer.allocate(4).putInt(stat2).array();
		System.arraycopy(s,0,array,0,4);
		System.arraycopy(t,0,array,4,4);
		System.arraycopy(s1,0,array,8,4);
		System.arraycopy(s2,0,array,12,4);
		
		return array;
	}

	public int getSymbol() {	return symbol;  }
	public int getType()   {	return type;	}
	public int getStat1()  {	return stat1;	}
	public int getStat2()  {	return stat2;   }

}