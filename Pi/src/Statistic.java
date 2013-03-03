
public class Statistic {
	public final String symbol;
	public final int timestamp;
	public final int price;
	public final int volume;
	
	public Statistic(String symbol, int timestamp, int price, int volume){
		this.symbol = symbol;
		this.timestamp = timestamp;
		this.price = price;
		this.volume = volume;
	}
}
