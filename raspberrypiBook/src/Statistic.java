import java.nio.ByteBuffer;
import Visualization.DisplayStrategy.DisplayStatisticStrategy;
import Visualization.DisplayStrategy.DisplayTableRow;

public abstract class Statistic {
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

	public Statistic(byte[] bs)
	{
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
	
	public Statistic(int s, int p, int ch, int v, int o, int h, int l, int pc, int vwap, int smm, int sma, int macd, ing signal, int histo)
		symbol=s;
		price=p;
		change=ch;
		volume = v;
		open = o;
		high = h;
		low = l;
		prevClose = pc;
		vWAP = vwap;
		sMM = smm;
		sMA = sma;
		mACD = macd;
		mACDSignal = signal;
		histogram = histo;
	}
	
	public byte[] toByte()
	{
		byte[] array = new byte[56];
		byte[] s = ByteBuffer.allocate(4).putInt(symbol).array();
		byte[] p = ByteBuffer.allocate(4).putInt(price).array();
		byte[] ch= ByteBuffer.allocate(4).putInt(change).array();
		byte[] v= ByteBuffer.allocate(4).putInt(volume).array();
		byte[] o= ByteBuffer.allocate(4).putInt(open).array();
		byte[] h= ByteBuffer.allocate(4).putInt(high).array();
		byte[] l= ByteBuffer.allocate(4).putInt(low).array();
		byte[] pc= ByteBuffer.allocate(4).putInt(prevClose).array();
		byte[] vwap= ByteBuffer.allocate(4).putInt(vWAP).array();
		byte[] smm= ByteBuffer.allocate(4).putInt(sMM).array();
		byte[] sma= ByteBuffer.allocate(4).putInt(sMA).array();
		byte[] macd= ByteBuffer.allocate(4).putInt(mACD).array();
		byte[] signal= ByteBuffer.allocate(4).putInt(mACDSignal).array();
		byte[] histo= ByteBuffer.allocate(4).putInt(histogram).array();
		
		System.arraycopy(s,0,array,0,4);
		System.arraycopy(p,0,array,4,4);
		System.arraycopy(ch,0,array,8,4);
		System.arraycopy(v,0,array,12,4);
		System.arraycopy(o,0,array,16,4);
		System.arraycopy(h,0,array,20,4);
		System.arraycopy(l,0,array,24,4);
		System.arraycopy(pc,0,array,28,4);
		System.arraycopy(vwap,0,array,32,4);
		System.arraycopy(smm,0,array,36,4);
		System.arraycopy(sma,0,array,40,4);
		System.arraycopy(macd,0,array,44,4);
		System.arraycopy(signal,0,array,48,4);
		System.arraycopy(histo,0,array,52,4);
		
		return array;
	}

	public String getSymbol(){
		return ""+symbol;
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
