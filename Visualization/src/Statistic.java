import DisplayStrategy.DisplayStatisticStrategy;


public class Statistic {
	public final String symbol;
	public final int timestamp;
	public final int price;
	public final int volume;
	protected final DisplayStatisticStrategy displayStrategy = new DisplayStatisticStrategy()
	
	public Statistic(String symbol, int timestamp, int price, int volume){
		this.symbol = symbol;
		this.timestamp = timestamp;
		this.price = price;
		this.volume = volume;
	}
	
	public String getSymbol(){
		return symbol;
	}
	
	public int getTimeStamp(){
		return timestamp;
	}
	
	public int getPrice(){
		return price;
	}
	
	public int getVolume(){
		return volume;
	}
	
	public abstract DisplayStatisticStrategy getDisplayStrategy(){
		return new DisplayStatisticStrategy
	}
}
