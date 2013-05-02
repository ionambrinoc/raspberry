package Visualization;
import java.nio.ByteBuffer;
public class Statistic {
	public final String symbol;
	public final int symInt;
	public final int price;
	public final int change;
	public final int volume;
	public final int open;
	public final int high;
	public final int low;
	public final int vWAP;
	public final int sMM;
	public final int sMA;
	public final int time;
	
	public Statistic(byte[] bs){
		ByteBuffer bb = ByteBuffer.wrap(bs);
		this.symbol = Integer.toString(bb.getInt(0));
		this.symInt = bb.getInt(0);
		this.price = bb.getInt(4);
		this.change = bb.getInt(8);
		this.volume = bb.getInt(12);
		this.open = bb.getInt(16);
		this.high = bb.getInt(20);
		this.low = bb.getInt(24);
		this.vWAP = bb.getInt(28);
		this.sMM = bb.getInt(32);
		this.sMA = bb.getInt(36);
		this.time = bb.getInt(40);
	}
	
	public String getSymbol(){
		return symbol;
	}
	public int getPrice(){
		return price;
	}
	public int getChange(){
		return change;
	}
	public int getVolume(){
		return volume;
	}
	public int getOpen(){
		return open;
	}
	public int getHigh(){
		return high;
	}
	public int getLow(){
		return low;
	}
	public int getVWAP(){
		return vWAP;
	}
	public int getSMM(){
		return sMM;
	}
	public int getSMA(){
		return sMA;
	}
	public int getTime(){
		return time;
	}
	
	public Statistic(int s, int p, int c, int v, int o, int h, int l, int vwap, int smm, int sma, int t){
		this.symInt = s;
		this.price = p;
		this.change = c;
		this.volume = v;
		this.open = o;
		this.high = h;
		this.low = l;
		this.vWAP = vwap;
		this.sMM = smm;
		this.sMA = sma;
		this.time = t;
		this.symbol = Integer.toString(s);
	}
	
	public byte[] toByte()
	{
		byte[] array = new byte[44];
		byte[] s = ByteBuffer.allocate(4).putInt(symInt).array();
		byte[] p = ByteBuffer.allocate(4).putInt(price).array();
		byte[] c= ByteBuffer.allocate(4).putInt(change).array();
		byte[] v= ByteBuffer.allocate(4).putInt(volume).array();
		byte[] o = ByteBuffer.allocate(4).putInt(open).array();
		byte[] h = ByteBuffer.allocate(4).putInt(high).array();
		byte[] l = ByteBuffer.allocate(4).putInt(low).array();
		byte[] vwap = ByteBuffer.allocate(4).putInt(vWAP).array();
		byte[] smm = ByteBuffer.allocate(4).putInt(sMM).array();
		byte[] sma = ByteBuffer.allocate(4).putInt(sMA).array();
		byte[] t = ByteBuffer.allocate(4).putInt(time).array();
		System.arraycopy(s,0,array,0,4);
		System.arraycopy(p,0,array,4,4);
		System.arraycopy(c,0,array,8,4);
		System.arraycopy(v,0,array,12,4);
		System.arraycopy(o,0,array,16,4);
		System.arraycopy(h,0,array,20,4);
		System.arraycopy(l,0,array,24,4);
		System.arraycopy(vwap,0,array,28,4);
		System.arraycopy(smm,0,array,32,4);
		System.arraycopy(sma,0,array,36,4);
		System.arraycopy(t,0,array,40,4);
		
		return array;
	}

}