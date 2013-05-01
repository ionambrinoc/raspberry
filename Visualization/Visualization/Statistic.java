package Visualization;
import java.nio.ByteBuffer;
import DisplayStrategy.DisplayStatisticStrategy;
import DisplayStrategy.DisplayTableRow;


public class Statistic {
	protected final DisplayStatisticStrategy displayStrategy;
	public final int symbol;
	public final int price;
	public final int change;
	public final int volume;
	public final int open;
	public final int high;
	public final int low;
	public final int prevClose;
	public final int vWAP;
	public final int sMM;
	public final int sMA;
	public final int mACD;
	public final int mACDSignal;
	public final int histogram;
	
	public Statistic(byte[] bs){
		ByteBuffer bb = ByteBuffer.wrap(bs);
		this.symbol = bb.getInt(0);
		this.price = bb.getInt(4);
		this.change = bb.getInt(8);
		this.volume = bb.getInt(12);
		this.open = bb.getInt(16);
		this.high = bb.getInt(20);
		this.low = bb.getInt(24);
		this.prevClose = bb.getInt(28);
		this.vWAP = bb.getInt(32);
		this.sMM = bb.getInt(36);
		this.sMA = bb.getInt(40);
		this.mACD = bb.getInt(44);
		this.mACDSignal = bb.getInt(48);
		this.histogram = bb.getInt(52);
		
		displayStrategy = new DisplayTableRow(this);
	}
	
	public String getSymbol(){
		return "EIL";
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
	public int getPrevClose(){
		return prevClose;
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
	public int getMACD(){
		return mACD;
	}
	public int getMACDSignal(){
		return mACDSignal;
	}
	public int getHistogram(){
		return histogram;
	}
	
	public DisplayStatisticStrategy getDisplayStrategy() {
		return displayStrategy;
	}
}
