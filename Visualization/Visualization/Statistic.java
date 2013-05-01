package Visualization;
import java.nio.ByteBuffer;

public class Statistic {
	public final String symbol;
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
}
