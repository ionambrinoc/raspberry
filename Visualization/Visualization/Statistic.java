package Visualization;
import java.nio.ByteBuffer;
import DisplayStrategy.DisplayStatisticStrategy;


public abstract class Statistic {
	public final int symbol;
	public final int type;
	public final int stat1;
	public final int stat2;
	
	public Statistic(byte[] bs){
		ByteBuffer bb = ByteBuffer.wrap(bs);
		this.symbol = bb.getInt(0);
		this.type = bb.getInt(4);
		this.stat1 = bb.getInt(8);
		this.stat2 = bb.getInt(12);
	}
	
	public int getSymbol(){
		return symbol;
	}
	
	public int getType(){
		return type;
	}
	
	public int getStat1(){
		return stat1;
	}
	
	public int getStat2(){
		return stat2;
	}
	
	public abstract DisplayStatisticStrategy getDisplayStrategy();
}
